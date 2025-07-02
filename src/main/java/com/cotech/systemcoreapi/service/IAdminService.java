package com.cotech.systemcoreapi.service;

import com.cotech.systemcoreapi.dto.AdminDto.AdminRequest;
import com.cotech.systemcoreapi.dto.AdminDto.AdminResponse;

import java.util.List;

public interface IAdminService {
    List<AdminResponse> getAllAdmin();
    AdminResponse addAdmin(AdminResponse adminResponse);
    AdminResponse updateAdmin(AdminResponse adminResponse, Long id);
    void deleteAdmin(Long id);
    AdminRequest getAdminById(Long id);
}
