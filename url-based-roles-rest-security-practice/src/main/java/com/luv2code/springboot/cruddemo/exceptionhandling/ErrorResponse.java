package com.luv2code.springboot.cruddemo.exceptionhandling;

/**
 * A standardized data structure for sending error information from the API to the client.
 * Ensures all error responses have a consistent format.
 */
public class ErrorResponse {

    private String message;     // Human-readable error details for the client.
    private int statusCode;     // The HTTP status code (e.g., 404, 400, 500).
    private long timestamp;     // The time the error occurred (useful for logging and debugging).

    // All-arguments constructor for creating a complete ErrorResponse.
    public ErrorResponse(String message, int statusCode, long timestamp) {
        this.message = message;
        this.statusCode = statusCode;
        this.timestamp = timestamp;
    }

    // Public getters provide read-only access to the fields.
    // The absence of setters makes this object effectively immutable after creation,
    // which is a good practice for data transfer objects.
    public String getMessage() {
        return message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public long getTimestamp() {
        return timestamp;
    }

    // Provides a detailed string representation, primarily for server-side logging.
    @Override
    public String toString() {
        return "ErrorResponse{"
                + "message='" + message + '\''
                + ", statusCode=" + statusCode
                + ", timestamp=" + timestamp
                + '}';
    }
}
