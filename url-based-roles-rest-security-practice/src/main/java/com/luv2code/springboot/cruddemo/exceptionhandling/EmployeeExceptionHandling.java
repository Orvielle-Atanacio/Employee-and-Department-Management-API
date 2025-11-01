package com.luv2code.springboot.cruddemo.exceptionhandling;

import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class EmployeeExceptionHandling {

    // Create a Logger instance for this class to log errors.
    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeExceptionHandling.class);

    // Catches any generic Exception that isn't handled by more specific handlers.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exc) {

        // 1. LOG THE FULL EXCEPTION FOR DEBUGGING (This is for developers)
        // This logs the error message AND the full stack trace at the ERROR level.
        LOGGER.error("An unexpected error occurred: ", exc); // The key is passing 'exc' as the second argument

        // 2. CREATE A GENERIC, USER-FRIENDLY MESSAGE (This is for the client)
        // Avoid exposing internal exception details which could be a security risk.
        String clientFriendlyMessage = "An internal server error occurred. Please contact support or try again later.";

        // 3. CREATE ErrorResponse WITH THE GENERIC MESSAGE
        ErrorResponse error = new ErrorResponse(
                clientFriendlyMessage, // Use the safe, generic message
                HttpStatus.INTERNAL_SERVER_ERROR.value(), // Use 500 for unexpected errors
                System.currentTimeMillis());

        // 4. RETURN THE RESPONSE
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Other exception handlers remain unchanged...
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(EmployeeNotFoundException exc) {
        // It's usually safe to show the message for a 404, as it's often just an ID.
        ErrorResponse error = new ErrorResponse(
                exc.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleException(DuplicateResourceException exc) {
        ErrorResponse error = new ErrorResponse(
                exc.getMessage(),
                HttpStatus.CONFLICT.value(),
                System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException exc) {
        // Validation errors are also safe to show to the client so they can correct
        // their input.
        String message = exc.getBindingResult().getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining(", "));
        ErrorResponse error = new ErrorResponse(
                "Validation failed: " + message,
                HttpStatus.BAD_REQUEST.value(),
                System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


}
