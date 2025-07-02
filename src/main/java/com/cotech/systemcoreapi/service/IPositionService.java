package com.cotech.systemcoreapi.service;

import com.cotech.systemcoreapi.dto.PositionDto.PositionRequest;
import com.cotech.systemcoreapi.dto.PositionDto.PositionRespond;

import java.util.List;

public interface IPositionService {
    PositionRespond createPosition(PositionRespond positionRespond);
    PositionRespond updatePosition(PositionRespond positionRespond, Long id);
    List<PositionRequest> getAllPositions();
    PositionRequest getPositionById(Long id);
    void deletePositionFromDepartment(Long departmentId, Long positionId);
}
