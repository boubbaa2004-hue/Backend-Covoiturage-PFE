package com.covoLiv.backend.repository;

import com.covoLiv.backend.entity.Colis;
import com.covoLiv.backend.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ColisRepository extends JpaRepository<Colis, Long> {
    Optional<Colis> findByCodeOTP(String codeOTP);
    List<Colis> findByExpediteur(Utilisateur expediteur);
    List<Colis> findByConducteur(Utilisateur conducteur);
    List<Colis> findByStatut(String statut);
    List<Colis> findByOffreLivraison_ConducteurOrConducteur(
            Utilisateur conducteurOffre, Utilisateur conducteurDirect
    );
}
