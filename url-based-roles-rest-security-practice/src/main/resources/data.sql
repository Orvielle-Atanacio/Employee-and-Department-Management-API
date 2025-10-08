-- Clear existing data in CORRECT ORDER (due to foreign keys)
DELETE FROM employee;
DELETE FROM roles;
DELETE FROM members;
DELETE FROM departments;

-- Insert departments FIRST (they're referenced by employees)
INSERT INTO departments (name) VALUES
('Engineering'),
('Human Resources'),
('Marketing'),
('Sales'),
('Finance');

-- Then insert members
INSERT INTO members (user_id, pw, active) VALUES
('employee', '$2a$10$1R5PZ9anssfc16swxv91cunyv7.aJhbkNdrf78QfRlFLEAm2OgccG', true),
('manager',  '$2a$10$1R5PZ9anssfc16swxv91cunyv7.aJhbkNdrf78QfRlFLEAm2OgccG', true),
('admin',    '$2a$10$1R5PZ9anssfc16swxv91cunyv7.aJhbkNdrf78QfRlFLEAm2OgccG', true);

-- Then insert roles
INSERT INTO roles (user_id, role) VALUES
('employee', 'ROLE_EMPLOYEE'),
('manager',  'ROLE_MANAGER'),
('manager',  'ROLE_EMPLOYEE'),
('admin',    'ROLE_ADMIN'),
('admin',    'ROLE_EMPLOYEE');

-- Finally insert employees (they reference departments)
INSERT INTO employee (first_name, last_name, email, department_id) VALUES
('John',    'Doe',     'john.doe@company.com', 1),
('Jane',    'Smith',   'jane.smith@company.com', 1),
('Maria',   'Santos',  'maria.santos@company.com', 2),
('Michael', 'Chen',    'michael.chen@company.com', 3),
('Sarah',   'Johnson', 'sarah.johnson@company.com', 4),
('David',   'Brown',   'david.brown@company.com', 5);