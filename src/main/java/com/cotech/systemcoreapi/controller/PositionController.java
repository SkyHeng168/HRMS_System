package com.cotech.systemcoreapi.controller;

import com.cotech.systemcoreapi.dto.PositionDto.PositionRequest;
import com.cotech.systemcoreapi.dto.PositionDto.PositionRespond;
import com.cotech.systemcoreapi.exception.CustomException.NotFoundException;
import com.cotech.systemcoreapi.service.impl.PositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/version1/position")
public class PositionController {
    private final PositionService positionService;

    @GetMapping
    public ResponseEntity<List<PositionRequest>> getAllPositions() {
        List<PositionRequest> positions = positionService.getAllPositions();
        return ResponseEntity.status(HttpStatus.OK).body(positions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPositionById(@PathVariable Long id) {
        try {
            PositionRequest position = positionService.getPositionById(id);
            return ResponseEntity.ok(position);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }


    @PostMapping
    public ResponseEntity<PositionRespond> createPosition(
            @RequestBody PositionRespond positionRespond
    ) {
        PositionRespond insertPosition = positionService.createPosition(positionRespond);
        return ResponseEntity.status(HttpStatus.CREATED).body(insertPosition);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PositionRespond> updatePosition(
            @PathVariable Long id,
            @RequestBody PositionRespond positionRespond
    ){
        PositionRespond updatePosition = positionService.updatePosition(positionRespond, id);
        return ResponseEntity.status(HttpStatus.OK).body(updatePosition);
    }

    @DeleteMapping("/department/{departmentId}/position/{positionId}")
    public ResponseEntity<String> deletePositionFromDepartment(
            @PathVariable Long departmentId,
            @PathVariable Long positionId) {
        try {
            positionService.deletePositionFromDepartment(departmentId, positionId);

            return ResponseEntity.status(HttpStatus.OK).body("Position and associated employees deleted successfully");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()).toString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An error occurred during deletion").toString());
        }
    }


}
