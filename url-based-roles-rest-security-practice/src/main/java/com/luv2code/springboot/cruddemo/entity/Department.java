package com.luv2code.springboot.cruddemo.entity;

import java.util.ArrayList;
import java.util.List;

public class Department {
    private long id;
    private String name;
    private List<Employee> employees = new ArrayList<>();

    // No-argument constructor
    public Department() {}

    public Department(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Department(long id, String name, List<Employee> employees) {
        this.id = id;
        this.name = name;
        this.employees = employees;
    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public void addEmployee(Employee employee) {
        if (employees == null) {
            employees = new ArrayList<>();
        }
        employees.add(employee);
        // Use the setter method, not direct field access
        if (employee != null) {
            employee.setDepartment(this);
        }
    }
}