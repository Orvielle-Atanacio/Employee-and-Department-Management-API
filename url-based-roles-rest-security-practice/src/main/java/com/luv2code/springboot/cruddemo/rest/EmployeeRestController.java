package com.luv2code.springboot.cruddemo.rest;

import com.luv2code.springboot.cruddemo.dto.CreateEmployeeRequestDTO;
import com.luv2code.springboot.cruddemo.dto.DepartmentResponseDTO;
import com.luv2code.springboot.cruddemo.dto.EmployeeResponseDTO;
import com.luv2code.springboot.cruddemo.entity.Employee;
import com.luv2code.springboot.cruddemo.exceptionhandling.EmployeeNotFoundException;
import com.luv2code.springboot.cruddemo.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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


// Marks this class as a REST controller whose methods return domain objects (not views).
// All endpoints in this class will be prefixed with "/api".
@Tag(name = "Employee Management", description = "CRUD operations for employees")
@RestController
@RequestMapping("/api")
public class EmployeeRestController {

    // Service layer dependency for handling business logic.
    private final EmployeeService employeeService;

    // Constructor-based dependency injection (best practice).
    public EmployeeRestController(EmployeeService theEmployeeService) {
        employeeService = theEmployeeService;
    }

    /**
     * GET endpoint to fetch a paginated and sorted list of all employees.
     * 
     * @param page The page number to retrieve (defaults to 0).
     * @param size The number of items per page (defaults to 10).
     * @param sort An array defining the sort field and direction (e.g.,
     *             ["firstName", "asc"]).
     * @return A ResponseEntity containing a Page of EmployeeResponseDTO objects.
     */

    @GetMapping("/employees")
    @Operation(summary = "Get all employees with corresponding department details")
    public ResponseEntity<Page<EmployeeResponseDTO>> getAllEmployees(
            @Valid @RequestParam(defaultValue = "0") int page,
            @Valid @RequestParam(defaultValue = "10") int size,
            @Valid @RequestParam(defaultValue = "id,asc") String[] sort) { // Changed default to avoid space issue

        // Parse the sort parameter into field and direction.
        String sortField = sort[0];
        String sortDirection = sort[1];
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;

        // Create a PageRequest object which encapsulates pagination and sorting info.
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        // Fetch the page of Employee entities from the service.
        Page<Employee> employeePage = employeeService.getAllEmployees(pageable);

        // Map the Page of Entities to a Page of DTOs to control the exposed data.
        Page<EmployeeResponseDTO> dtoPage = employeePage
                .map(emp -> new EmployeeResponseDTO(emp.getFirstName(), emp.getEmail(), emp.getId(),
                        emp.getDepartment() != null ? new DepartmentResponseDTO(emp.getDepartment()) : null));

        // Return the page of DTOs with an HTTP 200 OK status.
        return ResponseEntity.ok(dtoPage);
    }

    /**
     * DELETE endpoint to remove an employee by their ID.
     * 
     * @param employeeId The ID of the employee to delete.
     * @return A confirmation message.
     * @throws EmployeeNotFoundException if no employee with the given ID exists.
     */
    @DeleteMapping("/employees/{employeeId}")
    @Operation(summary = "Delete employee based on employee ID")
    public ResponseEntity<String> deleteEmployee(@PathVariable int employeeId) {
        // This call will throw EmployeeNotFoundException if not found.
        employeeService.deleteById(employeeId);
        return ResponseEntity.ok("Deleted employee id - " + employeeId);
    }

    /**
     * POST endpoint to create a new employee.
     * 
     * @param theEmployee The DTO containing data for the new employee.
     * @return A ResponseEntity with the created employee's data and HTTP 201
     *         status.
     */
    @PostMapping("/employees")
    @Operation(summary = "Create new employee (id is autoincremented)")
    public ResponseEntity<EmployeeResponseDTO> createEmployee(
            @Valid @RequestBody CreateEmployeeRequestDTO theEmployee) {
        // Delegate the creation logic to the service layer and get the response DTO.
        EmployeeResponseDTO response = employeeService.createUser(theEmployee);
        // Return the response with an HTTP 201 Created status.
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET endpoint to fetch a single employee by their ID.
     * 
     * @param employeeId The ID of the employee to retrieve.
     * @return A ResponseEntity containing the EmployeeResponseDTO.
     */
    @GetMapping("/employees/{employeeId}")
    @Operation(summary = "Get employee details based on employee ID)")
    public ResponseEntity<EmployeeResponseDTO> getEmployee(@PathVariable int employeeId) {
        // Fetch the employee entity
        Employee employee = employeeService.findById(employeeId); // This should return Employee entity

        // Create the DTO using the same pattern as your /employees endpoint
        EmployeeResponseDTO responseDTO = new EmployeeResponseDTO(
                employee.getFirstName(),
                employee.getEmail(),
                employee.getId(),
                employee.getDepartment() != null ? new DepartmentResponseDTO(employee.getDepartment()) : null);

        return ResponseEntity.ok(responseDTO);
    }

    /**
     * PUT endpoint to update an existing employee.
     * 
     * @param id  The ID of the employee to update.
     * @param dto The DTO containing the updated data.
     * @return A ResponseEntity with the updated employee's data.
     */
    @PutMapping("/employees/{id}")
    @Operation(summary = "Update an existing employee")
    public ResponseEntity<EmployeeResponseDTO> updateEmployee(@PathVariable int id,
            @Valid @RequestBody CreateEmployeeRequestDTO dto) { // Added @Valid for consistency
        // 1. Fetch the existing employee from the database (throws exception if not
        // found).
        Employee employee = employeeService.findById(id);
        // 2. Update the entity with new values from the DTO.
        employee.setFirstName(dto.firstName());
        employee.setLastName(dto.lastName());
        employee.setEmail(dto.email());

        // 3. Save the updated entity back to the database.
        Employee updatedEmployee = employeeService.save(employee, dto.departmentName());

        DepartmentResponseDTO departmentDTO = new DepartmentResponseDTO(
                updatedEmployee.getDepartment().getId(),
                updatedEmployee.getDepartment().getName());

        // 4. Convert the saved entity to a Response DTO and return it.
        EmployeeResponseDTO responseDTO = new EmployeeResponseDTO(updatedEmployee.getFirstName(),
                updatedEmployee.getEmail(), updatedEmployee.getId(), departmentDTO);
        return ResponseEntity.ok(responseDTO);
    }
}
