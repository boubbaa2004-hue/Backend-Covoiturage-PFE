package com.covoLiv.backend.controller;

import com.covoLiv.backend.dto.ColisRequest;
import com.covoLiv.backend.dto.ColisResponse;
import com.covoLiv.backend.service.ColisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/colis")
@RequiredArgsConstructor
public class ColisController {

    private final ColisService colisService;

    // Client crée un colis
    @PostMapping
    public ResponseEntity<ColisResponse> creer(
            @RequestBody ColisRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(colisService.creerColis(request, userDetails.getUsername()));
    }

    // Suivi public par OTP
    @GetMapping("/suivi/{otp}")
    public ResponseEntity<ColisResponse> suivre(@PathVariable String otp) {
        return ResponseEntity.ok(colisService.suivreParOTP(otp));
    }

    // Client voit ses colis
    @GetMapping("/mes-colis")
    public ResponseEntity<List<ColisResponse>> getMesColis(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(colisService.getMesColis(userDetails.getUsername()));
    }

    // Client accepte le prix
    @PatchMapping("/{id}/accepter-prix")
    public ResponseEntity<ColisResponse> accepterPrix(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(colisService.accepterPrix(id, userDetails.getUsername()));
    }

    // Client refuse le prix
    @PatchMapping("/{id}/refuser-prix")
    public ResponseEntity<ColisResponse> refuserPrix(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(colisService.refuserPrix(id, userDetails.getUsername()));
    }

    // Conducteur voit les colis en attente
    @GetMapping("/en-attente")
    public ResponseEntity<List<ColisResponse>> getEnAttente() {
        return ResponseEntity.ok(colisService.getColisEnAttente());
    }

    // Conducteur propose un prix
    @PatchMapping("/{id}/proposer-prix")
    public ResponseEntity<ColisResponse> proposerPrix(
            @PathVariable Long id,
            @RequestBody Map<String, Float> body,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(colisService.proposerPrix(id, body.get("prix"), userDetails.getUsername()));
    }

    // Conducteur démarre la livraison
    @PatchMapping("/{id}/demarrer")
    public ResponseEntity<ColisResponse> demarrer(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(colisService.demarrerLivraison(id, userDetails.getUsername()));
    }

    // Conducteur valide la livraison avec OTP
    @PatchMapping("/{id}/valider-livraison")
    public ResponseEntity<ColisResponse> valider(
            @PathVariable Long id,
            @RequestParam String otp,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(colisService.validerLivraison(id, otp, userDetails.getUsername()));
    }

    // Conducteur voit ses livraisons
    @GetMapping("/mes-livraisons")
    public ResponseEntity<List<ColisResponse>> getMesLivraisons(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(colisService.getMesLivraisons(userDetails.getUsername()));
    }
    // Client contre-propose
    @PatchMapping("/{id}/contre-proposer")
    public ResponseEntity<ColisResponse> contreProposer(
            @PathVariable Long id,
            @RequestBody Map<String, Float> body,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                colisService.contreProposerPrix(id, body.get("prix"), userDetails.getUsername())
        );
    }
    @PatchMapping("/{id}/accepter-contre-offre")
    public ResponseEntity<ColisResponse> accepterContreOffre(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(colisService.accepterPrix(id, userDetails.getUsername()));
    }
    @GetMapping("/mes-demandes")
    public ResponseEntity<List<ColisResponse>> getMesDemandes(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(colisService.getDemandesParConducteur(userDetails.getUsername()));
    }
}