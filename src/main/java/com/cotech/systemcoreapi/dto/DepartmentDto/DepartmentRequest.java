package com.cotech.systemcoreapi.dto.DepartmentDto;

import com.cotech.systemcoreapi.model.Position;

import java.time.LocalDateTime;
import java.util.List;

public record DepartmentRequest (
        Long id,
        String departmentName,
        List<String> positions,
        LocalDateTime createAt,
        LocalDateTime updateAt
){
}
