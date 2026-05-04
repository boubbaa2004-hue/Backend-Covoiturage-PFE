package com.covoLiv.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ColisRequest {
    @NotBlank
    private String description;

    @NotNull
    private Float poids;

    @NotBlank
    private String villeDepart;

    @NotBlank
    private String villeArrivee;

    @NotBlank
    private String nomDestinataire;

    @NotBlank
    private String telephoneDestinataire;

    private Float prix;
}