package com.covoLiv.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dateReservation;

    @Column(nullable = false)
    private Integer nbPlaces;

    @Column(nullable = false)
    private Float prixTotal;

    @Column(nullable = false)
    private String statut = "EN_ATTENTE"; // EN_ATTENTE, CONFIRME, ANNULE, TERMINE

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Utilisateur client;

    @ManyToOne
    @JoinColumn(name = "trajet_id", nullable = false)
    private Trajet trajet;
}