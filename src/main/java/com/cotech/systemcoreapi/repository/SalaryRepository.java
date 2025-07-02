package com.cotech.systemcoreapi.repository;

import com.cotech.systemcoreapi.model.Salary;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SalaryRepository extends JpaRepository<Salary, Long> {
    Optional<Salary> findByBasicSalary(Double basicSalary);
}
