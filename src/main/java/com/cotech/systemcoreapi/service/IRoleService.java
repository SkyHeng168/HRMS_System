package com.cotech.systemcoreapi.service;

import com.cotech.systemcoreapi.dto.RoleDto.RoleRequest;
import com.cotech.systemcoreapi.dto.RoleDto.RoleResponse;

import java.util.List;

public interface IRoleService {
    RoleResponse createRole(RoleResponse roleResponse);
    RoleResponse updateRole(RoleResponse roleResponse, Long id);
    List<RoleRequest> getAllRoles();
    RoleRequest getRoleById(Long id);
    void deleteRole(Long id);
}
