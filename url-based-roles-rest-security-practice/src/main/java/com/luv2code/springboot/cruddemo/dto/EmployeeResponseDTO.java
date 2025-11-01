package com.luv2code.springboot.cruddemo.dto;

public record EmployeeResponseDTO(
        String firstName,
        String lastName,
        String email,
        int id,
        DepartmentResponseDTO department) {

    // Constructor that takes all fields
    public EmployeeResponseDTO {
        // Compact constructor for validation if needed
    }

    // Additional constructor for backward compatibility with your existing code
    public EmployeeResponseDTO(String firstName, String email, int id, DepartmentResponseDTO department) {
        this(firstName, null, email, id, department);
    }
}