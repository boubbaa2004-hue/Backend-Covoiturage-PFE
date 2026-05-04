package com.covoLiv.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "evaluations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer note; // 1 à 5

    private String commentaire;

    private LocalDateTime dateEvaluation;

    @ManyToOne
    @JoinColumn(name = "evaluateur_id", nullable = false)
    private Utilisateur evaluateur;

    @ManyToOne
    @JoinColumn(name = "evalue_id", nullable = false)
    private Utilisateur evalue;

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;
}