package com.cotech.systemcoreapi.dto.RoleDto;

import java.time.LocalDateTime;

public record RoleResponse (
        String roleName,
        LocalDateTime createAt,
        LocalDateTime updateAt
){
}
