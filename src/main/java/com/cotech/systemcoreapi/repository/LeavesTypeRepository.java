package com.cotech.systemcoreapi.repository;

import com.cotech.systemcoreapi.model.Employee;
import com.cotech.systemcoreapi.model.Leaves;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LeavesTypeRepository extends JpaRepository<Leaves, Long> {
    long countByEmployeeAndStartDateBetween(Employee employee, LocalDate startDate, LocalDate endDate);
    List<Leaves> findByEmployeeAndEndDateBefore(Employee employee, LocalDate currentDate);
}
