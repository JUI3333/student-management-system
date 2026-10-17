package com.example.studentmanagement.exception;

/**
 * Thrown when no student exists for the requested id (becomes HTTP 404).
 */
public class StudentNotFoundException extends RuntimeException {

    public StudentNotFoundException(Long id) {
        super("Student with id " + id + " was not found");
    }
}
