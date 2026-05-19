package com.covoLiv.backend.dto;

import lombok.Data;

@Data
public class ColisRequest {
    private String description;
    private Float poids;
    private String villeDepart;
    private String villeArrivee;
    private String nomDestinataire;
    private String telephoneDestinataire;
    private Long offreLivraisonId; // optionnel
}