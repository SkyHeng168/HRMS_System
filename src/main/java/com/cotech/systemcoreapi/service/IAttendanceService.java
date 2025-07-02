package com.cotech.systemcoreapi.service;

import com.cotech.systemcoreapi.dto.AttendanceDto.AttendanceRequest;
import com.cotech.systemcoreapi.model.Attendance;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IAttendanceService {
    List<AttendanceRequest> getAttendance();
    List<AttendanceRequest> getAttendanceByDay(LocalDate date);
    void CheckInAndOutAttendance(Long id);
}
