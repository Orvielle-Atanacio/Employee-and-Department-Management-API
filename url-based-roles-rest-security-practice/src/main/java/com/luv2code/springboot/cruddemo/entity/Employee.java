package com.luv2code.springboot.cruddemo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

// Marks this class as a JPA entity, representing a table in the database.
@Entity
// Specifies the name of the database table this entity maps to.
@Table(name = "employee")
public class Employee {

    // Marks this field as the primary key of the table.
    @Id
    // Configures how the primary key is generated (auto-increment in most
    // databases).
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Maps this field to the 'id' column in the table.
    @Column(name = "id")
    private int id;

    // Validation: Ensures this field is not null before saving to the database.
    @NotNull(message = "First name is required")
    // Maps this field to the 'first_name' column in the table.
    @Column(name = "first_name")
    private String firstName;

    @NotNull(message = "Last name is required")
    @Column(name = "last_name")
    private String lastName;

    // Validation: Ensures the field is not null and contains a valid email format.
    @NotNull(message = "Email Address is required")
    @Email(message = "Must be a valid email")
    @Column(name = "email")
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    // Default constructor required by JPA for entity creation.
    public Employee() {
    }

    // Convenience constructor for creating new Employee objects without an ID
    // (which will be generated).
    public Employee(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    // --- Standard Getters and Setters ---
    // Allow other parts of the application to access and modify the private fields.
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
}