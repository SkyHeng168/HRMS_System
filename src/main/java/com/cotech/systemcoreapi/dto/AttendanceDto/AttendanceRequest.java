package com.cotech.systemcoreapi.dto.AttendanceDto;

import java.time.LocalDate;
import java.time.LocalTime;

public record AttendanceRequest(
        Long id,
        LocalDate date,
        LocalTime checkInTime,
        LocalTime checkOutTime,
        String employeeName // Change from List<String> to String
) {}


