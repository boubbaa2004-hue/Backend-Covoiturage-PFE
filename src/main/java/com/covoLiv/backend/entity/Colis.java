package com.covoLiv.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "colis")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Colis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private Float poids;
    private String villeDepart;
    private String villeArrivee;
    private String nomDestinataire;
    private String telephoneDestinataire;
    private Float prix;
    private String codeOTP;

    // EN_ATTENTE → PRIX_PROPOSE → ACCEPTE → EN_TRANSIT → LIVRE
    private String statut = "EN_ATTENTE";

    private LocalDateTime dateCreation;
    private LocalDateTime dateLivraison;

    @ManyToOne
    @JoinColumn(name = "expediteur_id")
    private Utilisateur expediteur;

    @ManyToOne
    @JoinColumn(name = "conducteur_id")
    private Utilisateur conducteur;

    @ManyToOne
    @JoinColumn(name = "offre_livraison_id")
    private OffreLivraison offreLivraison;
}