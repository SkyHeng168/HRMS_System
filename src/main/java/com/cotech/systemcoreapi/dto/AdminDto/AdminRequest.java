package com.cotech.systemcoreapi.dto.AdminDto;

import java.time.LocalDate;

public record AdminRequest(
    String firstname,
    String lastname,
    String email,
    String phoneNumber,
    String username,
    String password,
    LocalDate dateOfBirthday,
    String roleName
) {
}
