package com.covoLiv.backend.dto;

import lombok.Data;

@Data
public class OffreLivraisonRequest {
    private String villeDepart;
    private String villeArrivee;
    private Float prixParKg;
    private Float poidsMax;
    private String description;
}