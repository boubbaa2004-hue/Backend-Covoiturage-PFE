package com.covoLiv.backend.controller;

import com.covoLiv.backend.dto.*;
import com.covoLiv.backend.service.LitigeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/litiges")
@RequiredArgsConstructor
public class LitigeController {

    private final LitigeService litigeService;

    @PostMapping
    public ResponseEntity<LitigeResponse> creer(
            @Valid @RequestBody LitigeRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(litigeService.creerLitige(request, userDetails.getUsername()));
    }

    @GetMapping
    public ResponseEntity<List<LitigeResponse>> getTous() {
        return ResponseEntity.ok(litigeService.getTousLesLitiges());
    }

    @GetMapping("/ouverts")
    public ResponseEntity<List<LitigeResponse>> getOuverts() {
        return ResponseEntity.ok(litigeService.getLitigesOuverts());
    }

    @PatchMapping("/{id}/resoudre")
    public ResponseEntity<LitigeResponse> resoudre(@PathVariable Long id) {
        return ResponseEntity.ok(litigeService.resoudreLitige(id));
    }

    @PatchMapping("/{id}/en-cours")
    public ResponseEntity<LitigeResponse> mettreEnCours(@PathVariable Long id) {
        return ResponseEntity.ok(litigeService.mettreEnCours(id));
    }
}