package com.luv2code.springboot.cruddemo.service;

import com.github.pagehelper.PageInfo;
import com.luv2code.springboot.cruddemo.dto.CreateEmployeeRequestDTO;
import com.luv2code.springboot.cruddemo.dto.EmployeeResponseDTO;
import com.luv2code.springboot.cruddemo.entity.Employee;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;



public interface EmployeeService {

    PageInfo<EmployeeResponseDTO> getAllEmployees(int pageNum, int pageSize, String orderBy);

    EmployeeResponseDTO createEmployee(CreateEmployeeRequestDTO request);

    EmployeeResponseDTO getEmployeeById(int id);

    EmployeeResponseDTO updateEmployee(int id, CreateEmployeeRequestDTO request);

    void deleteEmployee(int id);

    PageInfo<EmployeeResponseDTO> getEmployeesByDepartment(
            Long departmentId, int pageNum, int pageSize, String orderBy);

    // Add these methods that your controller uses
    Optional<Employee> findByEmail(String email);

    Employee findById(int id);

    void deleteById(int id);

    EmployeeResponseDTO createUser(CreateEmployeeRequestDTO request);

    Employee save(Employee employee, String departmentName);
}