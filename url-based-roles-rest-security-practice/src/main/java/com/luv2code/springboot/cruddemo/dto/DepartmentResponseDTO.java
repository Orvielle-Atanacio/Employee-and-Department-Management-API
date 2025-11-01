package com.luv2code.springboot.cruddemo.dto;

import com.luv2code.springboot.cruddemo.entity.Department;

public record DepartmentResponseDTO(Long id, String name) {

    // Constructor that takes Department entity - use getter methods
    public DepartmentResponseDTO(Department department) {
        this(
                department != null ? department.getId() : null,      // Use getter method
                department != null ? department.getName() : null     // Use getter method
        );
    }

    // Regular constructor
    public DepartmentResponseDTO {
        // Compact constructor for validation if needed
    }
}