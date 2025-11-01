package com.luv2code.springboot.cruddemo.service;

import com.luv2code.springboot.cruddemo.dto.CreateEmployeeRequestDTO;
import com.luv2code.springboot.cruddemo.dto.EmployeeResponseDTO;
import com.luv2code.springboot.cruddemo.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface EmployeeService {

    Page<EmployeeResponseDTO> getAllEmployees(Pageable pageable);

    EmployeeResponseDTO createEmployee(CreateEmployeeRequestDTO request);

    EmployeeResponseDTO getEmployeeById(int id);

    EmployeeResponseDTO updateEmployee(int id, CreateEmployeeRequestDTO request);

    void deleteEmployee(int id);

    Page<EmployeeResponseDTO> getEmployeesByDepartment(Long departmentId, Pageable pageable);

    // Add these methods that your controller uses
    Optional<Employee> findByEmail(String email);

    Employee findById(int id);

    void deleteById(int id);

    EmployeeResponseDTO createUser(CreateEmployeeRequestDTO request);

    Employee save(Employee employee, String departmentName);
}