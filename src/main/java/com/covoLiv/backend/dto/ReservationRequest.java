package com.covoLiv.backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReservationRequest {
    @NotNull
    private Long trajetId;

    @NotNull
    private Integer nbPlaces;
}