package com.cotech.systemcoreapi.service.impl;

import com.cotech.systemcoreapi.dto.AttendanceDto.AttendanceRequest;
import com.cotech.systemcoreapi.exception.CustomException.NotFoundException;
import com.cotech.systemcoreapi.model.Attendance;
import com.cotech.systemcoreapi.model.Employee;
import com.cotech.systemcoreapi.repository.AttendanceRepository;
import com.cotech.systemcoreapi.repository.EmployeeRepository;
import com.cotech.systemcoreapi.service.IAttendanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttendanceService implements IAttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public List<AttendanceRequest> getAttendance() {
        List<Attendance> attendanceList = attendanceRepository.findAll();

        return attendanceList.stream().map(attendance -> new AttendanceRequest(
                attendance.getId(),
                attendance.getDate(),
                attendance.getCheckInTime(),
                attendance.getCheckOutTime(),
                attendance.getEmployee() != null ?
                        attendance.getEmployee().getFirstName() + " " + attendance.getEmployee().getLastName()
                        : "No Employee Assigned"
        )).collect(Collectors.toList());
    }

    @Override
    public List<AttendanceRequest> getAttendanceByDay(LocalDate date) {
        List<Attendance> attendanceList = attendanceRepository.findByDate(date);

        return attendanceList.stream()
                .filter(attendance -> attendance.getCheckInTime() != null) // Ensure check-in is recorded
                .map(attendance -> new AttendanceRequest(
                        attendance.getId(),
                        attendance.getDate(),
                        attendance.getCheckInTime(),
                        attendance.getCheckOutTime(),
                        attendance.getEmployee() != null ?
                                attendance.getEmployee().getFirstName() + " " + attendance.getEmployee().getLastName()
                                : "No Employee Assigned"
                ))
                .collect(Collectors.toList());
    }

    @Override
    public void CheckInAndOutAttendance(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Employee not found with ID: " + id));

        LocalDate today = LocalDate.now(ZoneId.of("Asia/Bangkok"));

        Optional<Attendance> optionalAttendance = attendanceRepository.findByDateAndEmployee(today, employee);

        if (optionalAttendance.isEmpty()) {
            Attendance attendance = new Attendance();
            attendance.setDate(today);
            attendance.setEmployee(employee);
            attendance.setCheckInTime(LocalTime.now(ZoneId.of("Asia/Bangkok")));

            attendanceRepository.save(attendance);
            log.info("Employee ID {} checked in successfully at {}", id, attendance.getCheckInTime());
        } else {
            Attendance attendance = optionalAttendance.get();
            if (attendance.getCheckOutTime() == null) {
                attendance.setCheckOutTime(LocalTime.now(ZoneId.of("Asia/Bangkok")));
                attendanceRepository.save(attendance);
                log.info("Employee ID {} checked out successfully at {}", id, attendance.getCheckOutTime());
            } else {
                throw new IllegalStateException("Employee ID " + id + " has already checked in and checked out today. Please come back tomorrow.");
            }
        }
    }
}
