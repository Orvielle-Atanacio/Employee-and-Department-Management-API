package com.luv2code.springboot.cruddemo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

// A Data Transfer Object (DTO) used for creating a new employee.
// Records are immutable (data cannot be changed after creation), making them perfect for DTOs.
// This DTO defines the exact structure of the data the API expects from a client (like a web form or frontend)
// when creating an employee.
// Using a DTO shields the internal Employee Entity from direct exposure to the API.

public record CreateEmployeeRequestDTO(
        // The first name of the employee to be created.
        @NotBlank(message = "First name is mandatory")
        String firstName,
        // The last name of the employee to be created.
        @NotBlank(message = "Last name is mandatory")
        String lastName,
        // The email address of the employee to be created.

        @NotBlank(message = "Email is mandatory")
        @Email(message = "Email should be valid")
        String email,
        
        @NotBlank(message = "Department name is mandatory")
        String departmentName) {

    public CreateEmployeeRequestDTO {
        java.util.Objects.requireNonNull(firstName, "firstName must not be null");
        java.util.Objects.requireNonNull(lastName, "lastName must not be null");
    }
}