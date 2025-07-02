package com.cotech.systemcoreapi.service;

import com.cotech.systemcoreapi.dto.SalaryDto.SalaryRequest;
import com.cotech.systemcoreapi.dto.SalaryDto.SalaryRespond;
import com.cotech.systemcoreapi.model.Salary;

import java.util.List;

public interface ISalaryService {
    List<SalaryRequest> getAllSalaries();
    SalaryRespond createSalary(SalaryRespond salaryRespond);
    void deleteSalary(Long salaryId);
}
