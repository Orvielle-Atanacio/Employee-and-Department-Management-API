package com.luv2code.springboot.cruddemo.rest;

import com.luv2code.springboot.cruddemo.dto.DepartmentRequestDTO;
import com.luv2code.springboot.cruddemo.dto.DepartmentResponseDTO;
import com.luv2code.springboot.cruddemo.entity.Department;
import com.luv2code.springboot.cruddemo.service.DepartmentService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(DepartmentRestController.class)
public class DepartmentRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DepartmentService departmentService;

    private Department dept;

    @BeforeEach
    void setUp() {
        dept = new Department();
        dept.setId(1L);
        dept.setName("HR");
    }

    @Test
    @WithMockUser(roles = { "USER" })
    void testCreateDepartment() throws Exception {
        when(departmentService.createDepartment(any(Department.class))).thenReturn(dept);

        String requestBody = """
                {
                    "name": "Engineering"
                }
                """;

        mockMvc.perform(post("/api/departments")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("HR"));
    }

    @Test
    @WithMockUser(roles = { "ADMIN" })
    void testDeleteDepartment() throws Exception {
        doNothing().when(departmentService).deleteDepartment(1L);

        mockMvc.perform(delete("/api/departments/1")
                .with(csrf()))
                .andExpect(status().isNoContent());

        verify(departmentService).deleteDepartment(1L);
    }

    @Test
    @WithMockUser
    void testGetAllDepartments() throws Exception {

        when(departmentService.getAllDepartments(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(dept)));

        mockMvc.perform(get("/api/departments")
                .param("page", "0")
                .param("size", "10")
                .param("sort", "id,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("HR"));

        verify(departmentService, times(1)).getAllDepartments(any(Pageable.class));
    }

    @Test
    @WithMockUser
    void testGetDepartmentById() throws Exception {
        when(departmentService.getDepartmentById(1L)).thenReturn(dept);
        mockMvc.perform(get("/api/departments/id/1")).andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("HR"));
    }

    @Test
    @WithMockUser
    void testGetDepartmentByName() throws Exception {
        when(departmentService.getDepartmentByName("HR")).thenReturn(dept);

        mockMvc.perform(get("/api/departments/name/HR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("HR"))
                .andExpect(jsonPath("$.id").value("1"));

        verify(departmentService, times(1)).getDepartmentByName("HR");
    }

    @Test
    void testUpdateDepartment() {
        when(departmentService.updateDepartment(eq(1L), any(Department.class)))
                .thenReturn(dept);
    }
}
