package com.covoLiv.backend.repository;

import com.covoLiv.backend.entity.PositionTracking;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PositionTrackingRepository extends JpaRepository<PositionTracking, Long> {
    Optional<PositionTracking> findByColisId(Long colisId);
}