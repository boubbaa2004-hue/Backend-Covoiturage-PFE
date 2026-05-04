package com.covoLiv.backend.repository;

import com.covoLiv.backend.entity.Paiement;
import com.covoLiv.backend.entity.Reservation;
import com.covoLiv.backend.entity.Colis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaiementRepository extends JpaRepository<Paiement, Long> {
    Optional<Paiement> findByReservation(Reservation reservation);
    Optional<Paiement> findByColis(Colis colis);
    List<Paiement> findByStatut(String statut);
}