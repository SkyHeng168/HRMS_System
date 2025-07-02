package com.cotech.systemcoreapi.controller;

import com.cotech.systemcoreapi.dto.DepartmentDto.DepartmentRequest;
import com.cotech.systemcoreapi.dto.DepartmentDto.DepartmentRespond;
import com.cotech.systemcoreapi.exception.CustomException.NotFoundException;
import com.cotech.systemcoreapi.service.impl.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/version1/department")
public class DepartmentController {
    private final DepartmentService departmentService;

    @GetMapping
    public ResponseEntity<List<DepartmentRequest>> getAllDepartment() {
        List<DepartmentRequest> departments = departmentService.getAllDepartments();
        if (departments.isEmpty()) {
            throw new NotFoundException("No departments found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(departments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentRequest> getDepartmentById(@PathVariable Long id) {
        DepartmentRequest departmentRequest = departmentService.getDepartment(id);
        return ResponseEntity.status(HttpStatus.OK).body(departmentRequest);
    }

    @PostMapping
    public ResponseEntity<DepartmentRespond> createDepartment(
            @RequestBody DepartmentRespond departmentRespond
    ){
        DepartmentRespond createdDepartment = departmentService.createDepartment(departmentRespond);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDepartment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentRespond> updateDepartment(
            @PathVariable Long id,
            @RequestBody DepartmentRespond departmentRespond) {
        DepartmentRespond updatedDepartment = departmentService.updateDepartment(departmentRespond, id);
        return new ResponseEntity<>(updatedDepartment, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDepartment(
            @PathVariable Long id
    ){
        try {
            departmentService.deleteDepartment(id);
            return ResponseEntity.status(HttpStatus.OK).body("Department deleted successfully");
        }catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Department not found");
        }
    }
}
