package com.covoLiv.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "position_tracking")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PositionTracking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long colisId;
    private Long conducteurId;

    private Double latitude;
    private Double longitude;

    private LocalDateTime updatedAt;

    // Statut livraison pour info
    private String statut;
}