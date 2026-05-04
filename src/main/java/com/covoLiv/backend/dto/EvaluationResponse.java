package com.covoLiv.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EvaluationResponse {
    private Long id;
    private Integer note;
    private String commentaire;
    private LocalDateTime dateEvaluation;
    private String nomEvaluateur;
    private String nomEvalue;
}