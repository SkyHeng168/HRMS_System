package com.cotech.systemcoreapi.dto.ProjectDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record ProjectRespond(
        Long id,
        String projectName,
        String projectDescription,
        LocalDate startDate,
        LocalDate endDate,
        List<Long> employeeId,
        LocalDateTime createAt,
        LocalDateTime updateAt
) {
}
