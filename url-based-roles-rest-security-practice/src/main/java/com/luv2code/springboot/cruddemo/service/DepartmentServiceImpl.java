package com.luv2code.springboot.cruddemo.service;

import java.util.DuplicateFormatFlagsException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.luv2code.springboot.cruddemo.exceptionhandling.EmployeeNotFoundException;
import com.luv2code.springboot.cruddemo.dao.DepartmentRepository;
import com.luv2code.springboot.cruddemo.entity.Department;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public Department createDepartment(Department department) {
        if (departmentRepository.existsByName(department.getName())) {
            throw new DuplicateFormatFlagsException("Department with name " + department.getName() + " already exists.");
        }
        return departmentRepository.save(department);
    }

    @Override
    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Department not found with id " + id));
    }

    @Override
    public Department getDepartmentByName(String name) {
        return departmentRepository.findByName(name)
                .orElseThrow(() -> new EmployeeNotFoundException("Department not found with name: " + name));
    }

    @Override
    public Page<Department> getAllDepartments(Pageable pageable) {
        return departmentRepository.findAll(pageable);
    }

    @Override
    public Department updateDepartment(Long id, Department departmentDetails) {
        Department department = getDepartmentById(id); 
        department.setName(departmentDetails.getName());
        return departmentRepository.save(department);
    }

    @Override
    public void deleteDepartment(Long id) {
        Department department = getDepartmentById(id);

        if(!department.getEmployees().isEmpty()){
            throw new RuntimeException("Cannot delete department with existing employees. Reassign employees first.");
        }
        departmentRepository.delete(department);
    }

}
