package com.example.studentmanagement.dto;

import com.example.studentmanagement.model.Student;

/**
 * JSON body returned to the client (keeps the database entity hidden from the API).
 */
public record StudentResponse(Long id, String firstName, String lastName, String email, String course) {

    public static StudentResponse from(Student student) {
        return new StudentResponse(student.getId(), student.getFirstName(), student.getLastName(),
                student.getEmail(), student.getCourse());
    }
}
