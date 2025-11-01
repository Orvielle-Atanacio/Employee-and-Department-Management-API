package com.luv2code.springboot.cruddemo.service;


import com.luv2code.springboot.cruddemo.dto.CreateEmployeeRequestDTO;
import com.luv2code.springboot.cruddemo.dto.EmployeeResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

// This interface defines the contract for the Employee Service layer.
// It declares the business logic available for the Employee entity.
// The implementation of this interface will contain the actual logic.
public interface EmployeeService {

    Page<EmployeeResponseDTO> getAllEmployees(Pageable pageable);

    EmployeeResponseDTO createEmployee(CreateEmployeeRequestDTO request);

    EmployeeResponseDTO getEmployeeById(int id);

    EmployeeResponseDTO updateEmployee(int id, CreateEmployeeRequestDTO request);

    void deleteEmployee(int id);

    Page<EmployeeResponseDTO> getEmployeesByDepartment(Long departmentId, Pageable pageable);

    // Keep entity methods internal or remove if not needed
}
