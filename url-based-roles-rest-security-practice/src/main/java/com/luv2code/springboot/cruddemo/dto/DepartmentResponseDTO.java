package com.luv2code.springboot.cruddemo.dto;

import com.luv2code.springboot.cruddemo.entity.Department;

public record DepartmentResponseDTO(Long id, String name) {
    public DepartmentResponseDTO(Department department) {
        this(
                department != null ? department.getId() : null,
                department != null ? department.getName() : null
        );
    }
}