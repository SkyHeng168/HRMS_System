package com.cotech.systemcoreapi.dto.EmployeeDto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record EmployeeRequest (
        Long id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        LocalDate dateOfJoining,
        String gender,
        String address,
        String position,
        String department,
        Double salary,
        String profilePicture,
        LocalDateTime createAt,
        LocalDateTime updateAt
){
}
