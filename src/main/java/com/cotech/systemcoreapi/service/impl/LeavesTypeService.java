package com.cotech.systemcoreapi.service.impl;

import com.cotech.systemcoreapi.dto.LeavesDto.LeavesRequest;
import com.cotech.systemcoreapi.dto.LeavesDto.LeavesRespond;
import com.cotech.systemcoreapi.exception.CustomException.EmployeeNotFoundException;
import com.cotech.systemcoreapi.exception.CustomException.MaxLeavesExceededException;
import com.cotech.systemcoreapi.exception.CustomException.NotFoundException;
import com.cotech.systemcoreapi.model.Employee;
import com.cotech.systemcoreapi.model.Enum.LeavesStatus;
import com.cotech.systemcoreapi.model.Enum.LeavesType;
import com.cotech.systemcoreapi.model.Leaves;
import com.cotech.systemcoreapi.repository.EmployeeRepository;
import com.cotech.systemcoreapi.repository.LeavesTypeRepository;
import com.cotech.systemcoreapi.service.ILeavesTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LeavesTypeService implements ILeavesTypeService {
    private final LeavesTypeRepository leavesTypeRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public LeavesRespond createLeavesType(LeavesRespond leavesRespond) {
        try {
            if (leavesRespond == null) {
                throw new IllegalArgumentException("LeavesRespond cannot be null");
            }

            // Log employee ID
            log.info("Employee ID from request: {}", leavesRespond.employee());

            Employee employee = employeeRepository.findById(leavesRespond.employee())
                    .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + leavesRespond.employee()));

            // Automatically delete expired leaves
            deleteExpiredLeaves(employee);

            // Calculate start and end of the current month
            LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
            LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());

            // Log date range
            log.info("Start of month: {}, End of month: {}", startOfMonth, endOfMonth);

            // Count leaves in the current month
            long leavesInCurrentMonth = leavesTypeRepository.countByEmployeeAndStartDateBetween(
                    employee,
                    startOfMonth,
                    endOfMonth);

            // Log leaves count
            log.info("Leaves in current month: {}", leavesInCurrentMonth);

            if (leavesInCurrentMonth >= 4) {
                throw new RuntimeException("Employee has already requested the maximum allowed leaves (4) for this month.");
            }

            // Proceed with creating the leave request
            LeavesType type = parseLeavesType(leavesRespond.leavesType());
            LeavesStatus status = parseLeavesStatus(leavesRespond.leavesStatus());

            Leaves leavesCreate = Leaves.builder()
                    .startDate(leavesRespond.startDate())
                    .endDate(leavesRespond.endDate())
                    .reason(leavesRespond.reason())
                    .leavesType(type)
                    .leavesStatus(status)
                    .employee(employee)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            Leaves savedLeave = leavesTypeRepository.save(leavesCreate);

            return new LeavesRespond(
                    savedLeave.getId(),
                    savedLeave.getStartDate(),
                    savedLeave.getEndDate(),
                    savedLeave.getReason(),
                    savedLeave.getLeavesType().name(),
                    savedLeave.getLeavesStatus().name(),
                    savedLeave.getEmployee().getId(),
                    savedLeave.getCreatedAt(),
                    savedLeave.getUpdatedAt()
            );
        } catch (IllegalArgumentException | EmployeeNotFoundException e) {
            throw new RuntimeException("Error occurred while processing the leave request: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred while creating leave: " + e.getMessage(), e);
        }
    }

    private void deleteExpiredLeaves(Employee employee) {
        LocalDate currentDate = LocalDate.now();

        List<Leaves> expiredLeaves = leavesTypeRepository.findByEmployeeAndEndDateBefore(employee, currentDate);

        if (!expiredLeaves.isEmpty()) {
            log.info("Deleting expired leaves for employee ID: {}", employee.getId());
            leavesTypeRepository.deleteAll(expiredLeaves);
            log.info("Expired leaves deleted successfully.");
        }
    }

    @Override
    public List<LeavesRequest> getAllLeavesType() {
        List<Leaves> leavesList = leavesTypeRepository.findAll();
        return leavesList.stream()
                .map(leaves -> new LeavesRequest(
                        leaves.getId(),
                        leaves.getStartDate(),
                        leaves.getEndDate(),
                        leaves.getReason(),
                        leaves.getLeavesType().name(),
                        leaves.getLeavesStatus().name(),
                        leaves.getEmployee().getLastName()
                )).collect(Collectors.toList());
    }

    @Override
    public void deleteLeavesType(Long id) {
        Leaves leaves = leavesTypeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Leaves not found with ID: " + id));
        leavesTypeRepository.delete(leaves);
    }

    private LeavesType parseLeavesType(String type) {
        return Arrays.stream(LeavesType.values())
                .filter(t -> t.name().equalsIgnoreCase(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid leave type: " + type));
    }

    private LeavesStatus parseLeavesStatus(String status) {
        return Arrays.stream(LeavesStatus.values())
                .filter(s -> s.name().equalsIgnoreCase(status))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid leave status: " + status));
    }
}
