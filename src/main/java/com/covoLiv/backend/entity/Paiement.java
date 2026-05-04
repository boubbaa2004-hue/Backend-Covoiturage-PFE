package com.covoLiv.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "paiements")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Paiement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Float montant;

    @Column(nullable = false)
    private String methode; // CASH, CARTE, VIREMENT

    @Column(nullable = false)
    private String statut = "EN_ATTENTE"; // EN_ATTENTE, PAYE, REMBOURSE

    private LocalDateTime datePaiement;

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @ManyToOne
    @JoinColumn(name = "colis_id")
    private Colis colis;
}