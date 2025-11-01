package com.luv2code.springboot.cruddemo.dto;

import jakarta.validation.constraints.NotBlank;

public record DepartmentRequestDTO(
        @NotBlank(message = "Department name is mandatory")
        String name
) {}
