package com.cotech.systemcoreapi.repository;

import com.cotech.systemcoreapi.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {
    Position findByPositionName(String positionName);
}
