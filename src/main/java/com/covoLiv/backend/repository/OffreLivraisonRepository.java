package com.covoLiv.backend.repository;

import com.covoLiv.backend.entity.OffreLivraison;
import com.covoLiv.backend.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OffreLivraisonRepository extends JpaRepository<OffreLivraison, Long> {
    List<OffreLivraison> findByStatut(String statut);
    List<OffreLivraison> findByConducteur(Utilisateur conducteur);
}