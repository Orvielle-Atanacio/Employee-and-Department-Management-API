package com.luv2code.springboot.cruddemo.mapper;

import com.luv2code.springboot.cruddemo.entity.Employee;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper {

    List<Employee> findAll();

    Optional<Employee> findById(int id);

    Optional<Employee> findByEmail(String email);

    void insert(Employee employee);

    void update(Employee employee);

    void deleteById(int id);

    List<Employee> findByDepartmentId(Long departmentId);

    int countAll();

    int countByDepartmentId(Long departmentId);

}