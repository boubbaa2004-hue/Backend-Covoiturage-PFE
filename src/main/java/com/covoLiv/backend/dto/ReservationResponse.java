package com.covoLiv.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReservationResponse {
    private Long id;
    private LocalDateTime dateReservation;
    private Integer nbPlaces;
    private Float prixTotal;
    private String statut;
    private String nomClient;
    private String villeDepart;
    private String villeArrivee;
    private LocalDateTime dateHeure;
    private String nomConducteur;
}