package com.cotech.systemcoreapi.service;

import com.cotech.systemcoreapi.dto.DepartmentDto.DepartmentRequest;
import com.cotech.systemcoreapi.dto.DepartmentDto.DepartmentRespond;

import java.util.List;

public interface IDepartmentService {
    DepartmentRespond createDepartment(DepartmentRespond departmentRespond);
    List<DepartmentRequest> getAllDepartments();
    DepartmentRequest getDepartment(Long id);
    DepartmentRespond updateDepartment(DepartmentRespond departmentRespond, Long id);
    void deleteDepartment(Long id);
}
