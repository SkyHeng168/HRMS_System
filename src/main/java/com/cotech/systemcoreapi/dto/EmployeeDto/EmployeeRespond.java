package com.cotech.systemcoreapi.dto.EmployeeDto;

import java.time.LocalDateTime;

public record EmployeeRespond (
        Long id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        LocalDateTime dateOfJoining,
        String gender,
        String address,
        Long position,
        Long department,
        Long salary,
        String profilePicture,
        LocalDateTime createAt,
        LocalDateTime updateAt
){
}
