package com.example.studentmanagement.exception;

/**
 * Thrown when another student already uses the email (becomes HTTP 409).
 */
public class DuplicateEmailException extends RuntimeException {

    public DuplicateEmailException(String email) {
        super("A student with email " + email + " already exists");
    }
}
