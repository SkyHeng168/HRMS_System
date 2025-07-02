package com.cotech.systemcoreapi.repository;

import com.cotech.systemcoreapi.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByPosition_PositionName(String positionName);
    List<Employee> findByDepartment_DepartmentName(String departmentName);
    Employee findByEmail(String email);
    List<Employee> findByPositionNull();
    long count();
}
