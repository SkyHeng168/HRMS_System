package com.cotech.systemcoreapi.service.impl;

import com.cotech.systemcoreapi.dto.AdminDto.AdminRequest;
import com.cotech.systemcoreapi.dto.AdminDto.AdminResponse;
import com.cotech.systemcoreapi.exception.CustomException.AdminAlreadyExistsException;
import com.cotech.systemcoreapi.exception.CustomException.NotFoundException;
import com.cotech.systemcoreapi.model.Admin;
import com.cotech.systemcoreapi.model.Role;
import com.cotech.systemcoreapi.repository.AdminRepository;
import com.cotech.systemcoreapi.repository.RoleRepository;
import com.cotech.systemcoreapi.service.IAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService implements IAdminService {
    private final AdminRepository adminRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    //Get All data of admin by using Response dto
    @Override
    public List<AdminResponse> getAllAdmin() {
        // Fetch all admins from the repository
        List<Admin> admins = adminRepository.findAll();
        return admins.stream()
                .map(admin -> new AdminResponse(
                        admin.getFirstname(),
                        admin.getLastname(),
                        admin.getEmail(),
                        admin.getPhoneNumber(),
                        admin.getUsername(),
                        admin.getPassword(),
                        admin.getDateOfBirthday(),
                        admin.getRole() != null ? admin.getRole().getId() : null, // Use role ID here
                        admin.getCreatedAt(),
                        admin.getUpdatedAt()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public AdminRequest getAdminById(Long id) {
        Optional<Admin> adminOptional = adminRepository.findById(id);

        if (adminOptional.isPresent()) {
            Admin admin = adminOptional.get();
            String roleName = admin.getRole() != null ? admin.getRole().getRoleName() : null;
            return new AdminRequest(
                    admin.getFirstname(),
                    admin.getLastname(),
                    admin.getEmail(),
                    admin.getPhoneNumber(),
                    admin.getUsername(),
                    admin.getPassword(),
                    admin.getDateOfBirthday(),
                    roleName
            );
        } else {
            throw new NotFoundException("Admin with ID " + id + " not found");
        }
    }

    // insert the admin with bcrypt the passwords
    @Override
    public AdminResponse addAdmin(AdminResponse adminResponse) {
        // Check if an Admin with the same username or email already exists
        Optional<Admin> existingAdmin = adminRepository.findByUsernameOrEmail(
                adminResponse.username(), adminResponse.email()
        );

        if (existingAdmin.isPresent()) {
            throw new AdminAlreadyExistsException("Admin with this username or email already exists.");
        }

        // Find the Role entity based on the roleId provided in the AdminResponse
        Role role = roleRepository.findById(adminResponse.role()) // Fetch Role by ID
                .orElseThrow(() -> new NotFoundException("Role not found with id: " + adminResponse.role()));
        String password = passwordEncoder.encode(adminResponse.password());
        // Create and save the new Admin
        Admin createAdmin = Admin.builder()
                .firstname(adminResponse.firstname())
                .lastname(adminResponse.lastname())
                .email(adminResponse.email())
                .phoneNumber(adminResponse.phoneNumber())
                .username(adminResponse.username())
                .dateOfBirthday(adminResponse.dateOfBirthday())
                .password(password)
                .role(role) // Set the Role entity here
                .createdAt(LocalDateTime.now())
                .build();

        Admin createdAdmin = adminRepository.save(createAdmin);

        // Return the AdminResponse with the roleId
        return new AdminResponse(
                createdAdmin.getFirstname(),
                createdAdmin.getLastname(),
                createdAdmin.getEmail(),
                createdAdmin.getPhoneNumber(),
                createdAdmin.getUsername(),
                createdAdmin.getPassword(),
                createdAdmin.getDateOfBirthday(),
                createdAdmin.getRole().getId(), // Use the roleId from the Role entity
                createdAdmin.getCreatedAt(),
                createdAdmin.getUpdatedAt()
        );
    }

    //update the admin by id. after update the update will insert auto and create not insert
    @Override
    public AdminResponse updateAdmin(AdminResponse adminResponse, Long id) {
        // Fetch the existing admin by ID
        Admin existingAdmin = adminRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Admin not found with id " + id));

        // Find the Role entity based on the role ID from AdminResponse
        Role role = roleRepository.findById(adminResponse.role()) // Assuming adminResponse.role() returns the roleId
                .orElseThrow(() -> new NotFoundException("Role not found with id: " + adminResponse.role()));

        // Update the Admin fields, including the Role
        existingAdmin.setFirstname(adminResponse.firstname());
        existingAdmin.setLastname(adminResponse.lastname());
        existingAdmin.setEmail(adminResponse.email());
        existingAdmin.setPhoneNumber(adminResponse.phoneNumber());
        existingAdmin.setUsername(adminResponse.username());
        existingAdmin.setPassword(adminResponse.password());
        existingAdmin.setRole(role); // Set the Role entity here
        existingAdmin.setUpdatedAt(LocalDateTime.now());

        // Save the updated Admin
        Admin updatedAdmin = adminRepository.save(existingAdmin);

        // Return the updated AdminResponse with the roleId
        return new AdminResponse(
                updatedAdmin.getFirstname(),
                updatedAdmin.getLastname(),
                updatedAdmin.getEmail(),
                updatedAdmin.getPhoneNumber(),
                updatedAdmin.getUsername(),
                updatedAdmin.getPassword(),
                updatedAdmin.getDateOfBirthday(),
                updatedAdmin.getRole().getId(), // Use roleId instead of roleName
                updatedAdmin.getCreatedAt(),
                updatedAdmin.getUpdatedAt()
        );
    }

    // delete admin by Id. ex: if id = 1. remove admin
    @Override
    public void deleteAdmin(Long id) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Admin not found with id " + id));
        if (admin == null) {
            // Handle the case where the admin is not found (e.g., return a 404 response)
            throw new NotFoundException("Admin not found with id " + id);
        }
        adminRepository.delete(admin);
    }

    public Admin findByUsernameAndPassword(String username, String password) {
        Admin admin = adminRepository.findByUsername(username);

        if (admin != null && passwordEncoder.matches(password, admin.getPassword())) {
            return admin;
        }
        return null;
    }

    public boolean verifyAdminCredentials(String username, String password) {
        Admin admin = adminRepository.findByUsername(username);
        return admin != null && passwordEncoder.matches(password, admin.getPassword());
    }
}