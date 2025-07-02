package com.cotech.systemcoreapi.dto.ProjectDto;

import java.time.LocalDate;
import java.util.List;

public record ProjectRequest(
        Long id,
        String projectName,
        String projectDescription,
        List<String> profilePicture,
        LocalDate startDate,
        LocalDate endDate
) {
}
