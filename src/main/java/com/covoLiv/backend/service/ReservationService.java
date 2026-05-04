package com.covoLiv.backend.service;

import com.covoLiv.backend.dto.ReservationRequest;
import com.covoLiv.backend.dto.ReservationResponse;
import com.covoLiv.backend.entity.Reservation;
import com.covoLiv.backend.entity.Trajet;
import com.covoLiv.backend.entity.Utilisateur;
import com.covoLiv.backend.repository.ReservationRepository;
import com.covoLiv.backend.repository.TrajetRepository;
import com.covoLiv.backend.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TrajetRepository trajetRepository;
    private final UtilisateurRepository utilisateurRepository;

    public ReservationResponse creerReservation(ReservationRequest request, String emailClient) {
        Utilisateur client = utilisateurRepository.findByEmail(emailClient)
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));

        Trajet trajet = trajetRepository.findById(request.getTrajetId())
                .orElseThrow(() -> new RuntimeException("Trajet non trouvé"));

        if (trajet.getPlacesDisponibles() < request.getNbPlaces()) {
            throw new RuntimeException("Pas assez de places disponibles");
        }

        float prixTotal = trajet.getPrixParPlace() * request.getNbPlaces();

        Reservation reservation = Reservation.builder()
                .dateReservation(LocalDateTime.now())
                .nbPlaces(request.getNbPlaces())
                .prixTotal(prixTotal)
                .statut("EN_ATTENTE")
                .client(client)
                .trajet(trajet)
                .build();

        trajet.setPlacesDisponibles(trajet.getPlacesDisponibles() - request.getNbPlaces());
        trajetRepository.save(trajet);
        reservationRepository.save(reservation);

        return mapToResponse(reservation);
    }

    public List<ReservationResponse> getMesReservations(String emailClient) {
        Utilisateur client = utilisateurRepository.findByEmail(emailClient)
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));
        return reservationRepository.findByClient(client)
                .stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ReservationResponse confirmerReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));
        reservation.setStatut("CONFIRME");
        reservationRepository.save(reservation);
        return mapToResponse(reservation);
    }

    public ReservationResponse annulerReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));

        Trajet trajet = reservation.getTrajet();
        trajet.setPlacesDisponibles(trajet.getPlacesDisponibles() + reservation.getNbPlaces());
        trajetRepository.save(trajet);

        reservation.setStatut("ANNULE");
        reservationRepository.save(reservation);
        return mapToResponse(reservation);
    }

    public List<ReservationResponse> getReservationsTrajet(Long trajetId) {
        Trajet trajet = trajetRepository.findById(trajetId)
                .orElseThrow(() -> new RuntimeException("Trajet non trouvé"));
        return reservationRepository.findByTrajet(trajet)
                .stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private ReservationResponse mapToResponse(Reservation r) {
        ReservationResponse response = new ReservationResponse();
        response.setId(r.getId());
        response.setDateReservation(r.getDateReservation());
        response.setNbPlaces(r.getNbPlaces());
        response.setPrixTotal(r.getPrixTotal());
        response.setStatut(r.getStatut());
        response.setNomClient(r.getClient().getNom());
        response.setVilleDepart(r.getTrajet().getVilleDepart());
        response.setVilleArrivee(r.getTrajet().getVilleArrivee());
        response.setDateHeure(r.getTrajet().getDateHeure());
        response.setNomConducteur(r.getTrajet().getConducteur().getNom());
        return response;
    }
}