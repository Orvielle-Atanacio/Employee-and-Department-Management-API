package com.luv2code.springboot.cruddemo.service;

import com.luv2code.springboot.cruddemo.dto.CreateEmployeeRequestDTO;
import com.luv2code.springboot.cruddemo.dto.DepartmentResponseDTO;
import com.luv2code.springboot.cruddemo.dto.EmployeeResponseDTO;
import com.luv2code.springboot.cruddemo.entity.Department;
import com.luv2code.springboot.cruddemo.entity.Employee;
import com.luv2code.springboot.cruddemo.exceptionhandling.EmployeeNotFoundException;
import com.luv2code.springboot.cruddemo.mapper.DepartmentMapper;
import com.luv2code.springboot.cruddemo.mapper.EmployeeMapper;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeMapper employeeMapper;
    private final DepartmentMapper departmentMapper;

    public EmployeeServiceImpl(EmployeeMapper employeeMapper, DepartmentMapper departmentMapper) {
        this.employeeMapper = employeeMapper;
        this.departmentMapper = departmentMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeResponseDTO> getAllEmployees(Pageable pageable) {
        // Get all employees from the database
        List<Employee> allEmployees = employeeMapper.findAll();

        // Apply pagination manually since MyBatis doesn't natively support Spring Data Pageable
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), allEmployees.size());

        if (start > allEmployees.size()) {
            return new PageImpl<>(List.of(), pageable, allEmployees.size());
        }

        List<EmployeeResponseDTO> employeeDTOs = allEmployees.subList(start, end)
                .stream()
                .map(this::convertToEmployeeResponseDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(employeeDTOs, pageable, allEmployees.size());
    }

    @Override
    public EmployeeResponseDTO createEmployee(CreateEmployeeRequestDTO request) {
        // Check if email already exists
        Optional<Employee> existingEmployee = employeeMapper.findByEmail(request.email());
        if (existingEmployee.isPresent()) {
            throw new RuntimeException("Employee with email " + request.email() + " already exists");
        }

        // Create new employee entity
        Employee employee = new Employee();
        employee.setFirstName(request.firstName());
        employee.setLastName(request.lastName());
        employee.setEmail(request.email());

        // Handle department assignment if provided
        if (request.departmentName() != null && !request.departmentName().trim().isEmpty()) {
            Department department = findOrCreateDepartment(request.departmentName());
            employee.setDepartment(department);
        }

        // Insert employee
        employeeMapper.insert(employee);

        // Return the created employee as DTO
        return convertToEmployeeResponseDTO(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeResponseDTO getEmployeeById(int id) {
        Employee employee = employeeMapper.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));

        return convertToEmployeeResponseDTO(employee);
    }

    @Override
    public EmployeeResponseDTO updateEmployee(int id, CreateEmployeeRequestDTO request) {
        // Find existing employee
        Employee existingEmployee = employeeMapper.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));

        // Check if email is being changed and if it conflicts with another employee
        if (!existingEmployee.getEmail().equals(request.email())) {
            Optional<Employee> employeeWithEmail = employeeMapper.findByEmail(request.email());
            if (employeeWithEmail.isPresent() && employeeWithEmail.get().getId() != id) {
                throw new RuntimeException("Email " + request.email() + " is already in use by another employee");
            }
        }

        // Update employee fields
        existingEmployee.setFirstName(request.firstName());
        existingEmployee.setLastName(request.lastName());
        existingEmployee.setEmail(request.email());

        // Handle department assignment
        if (request.departmentName() != null && !request.departmentName().trim().isEmpty()) {
            Department department = findOrCreateDepartment(request.departmentName());
            existingEmployee.setDepartment(department);
        } else {
            existingEmployee.setDepartment(null);
        }

        // Update employee in database
        employeeMapper.update(existingEmployee);

        // Return updated employee as DTO
        return convertToEmployeeResponseDTO(existingEmployee);
    }

    @Override
    public void deleteEmployee(int id) {
        // Check if employee exists
        if (!employeeMapper.findById(id).isPresent()) {
            throw new EmployeeNotFoundException("Employee not found with id: " + id);
        }

        employeeMapper.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeResponseDTO> getEmployeesByDepartment(Long departmentId, Pageable pageable) {
        // Get employees by department ID
        List<Employee> departmentEmployees = employeeMapper.findByDepartmentId(departmentId);

        // Apply pagination
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), departmentEmployees.size());

        if (start > departmentEmployees.size()) {
            return new PageImpl<>(List.of(), pageable, departmentEmployees.size());
        }

        List<EmployeeResponseDTO> employeeDTOs = departmentEmployees.subList(start, end)
                .stream()
                .map(this::convertToEmployeeResponseDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(employeeDTOs, pageable, departmentEmployees.size());
    }

    // Helper method to convert Employee entity to EmployeeResponseDTO
    private EmployeeResponseDTO convertToEmployeeResponseDTO(Employee employee) {
        DepartmentResponseDTO departmentDTO = null;
        if (employee.getDepartment() != null) {
            departmentDTO = new DepartmentResponseDTO(employee.getDepartment());
        }

        return new EmployeeResponseDTO(
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getId(),
                departmentDTO
        );
    }

    // Helper method to find or create department
    private Department findOrCreateDepartment(String departmentName) {
        // Try to find existing department by name
        Optional<Department> existingDepartment = departmentMapper.findByName(departmentName);

        if (existingDepartment.isPresent()) {
            return existingDepartment.get();
        } else {
            // Create new department if it doesn't exist
            Department newDepartment = new Department();
            newDepartment.setName(departmentName);
            departmentMapper.insert(newDepartment);
            return newDepartment;
        }
    }

    // Additional methods that your controller uses but aren't in the interface
    @Transactional(readOnly = true)
    public Employee findById(int id) {
        return employeeMapper.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));
    }

    public void deleteById(int id) {
        deleteEmployee(id);
    }

    public EmployeeResponseDTO createUser(CreateEmployeeRequestDTO request) {
        return createEmployee(request);
    }

    public Employee save(Employee employee, String departmentName) {
        if (employee.getId() == 0) {
            // New employee
            if (departmentName != null && !departmentName.trim().isEmpty()) {
                Department department = findOrCreateDepartment(departmentName);
                employee.setDepartment(department);
            }
            employeeMapper.insert(employee);
        } else {
            // Existing employee - update
            if (departmentName != null && !departmentName.trim().isEmpty()) {
                Department department = findOrCreateDepartment(departmentName);
                employee.setDepartment(department);
            } else {
                employee.setDepartment(null);
            }
            employeeMapper.update(employee);
        }
        return employee;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Employee> findByEmail(String email) {
        return employeeMapper.findByEmail(email);
    }
}