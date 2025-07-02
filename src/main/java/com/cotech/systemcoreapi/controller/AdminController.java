package com.cotech.systemcoreapi.controller;

import com.cotech.systemcoreapi.dto.AdminDto.AdminRequest;
import com.cotech.systemcoreapi.dto.AdminDto.AdminResponse;
import com.cotech.systemcoreapi.dto.TokenRequest;
import com.cotech.systemcoreapi.exception.CustomException.NotFoundException;
import com.cotech.systemcoreapi.model.Admin;
import com.cotech.systemcoreapi.service.authService.jwt.JwtService;
import com.cotech.systemcoreapi.service.impl.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/version1/admin")
public class AdminController {
    private final AdminService adminService;
    private final JwtService jwtService;

    //Get request use to request the information admin all the information
    @GetMapping
    public ResponseEntity<List<AdminResponse>> getAllAdmin() {
        List<AdminResponse> adminResponses = adminService.getAllAdmin();
        if (adminResponses.isEmpty()) {
            throw new NotFoundException("No admins found in the system");
        }
        return new ResponseEntity<>(adminResponses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminRequest> getAdminById(
            @PathVariable Long id
    ) {
        AdminRequest admin = adminService.getAdminById(id);
        return ResponseEntity.status(HttpStatus.OK).body(admin);
    }

    // Post request use to input admin
    @PostMapping
    public ResponseEntity<AdminResponse> insertAdmin(
            @RequestBody AdminResponse adminResponse
    ){
        AdminResponse InsertAdmin = adminService.addAdmin(adminResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(InsertAdmin);
    }

    //put request use to update the admin by using to update the admin
    @PutMapping("/{id}")
    public ResponseEntity<AdminResponse> updateAdmin(
            @RequestBody AdminResponse adminResponse,
            @PathVariable Long id
    ){
        AdminResponse UpdateAdmin = adminService.updateAdmin(adminResponse, id);
        return ResponseEntity.status(HttpStatus.OK).body(UpdateAdmin);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAdmin(
            @PathVariable Long id
    ){
        try{
            adminService.deleteAdmin(id);
            return ResponseEntity.status(HttpStatus.OK).body("Deleted Admin successfully...!");
        }catch (NotFoundException notFoundException){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin not found with : " + id + " " + notFoundException.getMessage());
        }
    }

    //---------------------------- Verify Level ----------------------------//
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Admin admin) {
        // Verify the admins credentials
        String username = admin.getUsername();  // Assuming Admin has a username field
        String password = admin.getPassword();  // Assuming Admin has a password field

        Admin authenticatedAdmin = adminService.findByUsernameAndPassword(username, password);

        if (authenticatedAdmin == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        // Verify credentials (you would need to implement this check in your adminService)
        boolean isValid = adminService.verifyAdminCredentials(username, password);
        if (!isValid) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        // Generate the access and refresh tokens
        String accessToken = jwtService.generateAccessToken(username);
        String refreshToken = jwtService.generateRefreshToken(username);

        TokenRequest tokenRequest = new TokenRequest(authenticatedAdmin.getId(), accessToken, refreshToken);
        return ResponseEntity.status(HttpStatus.OK).body(tokenRequest);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody TokenRequest tokenRequest) {
        String refreshToken = tokenRequest.getRefreshToken();

        if (refreshToken == null || refreshToken.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Refresh token is missing or empty");
        }


        String username = jwtService.extractUsername(refreshToken);

        if (username == null || !jwtService.validateRefreshToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }

        String newAccessToken = jwtService.generateAccessToken(username);

        return ResponseEntity.status(HttpStatus.OK).body(new TokenRequest(null, newAccessToken, refreshToken));
    }
}
