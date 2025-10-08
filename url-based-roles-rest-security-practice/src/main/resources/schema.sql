-- Drop tables in correct order (due to foreign keys)
DROP TABLE IF EXISTS employee CASCADE;
DROP TABLE IF EXISTS roles CASCADE;
DROP TABLE IF EXISTS members CASCADE;
DROP TABLE IF EXISTS departments CASCADE;

-- Create tables in correct order
CREATE TABLE departments (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE members (
    user_id VARCHAR(50) PRIMARY KEY,
    pw VARCHAR(255) NOT NULL,
    active BOOLEAN NOT NULL
);

CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    role VARCHAR(50) NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES members(user_id) ON DELETE CASCADE
);

CREATE TABLE employee (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    department_id INT NOT NULL,
    CONSTRAINT fk_department FOREIGN KEY (department_id) REFERENCES departments(id) ON DELETE CASCADE
);