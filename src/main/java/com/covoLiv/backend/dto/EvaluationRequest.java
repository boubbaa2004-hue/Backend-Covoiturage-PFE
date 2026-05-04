package com.covoLiv.backend.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class EvaluationRequest {
    @NotNull
    private Long evalueId;

    @NotNull @Min(1) @Max(5)
    private Integer note;

    private String commentaire;
}