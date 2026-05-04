package com.covoLiv.backend.controller;

import com.covoLiv.backend.dto.TrajetRequest;
import com.covoLiv.backend.dto.TrajetResponse;
import com.covoLiv.backend.service.TrajetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trajets")
@RequiredArgsConstructor
public class TrajetController {

    private final TrajetService trajetService;

    @GetMapping
    public ResponseEntity<List<TrajetResponse>> getTousLesTrajets() {
        return ResponseEntity.ok(trajetService.getTousLesTrajets());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrajetResponse> getTrajetById(@PathVariable Long id) {
        return ResponseEntity.ok(trajetService.getTrajetById(id));
    }

    @GetMapping("/recherche")
    public ResponseEntity<List<TrajetResponse>> rechercherTrajets(
            @RequestParam String villeDepart,
            @RequestParam String villeArrivee) {
        return ResponseEntity.ok(trajetService.rechercherTrajets(villeDepart, villeArrivee));
    }

    @PostMapping
    public ResponseEntity<TrajetResponse> creerTrajet(
            @Valid @RequestBody TrajetRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(trajetService.creerTrajet(request, userDetails.getUsername()));
    }

    @GetMapping("/mes-trajets")
    public ResponseEntity<List<TrajetResponse>> getMesTrajets(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(trajetService.getMesTrajets(userDetails.getUsername()));
    }

    @PatchMapping("/{id}/annuler")
    public ResponseEntity<Void> annulerTrajet(@PathVariable Long id) {
        trajetService.annulerTrajet(id);
        return ResponseEntity.ok().build();
    }
}