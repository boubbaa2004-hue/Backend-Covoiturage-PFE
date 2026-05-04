package com.covoLiv.backend.controller;

import com.covoLiv.backend.entity.Utilisateur;
import com.covoLiv.backend.repository.ColisRepository;
import com.covoLiv.backend.repository.ReservationRepository;
import com.covoLiv.backend.repository.TrajetRepository;
import com.covoLiv.backend.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UtilisateurRepository utilisateurRepository;
    private final TrajetRepository trajetRepository;
    private final ReservationRepository reservationRepository;
    private final ColisRepository colisRepository;

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUtilisateurs", utilisateurRepository.count());
        stats.put("totalTrajets", trajetRepository.count());
        stats.put("totalReservations", reservationRepository.count());
        stats.put("totalColis", colisRepository.count());
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/utilisateurs")
    public ResponseEntity<List<Utilisateur>> getAllUtilisateurs() {
        return ResponseEntity.ok(utilisateurRepository.findAll());
    }

    @PatchMapping("/utilisateurs/{id}/bloquer")
    public ResponseEntity<Void> bloquerUtilisateur(@PathVariable Long id) {
        Utilisateur u = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        u.setEstActif(false);
        utilisateurRepository.save(u);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/utilisateurs/{id}/debloquer")
    public ResponseEntity<Void> debloquerUtilisateur(@PathVariable Long id) {
        Utilisateur u = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        u.setEstActif(true);
        utilisateurRepository.save(u);
        return ResponseEntity.ok().build();
    }
}