package com.covoLiv.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "negociations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Negociation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Float offreClient;

    private Float offreConducteur;

    @Column(nullable = false)
    private String statut = "EN_COURS"; // EN_COURS, ACCEPTE, REFUSE

    private LocalDateTime dateCreation;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Utilisateur client;

    @ManyToOne
    @JoinColumn(name = "trajet_id", nullable = false)
    private Trajet trajet;
}