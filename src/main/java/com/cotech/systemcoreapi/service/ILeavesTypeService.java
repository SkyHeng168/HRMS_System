package com.cotech.systemcoreapi.service;

import com.cotech.systemcoreapi.dto.LeavesDto.LeavesRequest;
import com.cotech.systemcoreapi.dto.LeavesDto.LeavesRespond;

import java.util.List;

public interface ILeavesTypeService {
    LeavesRespond createLeavesType(LeavesRespond leavesRespond);
    List<LeavesRequest> getAllLeavesType();
    void deleteLeavesType(Long id);
}
