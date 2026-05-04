package com.covoLiv.backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NegociationRequest {
    @NotNull
    private Long trajetId;

    @NotNull
    private Float offreClient;
}