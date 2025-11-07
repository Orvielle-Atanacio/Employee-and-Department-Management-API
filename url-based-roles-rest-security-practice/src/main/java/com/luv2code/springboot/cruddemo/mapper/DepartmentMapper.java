package com.luv2code.springboot.cruddemo.mapper;

import com.luv2code.springboot.cruddemo.entity.Department;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DepartmentMapper {
    List<Department> findAll();

    Optional<Department> findByIdWithEmployees(long id);

    Optional<Department> findById(long id);

    Optional<Department> findByName(String name);

    void insert(Department department);

    void update(Department department);

    void deleteById(long id);

    int countAll();
}