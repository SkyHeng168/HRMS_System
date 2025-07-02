package com.cotech.systemcoreapi.controller;

import com.cotech.systemcoreapi.dto.LeavesDto.LeavesRequest;
import com.cotech.systemcoreapi.dto.LeavesDto.LeavesRespond;
import com.cotech.systemcoreapi.service.impl.LeavesTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/version1/leaves")
public class LeavesController {
    private final LeavesTypeService leavesTypeService;

    @GetMapping
    public ResponseEntity<List<LeavesRequest>> getAllLeaves() {
        List<LeavesRequest> leavesRequest = leavesTypeService.getAllLeavesType();
        return ResponseEntity.status(HttpStatus.CREATED).body(leavesRequest);
    }

    @PostMapping
    public ResponseEntity<LeavesRespond> createLeaves(
            @RequestBody LeavesRespond leavesRespond
    ){
        LeavesRespond saveLeaves = leavesTypeService.createLeavesType(leavesRespond);
        return ResponseEntity.status(HttpStatus.CREATED).body(saveLeaves);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLeaves(
            @PathVariable Long id
    ){
        leavesTypeService.deleteLeavesType(id);
        return ResponseEntity.status(HttpStatus.CREATED).body("Leaves deleted Successfully: " + id);
    }
}
