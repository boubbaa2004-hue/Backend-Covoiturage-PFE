package com.covoLiv.backend.service;

import com.covoLiv.backend.dto.ColisRequest;
import com.covoLiv.backend.dto.ColisResponse;
import com.covoLiv.backend.entity.Colis;
import com.covoLiv.backend.entity.Utilisateur;
import com.covoLiv.backend.repository.ColisRepository;
import com.covoLiv.backend.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ColisService {

    private final ColisRepository colisRepository;
    private final UtilisateurRepository utilisateurRepository;

    public ColisResponse creerColis(ColisRequest request, String emailExpediteur) {
        Utilisateur expediteur = utilisateurRepository.findByEmail(emailExpediteur)
                .orElseThrow(() -> new RuntimeException("Expéditeur non trouvé"));

        String otp = genererOTP();

        Colis colis = Colis.builder()
                .description(request.getDescription())
                .poids(request.getPoids())
                .villeDepart(request.getVilleDepart())
                .villeArrivee(request.getVilleArrivee())
                .nomDestinataire(request.getNomDestinataire())
                .telephoneDestinataire(request.getTelephoneDestinataire())
                .prix(request.getPrix())
                .codeOTP(otp)
                .statut("EN_ATTENTE")
                .dateCreation(LocalDateTime.now())
                .expediteur(expediteur)
                .build();

        colisRepository.save(colis);
        return mapToResponse(colis);
    }

    public ColisResponse validerLivraison(String codeOTP) {
        Colis colis = colisRepository.findByCodeOTP(codeOTP)
                .orElseThrow(() -> new RuntimeException("Code OTP invalide"));

        if (!colis.getStatut().equals("EN_TRANSIT")) {
            throw new RuntimeException("Le colis n'est pas en transit");
        }

        colis.setStatut("LIVRE");
        colis.setDateLivraison(LocalDateTime.now());
        colisRepository.save(colis);
        return mapToResponse(colis);
    }

    public List<ColisResponse> getMesColis(String emailExpediteur) {
        Utilisateur expediteur = utilisateurRepository.findByEmail(emailExpediteur)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return colisRepository.findByExpediteur(expediteur)
                .stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ColisResponse suivreParOTP(String otp) {
        Colis colis = colisRepository.findByCodeOTP(otp)
                .orElseThrow(() -> new RuntimeException("Code OTP invalide"));
        return mapToResponse(colis);
    }

    private String genererOTP() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    private ColisResponse mapToResponse(Colis colis) {
        ColisResponse response = new ColisResponse();
        response.setId(colis.getId());
        response.setDescription(colis.getDescription());
        response.setPoids(colis.getPoids());
        response.setVilleDepart(colis.getVilleDepart());
        response.setVilleArrivee(colis.getVilleArrivee());
        response.setNomDestinataire(colis.getNomDestinataire());
        response.setTelephoneDestinataire(colis.getTelephoneDestinataire());
        response.setCodeOTP(colis.getCodeOTP());
        response.setStatut(colis.getStatut());
        response.setPrix(colis.getPrix());
        response.setDateCreation(colis.getDateCreation());
        response.setNomExpediteur(colis.getExpediteur().getNom());
        if (colis.getConducteur() != null) {
            response.setNomConducteur(colis.getConducteur().getNom());
        }
        return response;
    }
}