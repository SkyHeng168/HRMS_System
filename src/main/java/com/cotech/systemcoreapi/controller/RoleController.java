package com.cotech.systemcoreapi.controller;

import com.cotech.systemcoreapi.dto.RoleDto.RoleRequest;
import com.cotech.systemcoreapi.dto.RoleDto.RoleResponse;
import com.cotech.systemcoreapi.exception.CustomException.NotFoundException;
import com.cotech.systemcoreapi.service.impl.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/version1/role")
public class RoleController {
    private final RoleService roleService;

    //get request mapping using to get mapping and showing to admin to chose when create the admin.
    @GetMapping
    public ResponseEntity<List<RoleRequest>> getAllRoles() {
        List<RoleRequest> roles = roleService.getAllRoles();
        if (roles.isEmpty()) {
            throw new NotFoundException("No roles found");
        }
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleRequest> getRole(
            @PathVariable Long id
    ) {
        RoleRequest roleId = roleService.getRoleById(id);
        return ResponseEntity.ok(roleId);
    }

    //create request mapping using to create role for role tale.
    @PostMapping()
    public ResponseEntity<RoleResponse> insertRole(
            @RequestBody RoleResponse roleResponse
    ){
        RoleResponse InsertRole = roleService.createRole(roleResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(InsertRole);
    }

    //delete mapping use to delete role from database if role was wrong or not use anymore.
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRole(
            @PathVariable Long id
    ) {
        try{
            roleService.deleteRole(id);
            return ResponseEntity.status(HttpStatus.OK).body("Role deleted Successfully!");
        }catch (NotFoundException roleNotFoundException){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(roleNotFoundException.getMessage());
        }
    }

    //put request mapping use to update the role of admin to access to system
    @PutMapping("/{id}")
    public ResponseEntity<RoleResponse> updateRole(
            @RequestBody RoleResponse roleResponse,
            @PathVariable Long id
    ){
        RoleResponse UpdateRole = roleService.updateRole(roleResponse, id);
        return ResponseEntity.status(HttpStatus.OK).body(UpdateRole);
    }
}
