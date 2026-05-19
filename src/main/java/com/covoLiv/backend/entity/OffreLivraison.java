package com.covoLiv.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "offres_livraison")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OffreLivraison {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String villeDepart;
    private String villeArrivee;
    private Float prixParKg;
    private Float poidsMax;
    private String description;

    // ACTIF, INACTIF
    private String statut = "ACTIF";

    private LocalDateTime dateCreation;

    @ManyToOne
    @JoinColumn(name = "conducteur_id")
    private Utilisateur conducteur;
    private Float poidsRestant; // initialisé = poidsMax
}