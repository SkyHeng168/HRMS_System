package com.cotech.systemcoreapi.service.impl;

import com.cotech.systemcoreapi.dto.DepartmentDto.DepartmentRequest;
import com.cotech.systemcoreapi.dto.DepartmentDto.DepartmentRespond;
import com.cotech.systemcoreapi.exception.CustomException.NotFoundException;
import com.cotech.systemcoreapi.model.Department;
import com.cotech.systemcoreapi.model.Position;
import com.cotech.systemcoreapi.repository.DepartmentRepository;
import com.cotech.systemcoreapi.service.IDepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class DepartmentService implements IDepartmentService {
    private final DepartmentRepository departmentRepository;

    @Override
    public DepartmentRespond createDepartment(DepartmentRespond departmentRespond) {
        Department exitingDepartment = departmentRepository.findByDepartmentName(departmentRespond.departmentName());
        if (exitingDepartment != null) {
            throw new NotFoundException("Department already exists");
        }
        Department department = Department.builder()
                .departmentName(departmentRespond.departmentName())
                .createAt(LocalDateTime.now())
                .build();
        Department savedDepartment = departmentRepository.save(department);
        return new DepartmentRespond(
                savedDepartment.getId(),
                savedDepartment.getDepartmentName(),
                savedDepartment.getCreateAt(),
                savedDepartment.getUpdateAt()
        );
    }

    @Override
    public List<DepartmentRequest> getAllDepartments() {
        List<Department> departments = departmentRepository.findAll(); // Get all departments from the repository
        return departments.stream()
                .map(department -> new DepartmentRequest(
                        department.getId(),
                        department.getDepartmentName(),
                        (department.getPositions() == null || department.getPositions().isEmpty())
                                ? null
                                : department.getPositions().stream()
                                .map(Position::getPositionName) // Extract position names
                                .collect(Collectors.toList()), // Collect them into a list
                        department.getCreateAt(),
                        department.getUpdateAt()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public DepartmentRequest getDepartment(Long id) {
        Optional<Department> optionalDepartment = departmentRepository.findById(id);
        if (optionalDepartment.isPresent()) {
            Department department = optionalDepartment.get();

            List<String> positionNames = department.getPositions().stream()
                    .map(Position::getPositionName)
                    .collect(Collectors.toList());

            return new DepartmentRequest(
                    department.getId(),
                    department.getDepartmentName(),
                    positionNames,
                    department.getCreateAt(),
                    department.getUpdateAt()
            );
        } else {
            throw new NotFoundException("Department not found");
        }
    }

    @Override
    public DepartmentRespond updateDepartment(DepartmentRespond departmentRespond, Long id) {
        Department existingDepartment = departmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Department with id %s not found", id)));
        Department exitingDepartment = departmentRepository.findByDepartmentName(departmentRespond.departmentName());
        if (exitingDepartment != null) {
            throw new NotFoundException("Department already exists");
        }
        if (existingDepartment == null) {
            Department newDepartment = Department.builder()
                    .departmentName(departmentRespond.departmentName())
                    .createAt(LocalDateTime.now())
                    .build();
            Department savedDepartment = departmentRepository.save(newDepartment);
            return new DepartmentRespond(
                    savedDepartment.getId(),
                    savedDepartment.getDepartmentName(),
                    savedDepartment.getCreateAt(),
                    savedDepartment.getUpdateAt()
            );
        } else {
            existingDepartment.setDepartmentName(departmentRespond.departmentName());
            existingDepartment.setUpdateAt(LocalDateTime.now());
            Department updatedDepartment = departmentRepository.save(existingDepartment);
            return new DepartmentRespond(
                    updatedDepartment.getId(),
                    updatedDepartment.getDepartmentName(),
                    updatedDepartment.getCreateAt(),
                    updatedDepartment.getUpdateAt()
            );
        }
    }

    @Override
    public void deleteDepartment(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Department not found" + id));
        departmentRepository.delete(department);
    }

}
