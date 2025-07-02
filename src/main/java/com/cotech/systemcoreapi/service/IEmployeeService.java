package com.cotech.systemcoreapi.service;

import com.cotech.systemcoreapi.dto.EmployeeDto.EmployeeRequest;
import com.cotech.systemcoreapi.dto.EmployeeDto.EmployeeRespond;
import com.cotech.systemcoreapi.model.Employee;

import java.util.List;

public interface IEmployeeService {
    List<EmployeeRequest> getAllEmployees();
    EmployeeRequest getEmployeeById(Long id);
    EmployeeRespond createEmployee(EmployeeRespond employeeRespond);
    EmployeeRespond updateEmployee(EmployeeRespond employeeRespond, Long id);
    List<Employee> getAllEmployeesByDepartment(String department);
    long countEmployees();
    void deleteEmployeeFromDepartmentAndPosition(Long employeeId, Long departmentId, Long positionId);
}
