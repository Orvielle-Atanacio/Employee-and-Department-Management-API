package com.luv2code.springboot.cruddemo.service;


import com.luv2code.springboot.cruddemo.dto.DepartmentRequestDTO;
import com.luv2code.springboot.cruddemo.dto.DepartmentResponseDTO;
import com.luv2code.springboot.cruddemo.entity.Department;
import com.luv2code.springboot.cruddemo.exceptionhandling.EmployeeNotFoundException;
import com.luv2code.springboot.cruddemo.mapper.DepartmentMapper;
import com.luv2code.springboot.cruddemo.mapper.EmployeeMapper;
import java.util.DuplicateFormatFlagsException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    String deptNotFound = "Department not found with id ";


    private final DepartmentMapper departmentMapper;
    private final EmployeeMapper employeeMapper;

    public DepartmentServiceImpl(DepartmentMapper departmentMapper, EmployeeMapper employeeMapper) {
        this.departmentMapper = departmentMapper;
        this.employeeMapper = employeeMapper;
    }

    @Override
    public DepartmentResponseDTO createDepartment(DepartmentRequestDTO request) {
        // Check for duplicate department name
        if (departmentMapper.findByName(request.name()).isPresent()) {
            throw new DuplicateFormatFlagsException("Department with name " + request.name() + " already exists.");
        }

        // Convert DTO to Entity
        Department department = new Department();
        department.setName(request.name());

        // Use MyBatis insert
        departmentMapper.insert(department);

        // Convert back to DTO
        return new DepartmentResponseDTO(department);
    }

    @Override
    public DepartmentResponseDTO getDepartmentById(Long id) {
        Department department = departmentMapper.findByIdWithEmployees(id)
                .orElseThrow(() -> new EmployeeNotFoundException(deptNotFound + id));
        return new DepartmentResponseDTO(department);
    }

    @Override
    public DepartmentResponseDTO getDepartmentByName(String name) {
        Department department = departmentMapper.findByName(name)
                .orElseThrow(() -> new EmployeeNotFoundException(deptNotFound + name));
        return new DepartmentResponseDTO(department);
    }

    @Override
    public Page<DepartmentResponseDTO> getAllDepartments(Pageable pageable) {
        List<Department> departments = departmentMapper.findAll();
        int total = departmentMapper.countAll();

        // Convert entities to DTOs
        List<DepartmentResponseDTO> dtoList = departments.stream()
                .map(DepartmentResponseDTO::new)
                .collect(Collectors.toList());

        return new PageImpl<>(dtoList, pageable, total);
    }

    @Override
    public DepartmentResponseDTO updateDepartment(Long id, DepartmentRequestDTO request) {
        Department department = departmentMapper.findByIdWithEmployees(id)
                .orElseThrow(() -> new EmployeeNotFoundException(deptNotFound + id));

        department.setName(request.name());
        departmentMapper.update(department);

        return new DepartmentResponseDTO(department);
    }

    @Override
    public void deleteDepartment(Long id) {
        Department department = departmentMapper.findByIdWithEmployees(id)
                .orElseThrow(() -> new EmployeeNotFoundException(deptNotFound + id));

        if (!department.getEmployees().isEmpty()) {
            throw new RuntimeException("Cannot delete department with existing employees. Reassign employees first.");
        }
        departmentMapper.deleteById(id);
    }
}