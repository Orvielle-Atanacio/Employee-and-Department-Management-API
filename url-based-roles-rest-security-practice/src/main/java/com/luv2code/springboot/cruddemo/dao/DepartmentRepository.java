package com.luv2code.springboot.cruddemo.dao;

import com.luv2code.springboot.cruddemo.entity.Department;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;



public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Optional<Department> findByName(String name);

    Boolean existsByName(String name);
}
