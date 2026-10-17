package com.example.studentmanagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * JSON body accepted by POST and PUT. The annotations define the validation rules.
 */
public record StudentRequest(
        @NotBlank(message = "First name is required") String firstName,
        @NotBlank(message = "Last name is required") String lastName,
        @NotBlank(message = "Email is required") @Email(message = "Email must be a valid address") String email,
        @NotBlank(message = "Course is required") String course) {
}
