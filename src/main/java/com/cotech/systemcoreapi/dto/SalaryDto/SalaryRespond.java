package com.cotech.systemcoreapi.dto.SalaryDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record SalaryRespond (
        Long id,
        Double basicSalary,
        Double allowance,
        Double deductions,
        Double netSalary,
        LocalDate paymentDate,
        LocalDateTime createAt,
        LocalDateTime updateAt
){
}
