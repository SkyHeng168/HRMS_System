package com.cotech.systemcoreapi.controller;

import com.cotech.systemcoreapi.dto.SalaryDto.SalaryRequest;
import com.cotech.systemcoreapi.dto.SalaryDto.SalaryRespond;
import com.cotech.systemcoreapi.exception.CustomException.NotFoundException;
import com.cotech.systemcoreapi.service.impl.SalaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/version1/salary")
public class SalaryController {
    private final SalaryService salaryService;

    @GetMapping
    public ResponseEntity<List<SalaryRequest>> getAllSalaries() {
        List<SalaryRequest> salaries = salaryService.getAllSalaries();
        if (salaries.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(salaries, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<SalaryRespond> createSalary(@RequestBody SalaryRespond salaryRespond) {
        try {
            SalaryRespond createdSalary = salaryService.createSalary(salaryRespond);
            return ResponseEntity.ok(createdSalary);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSalary(
            @PathVariable Long id
    ) {
        try{
            salaryService.deleteSalary(id);
            return ResponseEntity.status(HttpStatus.OK).body("Deleted salary successfully");
        }catch (NotFoundException notFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundException.getMessage());
        }
    }
}
