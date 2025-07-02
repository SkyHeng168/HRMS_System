package com.cotech.systemcoreapi.dto.LeavesDto;


import java.time.LocalDate;
import java.time.LocalDateTime;

public record LeavesRespond(
        Long id,
        LocalDate startDate,
        LocalDate endDate,
        String reason,
        String leavesType,
        String leavesStatus,
        Long employee,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
