package com.covoLiv.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "colis")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Colis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Float poids;

    @Column(nullable = false)
    private String villeDepart;

    @Column(nullable = false)
    private String villeArrivee;

    private String nomDestinataire;
    private String telephoneDestinataire;

    @Column(unique = true)
    private String codeOTP;

    @Column(nullable = false)
    private String statut = "EN_ATTENTE"; // EN_ATTENTE, EN_TRANSIT, LIVRE, ANNULE

    private LocalDateTime dateCreation;
    private LocalDateTime dateLivraison;

    private Float prix;

    @ManyToOne
    @JoinColumn(name = "expediteur_id", nullable = false)
    private Utilisateur expediteur;

    @ManyToOne
    @JoinColumn(name = "conducteur_id")
    private Utilisateur conducteur;
}