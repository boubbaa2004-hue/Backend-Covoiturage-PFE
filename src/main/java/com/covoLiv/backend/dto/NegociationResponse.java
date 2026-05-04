package com.covoLiv.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NegociationResponse {
    private Long id;
    private Float offreClient;
    private Float offreConducteur;
    private String statut;
    private LocalDateTime dateCreation;
    private String nomClient;
    private String villeDepart;
    private String villeArrivee;
    private Long trajetId;
}
