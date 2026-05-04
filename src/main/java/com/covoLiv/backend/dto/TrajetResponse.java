package com.covoLiv.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TrajetResponse {
    private Long id;
    private String villeDepart;
    private String villeArrivee;
    private LocalDateTime dateHeure;
    private Integer placesDisponibles;
    private Float volumeCoffre;
    private Float prixParPlace;
    private String statut;
    private String description;
    private String nomConducteur;
    private Long conducteurId;
    private Float noteConducteur;
    private Boolean conducteurVerifie;
}