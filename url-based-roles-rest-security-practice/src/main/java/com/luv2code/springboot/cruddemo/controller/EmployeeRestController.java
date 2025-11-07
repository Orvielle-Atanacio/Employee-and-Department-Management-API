package com.luv2code.springboot.cruddemo.controller;

import com.github.pagehelper.PageInfo;
import com.luv2code.springboot.cruddemo.dto.CreateEmployeeRequestDTO;
import com.luv2code.springboot.cruddemo.dto.DepartmentResponseDTO;
import com.luv2code.springboot.cruddemo.dto.EmployeeResponseDTO;
import com.luv2code.springboot.cruddemo.entity.Employee;
import com.luv2code.springboot.cruddemo.exceptionhandling.EmployeeNotFoundException;
import com.luv2code.springboot.cruddemo.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Employee Management", description = "CRUD operations for employees")
@RestController
@RequestMapping("/api")
public class EmployeeRestController {

    private final EmployeeService employeeService;

    public EmployeeRestController(EmployeeService theEmployeeService) {
        employeeService = theEmployeeService;
    }

    @GetMapping("/employees")
    @Operation(summary = "Get all employees with corresponding department details")
    public ResponseEntity<PageInfo<EmployeeResponseDTO>> getAllEmployees(
            @Valid @RequestParam(defaultValue = "1") int page,  // Changed from 0 to 1
            @Valid @RequestParam(defaultValue = "10") int size,
            @Valid @RequestParam(defaultValue = "id,asc") String[] sort) {

        // Convert sort parameters to PageHelper format
        String sortField = sort[0];
        String sortDirection = sort.length > 1 ? sort[1] : "asc";
        String orderBy = convertToOrderBy(sortField, sortDirection);

        // Use PageHelper with converted orderBy
        PageInfo<EmployeeResponseDTO> employeePage = employeeService.getAllEmployees(page, size, orderBy);

        return ResponseEntity.ok(employeePage);
    }

    @GetMapping("/departments/{departmentId}/employees")
    @Operation(summary = "Get employees by department ID")
    public ResponseEntity<PageInfo<EmployeeResponseDTO>> getEmployeesByDepartment(
            @PathVariable Long departmentId,
            @Valid @RequestParam(defaultValue = "1") int page,  // Changed from 0 to 1
            @Valid @RequestParam(defaultValue = "10") int size,
            @Valid @RequestParam(defaultValue = "id,asc") String[] sort) {

        // Convert sort parameters to PageHelper format
        String sortField = sort[0];
        String sortDirection = sort.length > 1 ? sort[1] : "asc";
        String orderBy = convertToOrderBy(sortField, sortDirection);

        // Use PageHelper with converted orderBy
        PageInfo<EmployeeResponseDTO> employeePage =
                employeeService.getEmployeesByDepartment(departmentId, page, size, orderBy);

        return ResponseEntity.ok(employeePage);
    }

    @GetMapping("/employees/{employeeId}")
    @Operation(summary = "Get employee details based on employee ID")
    public ResponseEntity<EmployeeResponseDTO> getEmployee(@PathVariable int employeeId) {
        EmployeeResponseDTO employee = employeeService.getEmployeeById(employeeId);
        return ResponseEntity.ok(employee);
    }

    @PostMapping("/employees")
    @Operation(summary = "Create new employee (id is autoincremented)")
    public ResponseEntity<EmployeeResponseDTO> createEmployee(
            @Valid @RequestBody CreateEmployeeRequestDTO theEmployee) {
        EmployeeResponseDTO response = employeeService.createEmployee(theEmployee);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/employees/{id}")
    @Operation(summary = "Update an existing employee")
    public ResponseEntity<EmployeeResponseDTO> updateEmployee(
            @PathVariable int id,
            @Valid @RequestBody CreateEmployeeRequestDTO dto) {
        EmployeeResponseDTO response = employeeService.updateEmployee(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/employees/{employeeId}")
    @Operation(summary = "Delete employee based on employee ID")
    public ResponseEntity<String> deleteEmployee(@PathVariable int employeeId) {
        employeeService.deleteEmployee(employeeId);
        return ResponseEntity.ok("Deleted employee id - " + employeeId);
    }

    @GetMapping("/employees/search")
    @Operation(summary = "Search employee by email")
    public ResponseEntity<EmployeeResponseDTO> getEmployeeByEmail(@RequestParam String email) {
        // This method uses the service layer properly
        Optional<Employee> employeeOpt = employeeService.findByEmail(email);
        Employee employee = employeeOpt.orElseThrow(() ->
                new EmployeeNotFoundException("Employee not found with email: " + email));

        DepartmentResponseDTO departmentDTO = employee.getDepartment() != null
                ? new DepartmentResponseDTO(employee.getDepartment()) : null;

        EmployeeResponseDTO responseDTO = new EmployeeResponseDTO(
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getId(),
                departmentDTO
        );

        return ResponseEntity.ok(responseDTO);
    }

    // Helper method to convert Spring Data sort to SQL ORDER BY clause
    private String convertToOrderBy(String sortField, String sortDirection) {
        // Map entity field names to database column names if needed
        String columnName = mapFieldToColumn(sortField);
        return columnName + " " + sortDirection.toUpperCase();
    }

    // Helper method to map entity field names to database column names
    private String mapFieldToColumn(String fieldName) {
        // Add mappings for your specific database column names
        switch (fieldName.toLowerCase()) {
            case "id":
                return "id";
            case "firstname":
                return "first_name";
            case "lastname":
                return "last_name";
            case "email":
                return "email";
            case "department":
                return "department_id";
            default:
                return fieldName; // Use as-is if no mapping needed
        }
    }
}