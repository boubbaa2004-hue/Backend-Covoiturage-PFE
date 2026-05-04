package com.covoLiv.backend.repository;

import com.covoLiv.backend.entity.Trajet;
import com.covoLiv.backend.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TrajetRepository extends JpaRepository<Trajet, Long> {
    List<Trajet> findByStatut(String statut);
    List<Trajet> findByConducteur(Utilisateur conducteur);
    List<Trajet> findByVilleDepartAndVilleArriveeAndStatut(String villeDepart, String villeArrivee, String statut);
}