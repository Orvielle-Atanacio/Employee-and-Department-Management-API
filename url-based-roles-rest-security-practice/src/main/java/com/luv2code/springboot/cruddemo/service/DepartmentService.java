package com.luv2code.springboot.cruddemo.service;

import com.luv2code.springboot.cruddemo.dto.DepartmentRequestDTO;
import com.luv2code.springboot.cruddemo.dto.DepartmentResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DepartmentService {

    DepartmentResponseDTO createDepartment(DepartmentRequestDTO request);

    DepartmentResponseDTO getDepartmentById(Long id);

    DepartmentResponseDTO getDepartmentByName(String name);

    Page<DepartmentResponseDTO> getAllDepartments(Pageable pageable);

    DepartmentResponseDTO updateDepartment(Long id, DepartmentRequestDTO request);

    void deleteDepartment(Long id);

}
