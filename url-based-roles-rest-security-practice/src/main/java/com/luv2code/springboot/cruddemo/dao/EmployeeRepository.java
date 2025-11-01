package com.luv2code.springboot.cruddemo.dao;

import com.luv2code.springboot.cruddemo.entity.Employee;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

// This interface is a Spring Data JPA Repository.
// It handles all database interactions for the Employee entity.
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    // By extending JpaRepository, this interface automatically gets
    // CRUD (Create, Read, Update, Delete) methods like:
    // - save(S entity) : Saves an employee
    // - findById(ID id) : Finds an employee by their ID
    // - findAll() : Returns all employees
    // - deleteById(ID id) : Deletes an employee by ID
    // - count() : Returns the total number of employees
    // The types are: Employee (the Entity) and Integer (the type of its Primary Key, id).

    // You can add custom database query methods here later if needed.
    // For example:
    // List<Employee> findByLastName(String lastName);
    // Spring Data JPA will automatically implement it based on the method name.

    Optional<Employee> findByEmail(String email);

    Page<Employee> findByDepartmentId(Long departmentId, Pageable pageable);

}
