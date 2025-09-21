package com.luv2code.springboot.cruddemo.rest;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.luv2code.springboot.cruddemo.dto.DepartmentRequestDTO;
import com.luv2code.springboot.cruddemo.dto.DepartmentResponseDTO;
import com.luv2code.springboot.cruddemo.entity.Department;

import com.luv2code.springboot.cruddemo.service.DepartmentService;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Department Management", description = "CRUD operations for departments")
@RestController
@RequestMapping("/api/departments")
public class DepartmentRestController {

    @Autowired
    private DepartmentService departmentService;

    // Get all departments
    @GetMapping
    @Operation(summary = "Get all department with pagination")
    public ResponseEntity<Page<DepartmentResponseDTO>> getAllDepartments(
            @Valid @RequestParam(defaultValue = "0") int page,
            @Valid @RequestParam(defaultValue = "10") int size,
            @Valid @RequestParam(defaultValue = "id,asc") String[] sort) {

        // Parse the sort parameter into field and direction.
        String sortField = sort[0];
        String sortDirection = sort[1];
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        // Create a PageRequest object which encapsulates pagination and sorting info.
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        // Fetch the page of Department entities from the service.
        Page<Department> departmentPage = departmentService.getAllDepartments(pageable);

        // Map the Page of Entities to a Page of DTOs to control the exposed data.
        Page<DepartmentResponseDTO> dtoPage = departmentPage
                .map(DepartmentResponseDTO::new);

        // Return the page of DTOs with an HTTP 200 OK status.
        return ResponseEntity.ok(dtoPage);
    }

    // GET department by id
    @GetMapping("/id/{id}")
    @Operation(summary = "Get department by id")
    public ResponseEntity<DepartmentResponseDTO> getDepartmentById(@PathVariable Long id) {
        Department department = departmentService.getDepartmentById(id);
        DepartmentResponseDTO responseDTO = new DepartmentResponseDTO(department);
        return ResponseEntity.ok(responseDTO);
    }

    // GET department by name
    @Operation(summary = "Get department by name")
    @GetMapping("/name/{name}")
    public ResponseEntity<DepartmentResponseDTO> getDepartmentByName(@PathVariable String name) {
        Department department = departmentService.getDepartmentByName(name);
        DepartmentResponseDTO responseDTO = new DepartmentResponseDTO(department);
        return ResponseEntity.ok(responseDTO);
    }

    // CREATE new department
    @PostMapping
    @Operation(summary = "Create a new department")
    @ApiResponse(responseCode = "201", description = "Department created")
    public ResponseEntity<DepartmentResponseDTO> createDepartment(
            @RequestBody @Valid DepartmentRequestDTO requestDTO) { // ← Now using correct annotation
        Department department = new Department();
        department.setName(requestDTO.name());
        Department saved = departmentService.createDepartment(department);
        return new ResponseEntity<>(new DepartmentResponseDTO(saved), HttpStatus.CREATED);
    }

    // UPDATE existing department
    @PutMapping("/{id}")
    @Operation(summary = "Update existing department")
    public ResponseEntity<DepartmentResponseDTO> updateDepartment(@PathVariable Long id,
            @RequestBody @Valid DepartmentRequestDTO requestDTO) {

        // Create department entity from DTO
        Department departmentDetails = new Department();
        departmentDetails.setName(requestDTO.name());

        // Update department
        Department updateDepartment = departmentService.updateDepartment(id, departmentDetails);
        DepartmentResponseDTO responseDTO = new DepartmentResponseDTO(updateDepartment);

        return ResponseEntity.ok(responseDTO);
    }

    // DELETE department
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a department without employee under it")
    public ResponseEntity<DepartmentResponseDTO> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }
}
