package com.cotech.systemcoreapi.service.impl;

import com.cotech.systemcoreapi.dto.SalaryDto.SalaryRequest;
import com.cotech.systemcoreapi.dto.SalaryDto.SalaryRespond;
import com.cotech.systemcoreapi.exception.CustomException.NotFoundException;
import com.cotech.systemcoreapi.model.Salary;
import com.cotech.systemcoreapi.repository.SalaryRepository;
import com.cotech.systemcoreapi.service.ISalaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalaryService implements ISalaryService {
    private final SalaryRepository salaryRepository;

    @Override
    public List<SalaryRequest> getAllSalaries() {
        List<Salary> salaries = salaryRepository.findAll();
        return salaries.stream().map(salary -> new SalaryRequest(
                salary.getId(),
                salary.getBasicSalary(),
                salary.getAllowance(),
                salary.getDeductions(),
                salary.getNetSalary(),
                salary.getPaymentDate(),
                (salary.getEmployees() != null && !salary.getEmployees().isEmpty()) ?
                        salary.getEmployees().stream()
                                .map(employee -> employee.getFirstName() + " " + employee.getLastName()) // Assuming we want the employee name
                                .distinct()
                                .collect(Collectors.toList()) : Collections.emptyList(), // If no employees, return empty list
                salary.getCreateAt(),
                salary.getUpdateAt()
        )).collect(Collectors.toList());
    }

    @Override
    public SalaryRespond createSalary(SalaryRespond salaryRespond) {
        Optional<Salary> existingSalary = salaryRepository.findByBasicSalary(salaryRespond.basicSalary());
        Salary salary;
        if (existingSalary.isPresent()) {
            salary = existingSalary.get();
        } else {
            salary = Salary.builder()
                    .basicSalary(salaryRespond.basicSalary())
                    .allowance(salaryRespond.allowance())
                    .deductions(salaryRespond.deductions())
                    .netSalary(salaryRespond.netSalary())
                    .paymentDate(salaryRespond.paymentDate())
                    .createAt(LocalDateTime.now())
                    .build();
            salaryRepository.save(salary);
        }
        return new SalaryRespond(
                salary.getId(),
                salary.getBasicSalary(),
                salary.getAllowance(),
                salary.getDeductions(),
                salary.getNetSalary(),
                salary.getPaymentDate(),
                salary.getCreateAt(),
                salary.getUpdateAt()
        );
    }

    @Override
    public void deleteSalary(Long salaryId) {
        Salary salary = salaryRepository.findById(salaryId)
                .orElseThrow(()-> new NotFoundException("Salary Not Found: " + salaryId));
        salaryRepository.delete(salary);
    }
}
