package com.covoLiv.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ColisResponse {
    private Long id;
    private String description;
    private Float poids;
    private String villeDepart;
    private String villeArrivee;
    private String nomDestinataire;
    private String telephoneDestinataire;
    private String codeOTP;
    private String statut;
    private Float prix;
    private LocalDateTime dateCreation;
    private String nomExpediteur;
    private String nomConducteur;
}