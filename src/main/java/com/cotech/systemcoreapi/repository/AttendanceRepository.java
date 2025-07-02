package com.cotech.systemcoreapi.repository;

import com.cotech.systemcoreapi.model.Attendance;
import com.cotech.systemcoreapi.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    Optional<Attendance> findByDateAndEmployee(LocalDate date, Employee employee);
    List<Attendance> findByDate(LocalDate date);
}

