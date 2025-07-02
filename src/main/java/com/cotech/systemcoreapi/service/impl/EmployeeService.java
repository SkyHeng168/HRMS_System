package com.cotech.systemcoreapi.service.impl;

import com.cotech.systemcoreapi.dto.EmployeeDto.EmployeeRequest;
import com.cotech.systemcoreapi.dto.EmployeeDto.EmployeeRespond;
import com.cotech.systemcoreapi.exception.CustomException.ResourceNotFoundException;
import com.cotech.systemcoreapi.model.*;
import com.cotech.systemcoreapi.repository.*;
import com.cotech.systemcoreapi.service.IEmployeeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService implements IEmployeeService {
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;
    private final AttendanceRepository attendanceRepository;
    private final SalaryRepository salaryRepository;
    private final ProjectRepository projectRepository;

    @Override
    public List<EmployeeRequest> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream()
                .map(employee -> new EmployeeRequest(
                        employee.getId(),
                        employee.getFirstName(),
                        employee.getLastName(),
                        employee.getEmail(),
                        employee.getPhoneNumber(),
                        employee.getDateOfJoining(),
                        employee.getGender(),
                        employee.getAddress(),
                        employee.getPosition() != null ? employee.getPosition().getPositionName() : "No Position",
                        employee.getDepartment() != null ? employee.getDepartment().getDepartmentName() : "No department",
                        employee.getSalary() != null ? employee.getSalary().getBasicSalary() : null,
                        employee.getProfilePicture(),
                        employee.getCreatedAt(),
                        employee.getUpdatedAt()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeRequest getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee with id " + id + " not found"));

        return new EmployeeRequest(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getPhoneNumber(),
                employee.getDateOfJoining(),
                employee.getGender(),
                employee.getAddress(),
                employee.getPosition() != null ? employee.getPosition().getPositionName() : null,
                employee.getDepartment() != null ? employee.getDepartment().getDepartmentName() : null,
                employee.getSalary() != null ? employee.getSalary().getBasicSalary() : null,
                employee.getProfilePicture(),
                employee.getCreatedAt(),
                employee.getUpdatedAt()
        );
    }

    @Override
    public EmployeeRespond createEmployee(EmployeeRespond employeeRespond) {
        Department department = departmentRepository.findById(employeeRespond.department())
                .orElseThrow(() -> new ResourceNotFoundException("Department"));

        Position position = positionRepository.findById(employeeRespond.position())
                .orElseThrow(() -> new ResourceNotFoundException("Position"));
        Salary salary = salaryRepository.findById(employeeRespond.salary())
                .orElseThrow(() -> new ResourceNotFoundException("Salary"));

        Employee newEmployee = Employee.builder()
                .firstName(employeeRespond.firstName())
                .lastName(employeeRespond.lastName())
                .email(employeeRespond.email())
                .phoneNumber(employeeRespond.phoneNumber())
                .dateOfJoining(LocalDate.now())
                .gender(employeeRespond.gender())
                .address(employeeRespond.address())
                .position(position)
                .department(department)
                .salary(salary)
                .profilePicture(employeeRespond.profilePicture())
                .createdAt(LocalDateTime.now())
                .build();

        Employee employeeSaved = employeeRepository.save(newEmployee);

        return new EmployeeRespond(
                employeeSaved.getId(),
                employeeSaved.getFirstName(),
                employeeSaved.getLastName(),
                employeeSaved.getEmail(),
                employeeSaved.getPhoneNumber(),
                employeeSaved.getDateOfJoining().atStartOfDay(),
                employeeSaved.getGender(),
                employeeSaved.getAddress(),
                employeeSaved.getPosition().getId(),
                employeeSaved.getDepartment().getId(),
                employeeSaved.getSalary().getId(),
                employeeSaved.getProfilePicture(),
                employeeSaved.getCreatedAt(),
                employeeSaved.getUpdatedAt()
        );
    }

    @Override
    public EmployeeRespond updateEmployee(EmployeeRespond employeeRespond, Long id) {

        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        Department department = departmentRepository.findById(employeeRespond.department())
                .orElseThrow(() -> new ResourceNotFoundException("Department"));

        Position position = positionRepository.findById(employeeRespond.position())
                .orElseThrow(() -> new ResourceNotFoundException("Position"));
        Salary salary = salaryRepository.findById(employeeRespond.salary())
                .orElseThrow(() -> new ResourceNotFoundException("Salary"));

        existingEmployee.setFirstName(employeeRespond.firstName());
        existingEmployee.setLastName(employeeRespond.lastName());
        existingEmployee.setEmail(employeeRespond.email());
        existingEmployee.setPhoneNumber(employeeRespond.phoneNumber());
        existingEmployee.setGender(employeeRespond.gender());
        existingEmployee.setAddress(employeeRespond.address());
        existingEmployee.setPosition(position);
        existingEmployee.setDepartment(department);
        existingEmployee.setSalary(salary);
        existingEmployee.setProfilePicture(employeeRespond.profilePicture());
        existingEmployee.setUpdatedAt(LocalDateTime.now());

        Employee updatedEmployee = employeeRepository.save(existingEmployee);

        return new EmployeeRespond(
                updatedEmployee.getId(),
                updatedEmployee.getFirstName(),
                updatedEmployee.getLastName(),
                updatedEmployee.getEmail(),
                updatedEmployee.getPhoneNumber(),
                updatedEmployee.getDateOfJoining().atStartOfDay(),
                updatedEmployee.getGender(),
                updatedEmployee.getAddress(),
                updatedEmployee.getPosition().getId(),
                updatedEmployee.getDepartment().getId(),
                updatedEmployee.getSalary().getId(),
                updatedEmployee.getProfilePicture(),
                updatedEmployee.getCreatedAt(),
                updatedEmployee.getUpdatedAt()
        );
    }

    @Override
    public List<Employee> getAllEmployeesByDepartment(String department) {
        return employeeRepository.findByDepartment_DepartmentName(department);
    }

    @Override
    public long countEmployees() {
        return employeeRepository.count();
    }

    @Override
    @Transactional
    public void deleteEmployeeFromDepartmentAndPosition(Long employeeId, Long departmentId, Long positionId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found: " + employeeId));

        // Remove the employee from the department if associated
        if (employee.getDepartment() != null && employee.getDepartment().getId().equals(departmentId)) {
            employee.setDepartment(null);
        } else {
            throw new ResourceNotFoundException("Employee is not associated with the specified department: " + departmentId);
        }

        // Remove the employee from the position if associated
        if (employee.getPosition() != null && employee.getPosition().getId().equals(positionId)) {
            employee.setPosition(null);
        } else {
            throw new ResourceNotFoundException("Employee is not associated with the specified position: " + positionId);
        }

        // Remove the employee from any projects they are part of
        if (employee.getProjects() != null && !employee.getProjects().isEmpty()) {
            for (Project project : employee.getProjects()) {
                project.getEmployees().remove(employee);  // Remove the employee from the project's employee list
                projectRepository.save(project);  // Save the updated project
            }
        }

        if (employee.getAttendances() != null && !employee.getAttendances().isEmpty()) {
            for (Attendance attendance : employee.getAttendances()) {
                attendance.setEmployee(null);
                attendanceRepository.save(attendance);  // Save the updated attendance record
            }
        }
        employeeRepository.delete(employee);
    }

}
