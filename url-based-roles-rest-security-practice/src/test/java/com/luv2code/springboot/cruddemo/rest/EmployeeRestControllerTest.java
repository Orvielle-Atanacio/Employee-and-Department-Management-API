package com.luv2code.springboot.cruddemo.rest;

import com.luv2code.springboot.cruddemo.dto.CreateEmployeeRequestDTO;
import com.luv2code.springboot.cruddemo.dto.EmployeeResponseDTO;
import com.luv2code.springboot.cruddemo.entity.Department;
import com.luv2code.springboot.cruddemo.entity.Employee;
import com.luv2code.springboot.cruddemo.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeRestController.class)
public class EmployeeRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;
    private Employee employee;
    private EmployeeResponseDTO employeeResponseDTO;
    private CreateEmployeeRequestDTO createEmployeeRequestDTO;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setId(1);
        employee.setFirstName("John");
        employee.setEmail("john@test.com");

        employeeResponseDTO = new EmployeeResponseDTO(
                employee.getFirstName(),
                employee.getEmail(),
                employee.getId(), null);

        createEmployeeRequestDTO = new CreateEmployeeRequestDTO("John", "Doe", "john@test.com", "HR");
    }

    @Test
    @WithMockUser
    void testCreateEmployee() throws Exception {
        when(employeeService.createUser(any(CreateEmployeeRequestDTO.class))).thenReturn(employeeResponseDTO);

        String requestBody = """
                    {
                      "firstName": "John",
                      "lastName": "Doe",
                      "email": "john@example.com",
                      "departmentName": "HR"
                    }
                """;

        mockMvc.perform(post("/api/employees")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("John"));

        verify(employeeService).createUser(any(CreateEmployeeRequestDTO.class));
    }

    @Test
    @WithMockUser
    void testDeleteEmployee() throws Exception {
        doNothing().when(employeeService).deleteById(1);

        mockMvc.perform(delete("/api/employees/1")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted employee id - 1"));

        verify(employeeService).deleteById(1);
    }

    @Test
    @WithMockUser
    void testGetAllEmployees() throws Exception {
        Page<Employee> employeePage = new PageImpl<>(List.of(employee));
        when(employeeService.getAllEmployees(any())).thenReturn(employeePage);

        mockMvc.perform(get("/api/employees")
                .param("page", "0")
                .param("size", "10")
                .param("sort", "id, asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].firstName").value("John"));

        verify(employeeService, times(1)).getAllEmployees(any());
    }

    @Test
    @WithMockUser
    void testGetEmployee() throws Exception {
        when(employeeService.findById(1)).thenReturn(employee);

        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"));

        verify(employeeService).findById(1);
    }

    @Test
    @WithMockUser
    void testUpdateEmployee() throws Exception {

        when(employeeService.findById(1)).thenReturn(employee);


        Employee updatedEmployee = new Employee();
        updatedEmployee.setId(1);
        updatedEmployee.setFirstName("Jane");
        updatedEmployee.setLastName("Smith");
        updatedEmployee.setEmail("jane@example.com");


        Department department = new Department();
        department.setId(1);
        department.setName("HR");
        updatedEmployee.setDepartment(department);

        when(employeeService.save(any(Employee.class), eq("HR"))).thenReturn(updatedEmployee);

        String requestBody = """
                    {
                      "firstName": "Jane",
                      "lastName": "Smith",
                      "email": "jane@example.com",
                      "departmentName": "HR"
                    }
                """;

        mockMvc.perform(put("/api/employees/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Jane"))
                .andExpect(jsonPath("$.email").value("jane@example.com"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.department.id").value(1)) // Check nested department
                .andExpect(jsonPath("$.department.name").value("HR")); // Check nested department

        verify(employeeService).findById(1);
        verify(employeeService).save(any(Employee.class), eq("HR"));
    }
}
