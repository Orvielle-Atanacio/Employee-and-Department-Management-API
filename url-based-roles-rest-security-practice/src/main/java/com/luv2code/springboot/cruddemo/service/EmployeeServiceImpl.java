package com.luv2code.springboot.cruddemo.service;

import com.luv2code.springboot.cruddemo.dto.CreateEmployeeRequestDTO;
import com.luv2code.springboot.cruddemo.dto.DepartmentResponseDTO;
import com.luv2code.springboot.cruddemo.dto.EmployeeResponseDTO;
import com.luv2code.springboot.cruddemo.entity.Department;
import com.luv2code.springboot.cruddemo.entity.Employee;
import com.luv2code.springboot.cruddemo.mapper.EmployeeMapper;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private DepartmentService departmentService;

    @Override
    public Page<EmployeeResponseDTO> getAllEmployees(Pageable pageable) {
        List<Employee> employees = employeeMapper.findAll();
        int total = employeeMapper.countAll();

        // Convert entities to DTOs
        List<EmployeeResponseDTO> employeeDTOs = employees.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(employeeDTOs, pageable, total);
    }

    @Override
    public EmployeeResponseDTO createEmployee(CreateEmployeeRequestDTO request) {
        // Convert DTO to Entity
        Employee employee = new Employee();
        employee.setFirstName(request.firstName());
        employee.setLastName(request.lastName());
        employee.setEmail(request.email());

        // Validate department exists and set it
        Department department = departmentService.getDepartmentByName(request.departmentName());
        employee.setDepartment(department);

        // Check for duplicate email
        Optional<Employee> existingEmployee = employeeMapper.findByEmail(employee.getEmail());
        if (existingEmployee.isPresent()) {
            throw new RuntimeException("Employee with email already exists: " + employee.getEmail());
        }

        // Save employee
        employeeMapper.insert(employee);

        return toResponseDTO(employee);
    }

    @Override
    public EmployeeResponseDTO getEmployeeById(int id) {
        Employee employee = employeeMapper.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
        return toResponseDTO(employee);
    }

    @Override
    public EmployeeResponseDTO updateEmployee(int id, CreateEmployeeRequestDTO request) {
        // Find existing employee
        Employee existingEmployee = employeeMapper.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));

        // Check if email is being changed to an existing one
        if (!existingEmployee.getEmail().equals(request.email())) {
            Optional<Employee> employeeWithEmail = employeeMapper.findByEmail(request.email());
            if (employeeWithEmail.isPresent()) {
                throw new RuntimeException("Employee with email already exists: " + request.email());
            }
        }

        // Update employee fields
        existingEmployee.setFirstName(request.firstName());
        existingEmployee.setLastName(request.lastName());
        existingEmployee.setEmail(request.email());

        // Update department if changed
        Department department = departmentService.getDepartmentByName(request.departmentName());
        existingEmployee.setDepartment(department);

        // Save updates
        employeeMapper.update(existingEmployee);

        return toResponseDTO(existingEmployee);
    }

    @Override
    public void deleteEmployee(int id) {
        // Check if employee exists
        Employee employee = employeeMapper.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
        employeeMapper.deleteById(id);
    }

    @Override
    public Page<EmployeeResponseDTO> getEmployeesByDepartment(Long departmentId, Pageable pageable) {
        // Ensure department exists
        departmentService.getDepartmentById(departmentId);

        List<Employee> employees = employeeMapper.findByDepartmentId(departmentId);
        int total = employeeMapper.countByDepartmentId(departmentId);

        // Convert entities to DTOs
        List<EmployeeResponseDTO> employeeDTOs = employees.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(employeeDTOs, pageable, total);
    }

    private EmployeeResponseDTO toResponseDTO(Employee employee) {
        DepartmentResponseDTO departmentDTO = employee.getDepartment() != null
                ? new DepartmentResponseDTO(employee.getDepartment())
                : null;

        return new EmployeeResponseDTO(
                employee.getFirstName(),
                employee.getLastName(),  // Added lastName based on your DTO
                employee.getEmail(),
                employee.getId(),
                departmentDTO
        );
    }
}