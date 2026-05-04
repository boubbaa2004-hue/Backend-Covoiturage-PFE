package com.covoLiv.backend.controller;

import com.covoLiv.backend.dto.NegociationRequest;
import com.covoLiv.backend.dto.NegociationResponse;
import com.covoLiv.backend.service.NegociationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/negociations")
@RequiredArgsConstructor
public class NegociationController {

    private final NegociationService negociationService;

    @PostMapping
    public ResponseEntity<NegociationResponse> creerNegociation(
            @Valid @RequestBody NegociationRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(negociationService.creerNegociation(request, userDetails.getUsername()));
    }

    @GetMapping("/mes-negociations")
    public ResponseEntity<List<NegociationResponse>> getMesNegociations(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(negociationService.getMesNegociations(userDetails.getUsername()));
    }

    @PatchMapping("/{id}/repondre")
    public ResponseEntity<NegociationResponse> repondre(
            @PathVariable Long id,
            @RequestParam(required = false) Float offreConducteur,
            @RequestParam String decision) {
        return ResponseEntity.ok(negociationService.repondreNegociation(id, offreConducteur, decision));
    }

    @GetMapping("/trajet/{trajetId}")
    public ResponseEntity<List<NegociationResponse>> getNegociationsTrajet(
            @PathVariable Long trajetId) {
        return ResponseEntity.ok(negociationService.getNegociationsTrajet(trajetId));
    }
}