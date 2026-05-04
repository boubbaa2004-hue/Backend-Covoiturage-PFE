package com.covoLiv.backend.repository;

import com.covoLiv.backend.entity.Evaluation;
import com.covoLiv.backend.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
    List<Evaluation> findByEvalue(Utilisateur evalue);
    List<Evaluation> findByEvaluateur(Utilisateur evaluateur);
}