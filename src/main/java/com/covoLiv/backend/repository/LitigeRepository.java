package com.covoLiv.backend.repository;

import com.covoLiv.backend.entity.Litige;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LitigeRepository extends JpaRepository<Litige, Long> {
    List<Litige> findByStatut(String statut);
    List<Litige> findByPlaignantId(Long id);
}