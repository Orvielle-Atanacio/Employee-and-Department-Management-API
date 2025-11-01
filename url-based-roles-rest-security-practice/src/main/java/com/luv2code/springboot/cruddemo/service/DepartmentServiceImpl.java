package com.luv2code.springboot.cruddemo.service;


import com.luv2code.springboot.cruddemo.dto.DepartmentRequestDTO;
import com.luv2code.springboot.cruddemo.dto.DepartmentResponseDTO;
import com.luv2code.springboot.cruddemo.entity.Department;
import com.luv2code.springboot.cruddemo.exceptionhandling.DepartmentNotFoundException;
import com.luv2code.springboot.cruddemo.mapper.DepartmentMapper;
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
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentMapper departmentMapper;

    public DepartmentServiceImpl(DepartmentMapper departmentMapper) {
        this.departmentMapper = departmentMapper;
    }

    @Override
    public DepartmentResponseDTO createDepartment(DepartmentRequestDTO request) {
        // Check if department with same name already exists
        Optional<Department> existingDepartment = departmentMapper.findByName(request.name());
        if (existingDepartment.isPresent()) {
            throw new RuntimeException("Department with name '" + request.name() + "' already exists");
        }

        // Create new department entity
        Department department = new Department();
        department.setName(request.name());

        // Insert department
        departmentMapper.insert(department);

        // Return the created department as DTO
        return convertToDepartmentResponseDTO(department);
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentResponseDTO getDepartmentById(Long id) {
        Department department = departmentMapper.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found with id: " + id));

        return convertToDepartmentResponseDTO(department);
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentResponseDTO getDepartmentByName(String name) {
        Department department = departmentMapper.findByName(name)
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found with name: " + name));

        return convertToDepartmentResponseDTO(department);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DepartmentResponseDTO> getAllDepartments(Pageable pageable) {
        // Get all departments from the database
        List<Department> allDepartments = departmentMapper.findAll();

        // Apply pagination manually since MyBatis doesn't natively support Spring Data Pageable
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), allDepartments.size());

        if (start > allDepartments.size()) {
            return new PageImpl<>(List.of(), pageable, allDepartments.size());
        }

        List<DepartmentResponseDTO> departmentDTOs = allDepartments.subList(start, end)
                .stream()
                .map(this::convertToDepartmentResponseDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(departmentDTOs, pageable, allDepartments.size());
    }

    @Override
    public DepartmentResponseDTO updateDepartment(Long id, DepartmentRequestDTO request) {
        // Find existing department
        Department existingDepartment = departmentMapper.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found with id: " + id));

        // Check if name is being changed and if it conflicts with another department
        if (!existingDepartment.getName().equals(request.name())) {
            Optional<Department> departmentWithName = departmentMapper.findByName(request.name());
            if (departmentWithName.isPresent() && departmentWithName.get().getId() != id) {
                throw new RuntimeException("Department name '" + request.name() + "' is already in use by another department");
            }
        }

        // Update department fields
        existingDepartment.setName(request.name());

        // Update department in database
        departmentMapper.update(existingDepartment);

        // Return updated department as DTO
        return convertToDepartmentResponseDTO(existingDepartment);
    }

    @Override
    public void deleteDepartment(Long id) {
        // Check if department exists
        Department department = departmentMapper.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found with id: " + id));

        // Check if department has employees (optional business logic)
        // You might want to prevent deletion if department has employees
        // or handle cascading deletion based on your business requirements

        departmentMapper.deleteById(id);
    }

    // Helper method to convert Department entity to DepartmentResponseDTO
    private DepartmentResponseDTO convertToDepartmentResponseDTO(Department department) {
        return new DepartmentResponseDTO(department);
    }

    // Additional helper methods that might be useful

    /**
     * Get department by ID with all employees (eager loading)
     */
    @Transactional(readOnly = true)
    public DepartmentResponseDTO getDepartmentByIdWithEmployees(Long id) {
        Department department = departmentMapper.findByIdWithEmployees(id)
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found with id: " + id));

        return convertToDepartmentResponseDTO(department);
    }

    /**
     * Check if department exists by ID
     */
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return departmentMapper.findById(id).isPresent();
    }

    /**
     * Check if department exists by name
     */
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return departmentMapper.findByName(name).isPresent();
    }

    /**
     * Get total count of departments
     */
    @Transactional(readOnly = true)
    public int getDepartmentCount() {
        return departmentMapper.countAll();
    }
}