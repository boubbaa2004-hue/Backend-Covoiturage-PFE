package com.covoLiv.backend.dto;

import lombok.Data;

@Data
public class ConducteurResponse {
    private Long id;
    private String nom;
    private String email;
    private String telephone;
    private String permisConduire;
    private String pieceIdentite;
    private String photoVoiture;
    private String carteGrise;
    private String marqueVoiture;
    private Boolean estVerifie;
    private String statutVerification;
    private Boolean estActif;
}