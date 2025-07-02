package com.cotech.systemcoreapi.dto.AdminDto;

import com.cotech.systemcoreapi.model.Role;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record AdminResponse(
        String firstname,
        String lastname,
        String email,
        String phoneNumber,
        String username,
        String password,
        LocalDate dateOfBirthday,
        Long role,
        LocalDateTime createAt,
        LocalDateTime updateAt
){
}
