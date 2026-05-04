package com.covoLiv.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "litiges")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Litige {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String type;

    private String description;

    @Column(nullable = false)
    private String statut = "OUVERT"; // OUVERT, EN_COURS, RESOLU

    private LocalDateTime dateCreation;
    private LocalDateTime dateResolution;

    @ManyToOne
    @JoinColumn(name = "plaignant_id")
    private Utilisateur plaignant;

    @ManyToOne
    @JoinColumn(name = "accuse_id")
    private Utilisateur accuse;
}