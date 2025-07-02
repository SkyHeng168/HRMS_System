package com.cotech.systemcoreapi.service.impl;

import com.cotech.systemcoreapi.dto.PositionDto.PositionRequest;
import com.cotech.systemcoreapi.dto.PositionDto.PositionRespond;
import com.cotech.systemcoreapi.exception.CustomException.NotFoundException;
import com.cotech.systemcoreapi.model.Department;
import com.cotech.systemcoreapi.model.Employee;
import com.cotech.systemcoreapi.model.Position;
import com.cotech.systemcoreapi.repository.DepartmentRepository;
import com.cotech.systemcoreapi.repository.EmployeeRepository;
import com.cotech.systemcoreapi.repository.PositionRepository;
import com.cotech.systemcoreapi.service.IPositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PositionService implements IPositionService {
    private final PositionRepository positionRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public PositionRespond createPosition(PositionRespond positionRespond) {
        Position existingPosition = positionRepository.findByPositionName(positionRespond.positionName());
        if (existingPosition != null) {
            throw new NotFoundException("Position already exists with the name: " + positionRespond.positionName());
        }
        Department department = departmentRepository.findById(positionRespond.department())
                .orElseThrow(() -> new NotFoundException("Department not found: " + positionRespond.department()));
        Position newPosition = Position.builder()
                .positionName(positionRespond.positionName())
                .department(department)
                .createAt(LocalDateTime.now())
                .build();
        Position savedPosition = positionRepository.save(newPosition);
        return new PositionRespond(
                savedPosition.getId(),
                savedPosition.getPositionName(),
                savedPosition.getDepartment().getId(),
                savedPosition.getCreateAt(),
                savedPosition.getUpdateAt()
        );
    }

    @Override
    public PositionRespond updatePosition(PositionRespond positionRespond, Long id) {
        Position existingPosition = positionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Position not found with id: " + id));
        if (positionRepository.findByPositionName(positionRespond.positionName()) != null
                && !existingPosition.getPositionName().equals(positionRespond.positionName())) {
            throw new NotFoundException("Position already exists with the name: " + positionRespond.positionName());
        }
        existingPosition.setPositionName(positionRespond.positionName());
        existingPosition.setUpdateAt(LocalDateTime.now());
        Position updatedPosition = positionRepository.save(existingPosition);
        return new PositionRespond(
                updatedPosition.getId(),
                updatedPosition.getPositionName(),
                updatedPosition.getDepartment().getId(),
                updatedPosition.getCreateAt(),
                updatedPosition.getUpdateAt()
        );
    }

    @Override
    public List<PositionRequest> getAllPositions() {
        List<Position> positions = positionRepository.findAll();

        return positions.stream()
                .map(position -> new PositionRequest(
                        position.getId(),
                        position.getPositionName(),
                        position.getEmployees() != null ?
                                position.getEmployees().stream()
                                        .map(employee -> employee.getFirstName() + " " + employee.getLastName())
                                        .collect(Collectors.toList()) : new ArrayList<>(),
                        position.getDepartment() != null ? position.getDepartment().getDepartmentName() : "No Department"
                ))
                .collect(Collectors.toList());
    }

    @Override
    public PositionRequest getPositionById(Long id) {
        Position position = positionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Position not found with ID: " + id));

        List<String> employeeNames = position.getEmployees() != null ?
                position.getEmployees().stream()
                        .map(employee -> employee.getFirstName() + " " + employee.getLastName())
                        .collect(Collectors.toList()) : new ArrayList<>();
        return new PositionRequest(
                position.getId(),
                position.getPositionName(),
                employeeNames,
                position.getDepartment() != null ? position.getDepartment().getDepartmentName() : "No Department"
        );
    }

    @Override
    public void deletePositionFromDepartment(Long departmentId, Long positionId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new NotFoundException("Department not found with ID: " + departmentId));

        Position position = department.getPositions().stream()
                .filter(p -> p.getId().equals(positionId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Position not found with ID: " + positionId));

        department.getPositions().remove(position);

        positionRepository.delete(position);

        List<Employee> employeesWithoutPosition = employeeRepository.findByPositionNull();
        for (Employee employee : employeesWithoutPosition) {
            if (employee.getPosition() == null) {
                employeeRepository.delete(employee);
            }
        }
    }

}
