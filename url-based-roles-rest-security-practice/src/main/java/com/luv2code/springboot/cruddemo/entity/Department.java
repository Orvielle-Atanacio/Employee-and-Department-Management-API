package com.luv2code.springboot.cruddemo.entity;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class Department {
    private long id;
    private String name;
    private List<Employee> employees = new ArrayList<>();

    public Department(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Department(long id, String name, List<Employee> employees) {
        this.id = id;
        this.name = name;
        this.employees = employees;
    }

    public void addEmployee(Employee employee) {
        if (employees == null) {
            employees = new ArrayList<>();
        }
        employees.add(employee);
        employee.setDepartment(this);
    }
}