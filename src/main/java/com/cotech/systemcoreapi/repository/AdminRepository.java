package com.cotech.systemcoreapi.repository;

import com.cotech.systemcoreapi.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByUsernameOrEmail(String username, String email);
    Admin findByUsername(String username);
}
