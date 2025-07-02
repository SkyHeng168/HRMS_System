package com.cotech.systemcoreapi.dto.PositionDto;

import java.util.List;

public record PositionRequest (
        Long id,
        String positionName,
        List<String> employees,
        String department
){
}
