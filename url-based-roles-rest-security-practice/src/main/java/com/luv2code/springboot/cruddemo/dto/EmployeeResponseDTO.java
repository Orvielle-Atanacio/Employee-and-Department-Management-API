package com.luv2code.springboot.cruddemo.dto;

// A Data Transfer Object (DTO) used for sending employee data back to the client.
// It provides a tailored view of the data, exposing only what the client needs.
// This protects the internal Entity structure (e.g., hiding the ID or other sensitive fields if necessary).

public record EmployeeResponseDTO(

        // The first name of the employee to be shown to the client.
        String firstName,

        // The email address of the employee to be shown to the client.
        String email,

        // The employee id to be shown to the client.
        int id,

        // The employee department ID
        DepartmentResponseDTO department) {
}