package com.luv2code.springboot.cruddemo.service;

import com.github.pagehelper.PageInfo;
import com.luv2code.springboot.cruddemo.dto.DepartmentRequestDTO;
import com.luv2code.springboot.cruddemo.dto.DepartmentResponseDTO;


public interface DepartmentService {

    DepartmentResponseDTO createDepartment(DepartmentRequestDTO request);

    DepartmentResponseDTO getDepartmentById(Long id);

    DepartmentResponseDTO getDepartmentByName(String name);

    PageInfo<DepartmentResponseDTO> getAllDepartments(int pageNum, int pageSize, String orderBy);

    DepartmentResponseDTO updateDepartment(Long id, DepartmentRequestDTO request);

    void deleteDepartment(Long id);

}
