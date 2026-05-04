package com.covoLiv.backend.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private String role;
    private String nom;
    private String email;
    private Long id;
    private Boolean estVerifie;
    private String statutVerification;

    public AuthResponse(String token, String role, String nom,
                        String email, Long id,
                        Boolean estVerifie, String statutVerification) {
        this.token = token;
        this.role = role;
        this.nom = nom;
        this.email = email;
        this.id = id;
        this.estVerifie = estVerifie;
        this.statutVerification = statutVerification;
    }
}