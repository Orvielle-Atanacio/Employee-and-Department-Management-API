package com.luv2code.springboot.cruddemo.exceptionhandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


// This annotation tells Spring to automatically map this exception to an HTTP 404 (NOT FOUND) status code.
// When this exception is thrown from a controller, Spring will use this status instead of the default 500.

@ResponseStatus(value = HttpStatus.CONFLICT)
public class CannotDeleteException extends RuntimeException {

    // Constructor with a custom error message.
    public CannotDeleteException(String message) {
        super(message);
    }

    // Constructor with a message and the original cause of the exception (for chaining exceptions).
    public CannotDeleteException(String message, Throwable cause) {
        super(message, cause);
    }

    // Constructor with only the original cause.
    public CannotDeleteException(Throwable cause) {
        super(cause);
    }
}
