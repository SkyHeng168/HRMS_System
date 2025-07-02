package com.cotech.systemcoreapi.dto.DepartmentDto;

import java.time.LocalDateTime;

public record DepartmentRespond (
        Long id,
        String departmentName,
        LocalDateTime createAt,
        LocalDateTime updateAt
){
}
