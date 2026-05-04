package com.covoLiv.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TrajetRequest {
    @NotBlank
    private String villeDepart;

    @NotBlank
    private String villeArrivee;

    @NotNull
    private LocalDateTime dateHeure;

    @NotNull
    private Integer placesDisponibles;

    private Float volumeCoffre;

    @NotNull
    private Float prixParPlace;

    private String description;
}