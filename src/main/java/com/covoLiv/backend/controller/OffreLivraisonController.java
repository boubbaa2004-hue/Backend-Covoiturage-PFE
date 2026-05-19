package com.covoLiv.backend.controller;

import com.covoLiv.backend.dto.OffreLivraisonRequest;
import com.covoLiv.backend.dto.OffreLivraisonResponse;
import com.covoLiv.backend.service.OffreLivraisonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/offres-livraison")
@RequiredArgsConstructor
public class OffreLivraisonController {

    private final OffreLivraisonService service;

    @PostMapping
    public ResponseEntity<OffreLivraisonResponse> publier(
            @RequestBody OffreLivraisonRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(service.publierOffre(request, userDetails.getUsername()));
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<OffreLivraisonResponse>> getDisponibles() {
        return ResponseEntity.ok(service.getOffresDisponibles());
    }

    @GetMapping("/mes-offres")
    public ResponseEntity<List<OffreLivraisonResponse>> getMesOffres(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(service.getMesOffres(userDetails.getUsername()));
    }

    //  FIX 4 : utilise `service` injecté, pas une variable null
    @PatchMapping("/{id}/desactiver")
    public ResponseEntity<?> desactiver(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        service.desactiverOffre(id, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }
}