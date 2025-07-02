package com.cotech.systemcoreapi.dto.RoleDto;

import java.util.List;

public record RoleRequest(
        String roleName,
        List<String> admins
) {
}
