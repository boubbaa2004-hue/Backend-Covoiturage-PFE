package com.covoLiv.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class LitigeResponse {
    private Long id;
    private String type;
    private String description;
    private String statut;
    private LocalDateTime dateCreation;
    private LocalDateTime dateResolution;
    private String nomPlaignant;
    private String nomAccuse;
}