package com.cotech.systemcoreapi.service.impl;

import com.cotech.systemcoreapi.dto.RoleDto.RoleRequest;
import com.cotech.systemcoreapi.dto.RoleDto.RoleResponse;
import com.cotech.systemcoreapi.exception.CustomException.NotFoundException;
import com.cotech.systemcoreapi.model.Admin;
import com.cotech.systemcoreapi.model.Role;
import com.cotech.systemcoreapi.repository.RoleRepository;
import com.cotech.systemcoreapi.service.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService {
    private final RoleRepository roleRepository;

    @Override
    public RoleResponse createRole(RoleResponse roleResponse) {
        Role exitingRole = roleRepository.findByRoleName(roleResponse.roleName());
        if (exitingRole != null) {
            throw new NotFoundException("Role with name " + exitingRole.getRoleName() + " already exists");
        }
        Role createdRole = Role.builder()
                .roleName(roleResponse.roleName())
                .createdAt(LocalDateTime.now())
                .build();
        Role createdRoleSaved = roleRepository.save(createdRole);
        return new RoleResponse(
                createdRoleSaved.getRoleName(),
                createdRoleSaved.getCreatedAt(),
                createdRoleSaved.getUpdatedAt()
        );
    }

    @Override
    public RoleResponse updateRole(RoleResponse roleResponse, Long id) {
        Role exitingRole = roleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Role with id " + id + " not found"));

        exitingRole.setRoleName(roleResponse.roleName());
        exitingRole.setUpdatedAt(LocalDateTime.now());

        Role updatedRole = roleRepository.save(exitingRole);

        return new RoleResponse(
                updatedRole.getRoleName(),
                updatedRole.getCreatedAt(),
                updatedRole.getUpdatedAt()
        );
    }

    @Override
    public List<RoleRequest> getAllRoles() {
        List<Role> roles = roleRepository.findAll();

        return roles.stream()
                .map(role -> {
                    // Fetch the list of admins associated with this role
                    List<String> admins = role.getAdmins().stream()
                            .map(Admin::getUsername)  // Assuming Admin has a getUsername() method
                            .collect(Collectors.toList());

                    // Map to RoleRequest including the list of admin usernames
                    return new RoleRequest(
                            role.getRoleName(),
                            admins
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    public RoleRequest getRoleById(Long id) {
        Optional<Role> roleOptional = roleRepository.findById(id);
        if (roleOptional.isPresent()) {
            Role role = roleOptional.get();
            List<String> admins = role.getAdmins().stream()
                    .map(Admin::getUsername)
                    .collect(Collectors.toList());
            return new RoleRequest(
                    role.getRoleName(),
                    admins
            );
        }else {
            throw new NotFoundException("Role with id " + id + " not found");
        }
    }

    //delete Service use to delete role by id.
    @Override
    public void deleteRole(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Role with id " + id + " not found"));
        roleRepository.delete(role);
    }
}
