package com.covoLiv.backend.controller;

import com.covoLiv.backend.dto.ColisRequest;
import com.covoLiv.backend.dto.ColisResponse;
import com.covoLiv.backend.service.ColisService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/colis")
@RequiredArgsConstructor
public class ColisController {

    private final ColisService colisService;

    @PostMapping
    public ResponseEntity<ColisResponse> creerColis(
            @Valid @RequestBody ColisRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(colisService.creerColis(request, userDetails.getUsername()));
    }

    @GetMapping("/mes-colis")
    public ResponseEntity<List<ColisResponse>> getMesColis(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(colisService.getMesColis(userDetails.getUsername()));
    }

    @GetMapping("/suivi/{otp}")
    public ResponseEntity<ColisResponse> suivreParOTP(@PathVariable String otp) {
        return ResponseEntity.ok(colisService.suivreParOTP(otp));
    }

    @PatchMapping("/valider/{otp}")
    public ResponseEntity<ColisResponse> validerLivraison(@PathVariable String otp) {
        return ResponseEntity.ok(colisService.validerLivraison(otp));
    }
}