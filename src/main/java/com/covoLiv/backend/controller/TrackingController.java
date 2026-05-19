package com.covoLiv.backend.controller;

import com.covoLiv.backend.entity.PositionTracking;
import com.covoLiv.backend.repository.ColisRepository;
import com.covoLiv.backend.repository.PositionTrackingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/tracking")
@RequiredArgsConstructor
public class TrackingController {

    private final PositionTrackingRepository trackingRepo;
    private final ColisRepository colisRepo;

    @PostMapping("/position/{colisId}")
    public ResponseEntity<?> updatePosition(
            @PathVariable Long colisId,
            @RequestBody Map<String, Double> body,
            @AuthenticationPrincipal UserDetails userDetails) {

        Double lat = body.get("latitude");
        Double lng = body.get("longitude");

        if (lat == null || lng == null)
            return ResponseEntity.badRequest().body("Coordonnées manquantes");

        PositionTracking tracking = trackingRepo.findByColisId(colisId)
                .orElse(PositionTracking.builder().colisId(colisId).build());

        tracking.setLatitude(lat);
        tracking.setLongitude(lng);
        tracking.setUpdatedAt(LocalDateTime.now());

        colisRepo.findById(colisId).ifPresent(c -> {
            tracking.setStatut(c.getStatut());
            if (c.getConducteur() != null)
                tracking.setConducteurId(c.getConducteur().getId());
        });

        trackingRepo.save(tracking);
        System.out.println("=== POSITION SAVED === Colis " + colisId + " → " + lat + ", " + lng);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "colisId", colisId,
                "latitude", lat,
                "longitude", lng,
                "updatedAt", tracking.getUpdatedAt().toString()
        ));
    }
    @GetMapping("/position/{colisId}")
    public ResponseEntity<?> getPosition(@PathVariable Long colisId) {
        return trackingRepo.findByColisId(colisId)
                .map(t -> ResponseEntity.ok(Map.of(
                        "found", true,
                        "colisId", t.getColisId(),
                        "latitude", t.getLatitude(),
                        "longitude", t.getLongitude(),
                        "statut", t.getStatut() != null ? t.getStatut() : "",
                        "updatedAt", t.getUpdatedAt().toString()
                )))
                .orElse(ResponseEntity.ok(Map.of("found", false)));
    }
}