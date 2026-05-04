package com.covoLiv.backend.controller;

import com.covoLiv.backend.dto.*;
import com.covoLiv.backend.service.EvaluationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/evaluations")
@RequiredArgsConstructor
public class EvaluationController {

    private final EvaluationService evaluationService;

    @PostMapping
    public ResponseEntity<EvaluationResponse> evaluer(
            @Valid @RequestBody EvaluationRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(evaluationService.creerEvaluation(request, userDetails.getUsername()));
    }

    @GetMapping("/utilisateur/{id}")
    public ResponseEntity<List<EvaluationResponse>> getEvaluations(@PathVariable Long id) {
        return ResponseEntity.ok(evaluationService.getEvaluationsUtilisateur(id));
    }
}