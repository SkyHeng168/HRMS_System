package com.cotech.systemcoreapi.dto.PositionDto;

import java.time.LocalDateTime;

public record PositionRespond(
        Long id,
        String positionName,
        Long department,
        LocalDateTime createAt,
        LocalDateTime updateAt
){
}
