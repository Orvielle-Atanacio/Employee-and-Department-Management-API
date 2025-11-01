package com.luv2code.springboot.cruddemo.controller;


import com.luv2code.springboot.cruddemo.dto.DepartmentRequestDTO;
import com.luv2code.springboot.cruddemo.dto.DepartmentResponseDTO;
import com.luv2code.springboot.cruddemo.service.DepartmentService;
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


@Tag(name = "Department Management", description = "CRUD operations for departments")
@RestController
@RequestMapping("/api/departments")
public class DepartmentRestController {

    private final DepartmentService departmentService;

    public DepartmentRestController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping
    @Operation(summary = "Create a new department")
    public ResponseEntity<DepartmentResponseDTO> createDepartment(@Valid @RequestBody DepartmentRequestDTO request) {
        DepartmentResponseDTO response = departmentService.createDepartment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get department by ID")
    public ResponseEntity<DepartmentResponseDTO> getDepartmentById(@PathVariable Long id) {
        DepartmentResponseDTO response = departmentService.getDepartmentById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "Get department by name")
    public ResponseEntity<DepartmentResponseDTO> getDepartmentByName(@PathVariable String name) {
        DepartmentResponseDTO response = departmentService.getDepartmentByName(name);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get all departments with pagination and sorting")
    public ResponseEntity<Page<DepartmentResponseDTO>> getAllDepartments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort) {

        String sortField = sort[0];
        String sortDirection = sort.length > 1 ? sort[1] : "asc";
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        Page<DepartmentResponseDTO> departments = departmentService.getAllDepartments(pageable);

        return ResponseEntity.ok(departments);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing department")
    public ResponseEntity<DepartmentResponseDTO> updateDepartment(
            @PathVariable Long id,
            @Valid @RequestBody DepartmentRequestDTO request) {
        DepartmentResponseDTO response = departmentService.updateDepartment(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a department by ID")
    public ResponseEntity<String> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.ok("Deleted department with id: " + id);
    }
}