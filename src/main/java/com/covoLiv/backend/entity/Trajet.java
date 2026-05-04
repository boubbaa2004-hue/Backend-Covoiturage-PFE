package com.covoLiv.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "trajets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Trajet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String villeDepart;

    @Column(nullable = false)
    private String villeArrivee;

    @Column(nullable = false)
    private LocalDateTime dateHeure;

    @Column(nullable = false)
    private Integer placesDisponibles;

    private Float volumeCoffre;

    @Column(nullable = false)
    private Float prixParPlace;

    @Column(nullable = false)
    private String statut = "ACTIF"; // ACTIF, ANNULE, TERMINE

    private String description;

    @ManyToOne
    @JoinColumn(name = "conducteur_id", nullable = false)
    private Utilisateur conducteur;
}