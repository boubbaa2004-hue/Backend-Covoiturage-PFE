package com.covoLiv.backend.controller;

import com.covoLiv.backend.dto.ReservationRequest;
import com.covoLiv.backend.dto.ReservationResponse;
import com.covoLiv.backend.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationResponse> creerReservation(
            @Valid @RequestBody ReservationRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(reservationService.creerReservation(request, userDetails.getUsername()));
    }

    @GetMapping("/mes-reservations")
    public ResponseEntity<List<ReservationResponse>> getMesReservations(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(reservationService.getMesReservations(userDetails.getUsername()));
    }

    @PatchMapping("/{id}/confirmer")
    public ResponseEntity<ReservationResponse> confirmer(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.confirmerReservation(id));
    }

    @PatchMapping("/{id}/annuler")
    public ResponseEntity<ReservationResponse> annuler(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.annulerReservation(id));
    }

    @GetMapping("/trajet/{trajetId}")
    public ResponseEntity<List<ReservationResponse>> getReservationsTrajet(@PathVariable Long trajetId) {
        return ResponseEntity.ok(reservationService.getReservationsTrajet(trajetId));
    }
}