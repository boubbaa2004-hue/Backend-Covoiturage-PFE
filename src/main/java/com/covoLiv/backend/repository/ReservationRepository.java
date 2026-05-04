package com.covoLiv.backend.repository;

import com.covoLiv.backend.entity.Reservation;
import com.covoLiv.backend.entity.Utilisateur;
import com.covoLiv.backend.entity.Trajet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByClient(Utilisateur client);
    List<Reservation> findByTrajet(Trajet trajet);
    List<Reservation> findByStatut(String statut);
}