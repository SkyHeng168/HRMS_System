package com.cotech.systemcoreapi.controller;

import com.cotech.systemcoreapi.dto.EmployeeDto.EmployeeRequest;
import com.cotech.systemcoreapi.dto.EmployeeDto.EmployeeRespond;
import com.cotech.systemcoreapi.exception.CustomException.ResourceNotFoundException;
import com.cotech.systemcoreapi.model.Employee;
import com.cotech.systemcoreapi.service.impl.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/version1/employee")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;
    public static String uploadDirectory = Paths.get("src", "main", "resources", "static").toAbsolutePath().toString();
    @GetMapping
    public ResponseEntity<List<EmployeeRequest>> getAllEmployees() {
        List<EmployeeRequest> employees = employeeService.getAllEmployees();
        return ResponseEntity.status(HttpStatus.OK).body(employees);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeRequest> getEmployeeById(
            @PathVariable Long id
    ) {
        EmployeeRequest employee = employeeService. getEmployeeById(id);
        return ResponseEntity.status(HttpStatus.OK).body(employee);
    }

    @GetMapping("/department")
    public List<Employee> getEmployeesByDepartment(
            @RequestParam String department
    ) {
        return employeeService.getAllEmployeesByDepartment(department);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countEmployees(){
        long count = employeeService.countEmployees();
        return ResponseEntity.status(HttpStatus.OK).body(count);
    }

    @PostMapping
    public ResponseEntity<EmployeeRespond> insertEmployee(
            @ModelAttribute EmployeeRespond employeeRespond,
            @RequestPart("image") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        String originalFilename = file.getOriginalFilename();
        Path filePath = Paths.get(uploadDirectory, originalFilename);
        Files.createDirectories(filePath.getParent());
        try {
            Files.write(filePath, file.getBytes());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
        EmployeeRespond updatedEmployeeRespond = new EmployeeRespond(
                employeeRespond.id(),
                employeeRespond.firstName(),
                employeeRespond.lastName(),
                employeeRespond.email(),
                employeeRespond.phoneNumber(),
                employeeRespond.dateOfJoining(),
                employeeRespond.gender(),
                employeeRespond.address(),
                employeeRespond.position(),
                employeeRespond.department(),
                employeeRespond.salary(),
                originalFilename,
                employeeRespond.createAt(),
                employeeRespond.updateAt()
        );
        EmployeeRespond create = employeeService.createEmployee(updatedEmployeeRespond);
        return ResponseEntity.status(HttpStatus.CREATED).body(create);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeRespond> updateEmployee(
            @PathVariable Long id,
            @ModelAttribute EmployeeRespond employeeRespond,
            @RequestPart(value = "image", required = false) MultipartFile file) throws IOException {

        EmployeeRequest existingEmployeeRespond = employeeService.getEmployeeById(id);
        if (existingEmployeeRespond == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        String profilePicture = existingEmployeeRespond.profilePicture();
        if (file != null && !file.isEmpty()) {
            String originalFilename = file.getOriginalFilename();
            Path filePath = Paths.get(uploadDirectory, originalFilename);
            Files.createDirectories(filePath.getParent());
            try {
                Files.write(filePath, file.getBytes());
                profilePicture = originalFilename;
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        }

        EmployeeRespond updatedEmployeeRespond = new EmployeeRespond(
                id,
                employeeRespond.firstName(),
                employeeRespond.lastName(),
                employeeRespond.email(),
                employeeRespond.phoneNumber(),
                employeeRespond.dateOfJoining(),
                employeeRespond.gender(),
                employeeRespond.address(),
                employeeRespond.position(),
                employeeRespond.department(),
                employeeRespond.salary(),
                profilePicture,
                existingEmployeeRespond.createAt(),
                LocalDateTime.now()
        );

        EmployeeRespond updatedEmployee = employeeService.updateEmployee(updatedEmployeeRespond, id);

        return ResponseEntity.status(HttpStatus.OK).body(updatedEmployee);
    }


    @DeleteMapping("/{employeeId}/departments/{departmentId}/positions/{positionId}")
    public ResponseEntity<String> deleteEmployeeFromDepartmentAndPosition(
            @PathVariable Long employeeId,
            @PathVariable Long departmentId,
            @PathVariable Long positionId
    ) {
        try {
            employeeService.deleteEmployeeFromDepartmentAndPosition(employeeId, departmentId, positionId);
            return ResponseEntity.ok("Employee disassociated from department and position successfully.");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }
}
