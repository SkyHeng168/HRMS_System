package com.cotech.systemcoreapi.controller;

import com.cotech.systemcoreapi.dto.AttendanceDto.AttendanceRequest;
import com.cotech.systemcoreapi.exception.CustomException.NotFoundException;
import com.cotech.systemcoreapi.service.impl.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/version1/public/attendance")
public class AttendedController {
    private final AttendanceService attendanceService;

    @GetMapping
    public ResponseEntity<List<AttendanceRequest>> getAttendance() {
        List<AttendanceRequest> attendance = attendanceService.getAttendance();
        return ResponseEntity.ok(attendance);
    }

    @GetMapping("/{date}")
    public ResponseEntity<?> getAttendanceByDate(@PathVariable String date) {
        try {
            LocalDate localDate = LocalDate.parse(date);
            List<AttendanceRequest> attendanceRequests = attendanceService.getAttendanceByDay(localDate);
            return ResponseEntity.ok(attendanceRequests);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Invalid date format. Please use YYYY-MM-DD.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/checkin-out/{employeeId}")
    public ResponseEntity<String> checkInAndOut(@PathVariable Long employeeId) {
        try {
            attendanceService.CheckInAndOutAttendance(employeeId);
            return ResponseEntity.ok("Check-in/out successful for employee ID: " + employeeId);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing request: " + e.getMessage());
        }
    }
}
