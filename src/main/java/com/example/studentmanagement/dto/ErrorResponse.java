package com.example.studentmanagement.dto;

import java.time.Instant;
import java.util.Map;

/**
 * Standard error body. fieldErrors is only filled for validation failures.
 */
public record ErrorResponse(Instant timestamp, int status, String error, String message,
                            Map<String, String> fieldErrors) {

    public static ErrorResponse of(int status, String error, String message, Map<String, String> fieldErrors) {
        return new ErrorResponse(Instant.now(), status, error, message, fieldErrors);
    }
}
