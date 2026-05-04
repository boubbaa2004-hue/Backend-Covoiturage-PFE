package com.covoLiv.backend.repository;

import com.covoLiv.backend.entity.Negociation;
import com.covoLiv.backend.entity.Utilisateur;
import com.covoLiv.backend.entity.Trajet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NegociationRepository extends JpaRepository<Negociation, Long> {
    List<Negociation> findByClient(Utilisateur client);
    List<Negociation> findByTrajet(Trajet trajet);
    List<Negociation> findByStatut(String statut);
}