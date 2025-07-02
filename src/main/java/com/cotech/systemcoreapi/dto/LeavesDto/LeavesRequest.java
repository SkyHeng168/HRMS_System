package com.cotech.systemcoreapi.dto.LeavesDto;

import java.time.LocalDate;

public record LeavesRequest(
        Long id,
        LocalDate startDate,
        LocalDate endDate,
        String reason,
        String leavesType,
        String leavesStatus,
        String employee
) {
}
