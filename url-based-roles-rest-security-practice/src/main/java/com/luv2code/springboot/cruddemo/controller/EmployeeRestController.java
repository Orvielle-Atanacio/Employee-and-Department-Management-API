package com.luv2code.springboot.cruddemo.controller;


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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public ResponseEntity<Page<EmployeeResponseDTO>> getAllEmployees(
            @Valid @RequestParam(defaultValue = "0") int page,
            @Valid @RequestParam(defaultValue = "10") int size,
            @Valid @RequestParam(defaultValue = "id,asc") String[] sort) {

        String sortField = sort[0];
        String sortDirection = sort.length > 1 ? sort[1] : "asc";
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        Page<EmployeeResponseDTO> employeePage = employeeService.getAllEmployees(pageable);

        return ResponseEntity.ok(employeePage);
    }

    @GetMapping("/departments/{departmentId}/employees")
    @Operation(summary = "Get employees by department ID")
    public ResponseEntity<Page<EmployeeResponseDTO>> getEmployeesByDepartment(
            @PathVariable Long departmentId,
            @Valid @RequestParam(defaultValue = "0") int page,
            @Valid @RequestParam(defaultValue = "10") int size,
            @Valid @RequestParam(defaultValue = "id,asc") String[] sort) {

        String sortField = sort[0];
        String sortDirection = sort.length > 1 ? sort[1] : "asc";
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        Page<EmployeeResponseDTO> employeePage = employeeService.getEmployeesByDepartment(departmentId, pageable);

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
}