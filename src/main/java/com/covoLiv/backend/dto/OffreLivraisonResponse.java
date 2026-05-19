package com.covoLiv.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class OffreLivraisonResponse {
    private Long id;
    private String villeDepart;
    private String villeArrivee;
    private Float prixParKg;
    private Float poidsMax;
    private String description;
    private String statut;
    private LocalDateTime dateCreation;
    private Long conducteurId;
    private String nomConducteur;
    private String photoProfile;
    private String marqueVoiture;
    private Float noteConducteur;
    private Float poidsRestant;
}