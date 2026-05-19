package com.covoLiv.backend.service;

import com.covoLiv.backend.dto.ColisRequest;
import com.covoLiv.backend.dto.ColisResponse;
import com.covoLiv.backend.entity.*;
import com.covoLiv.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ColisService {

    private final ColisRepository colisRepo;
    private final UtilisateurRepository utilisateurRepo;
    private final OffreLivraisonRepository offreRepo;

    public ColisResponse creerColis(ColisRequest request, String emailExpediteur) {
        Utilisateur expediteur = utilisateurRepo.findByEmail(emailExpediteur)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        String otp = String.format("%06d", new Random().nextInt(999999));

        Colis.ColisBuilder builder = Colis.builder()
                .description(request.getDescription())
                .poids(request.getPoids())
                .villeDepart(request.getVilleDepart())
                .villeArrivee(request.getVilleArrivee())
                .nomDestinataire(request.getNomDestinataire())
                .telephoneDestinataire(request.getTelephoneDestinataire())
                .codeOTP(otp)
                .statut("EN_ATTENTE")
                .dateCreation(LocalDateTime.now())
                .expediteur(expediteur);

        if (request.getOffreLivraisonId() != null) {
            OffreLivraison offre = offreRepo.findById(request.getOffreLivraisonId())
                    .orElseThrow(() -> new RuntimeException("Offre non trouvée"));
            builder.offreLivraison(offre);
            builder.conducteur(offre.getConducteur());
            float prix = request.getPoids() * offre.getPrixParKg();
            builder.prix(prix);
            builder.statut("PRIX_PROPOSE");
        }

        Colis colis = colisRepo.save(builder.build());
        System.out.println("=== COLIS CRÉÉ === OTP: " + otp);
        return mapToResponse(colis);
    }

    public ColisResponse accepterPrix(Long id, String emailClient) {
        Colis colis = colisRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Colis non trouvé"));

        if (!"PRIX_PROPOSE".equals(colis.getStatut())) {
            throw new RuntimeException("Aucun prix à accepter — statut actuel : " + colis.getStatut());
        }

        colis.setStatut("ACCEPTE");
        colisRepo.save(colis);
        System.out.println("=== PRIX ACCEPTÉ === Colis: " + id);
        return mapToResponse(colis);
    }

    //  FIX 5 : méthode dédiée pour le conducteur qui accepte la contre-offre du client
    public ColisResponse accepterContreOffre(Long id, String emailConducteur) {
        Colis colis = colisRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Colis non trouvé"));

        if (!"CONTRE_OFFRE_CLIENT".equals(colis.getStatut())) {
            throw new RuntimeException("Aucune contre-offre à accepter — statut : " + colis.getStatut());
        }

        colis.setStatut("ACCEPTE");
        colisRepo.save(colis);
        System.out.println("=== CONTRE-OFFRE ACCEPTÉE === Colis: " + id + " · Prix: " + colis.getPrix());
        return mapToResponse(colis);
    }

    public ColisResponse refuserPrix(Long id, String emailClient) {
        Colis colis = colisRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Colis non trouvé"));

        colis.setStatut("EN_ATTENTE");
        colis.setConducteur(null);
        colis.setOffreLivraison(null);
        colis.setPrix(null);
        colisRepo.save(colis);
        return mapToResponse(colis);
    }

    public ColisResponse proposerPrix(Long colisId, Float prix, String emailConducteur) {
        Colis colis = colisRepo.findById(colisId)
                .orElseThrow(() -> new RuntimeException("Colis non trouvé"));

        if (!"EN_ATTENTE".equals(colis.getStatut()) &&
                !"CONTRE_OFFRE_CLIENT".equals(colis.getStatut())) {
            throw new RuntimeException("Ce colis n'est pas disponible — statut : " + colis.getStatut());
        }

        Utilisateur conducteur = utilisateurRepo.findByEmail(emailConducteur)
                .orElseThrow(() -> new RuntimeException("Conducteur non trouvé"));

        if (!(conducteur instanceof Conducteur c) || !Boolean.TRUE.equals(c.getEstVerifie())) {
            throw new RuntimeException("Seul un conducteur vérifié peut proposer un prix");
        }

        colis.setConducteur(conducteur);
        colis.setPrix(prix);
        colis.setStatut("PRIX_PROPOSE");
        colisRepo.save(colis);
        return mapToResponse(colis);
    }

    public ColisResponse demarrerLivraison(Long id, String emailConducteur) {
        Colis colis = colisRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Colis non trouvé"));

        if (!"ACCEPTE".equals(colis.getStatut())) {
            throw new RuntimeException("Le prix doit être accepté avant de démarrer");
        }

        colis.setStatut("EN_TRANSIT");
        colisRepo.save(colis);
        return mapToResponse(colis);
    }

    public ColisResponse validerLivraison(Long id, String otp, String emailConducteur) {
        Colis colis = colisRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Colis non trouvé"));

        if (!colis.getCodeOTP().equals(otp)) {
            throw new RuntimeException("Code OTP incorrect");
        }

        if (!"EN_TRANSIT".equals(colis.getStatut())) {
            throw new RuntimeException("Ce colis n'est pas en transit");
        }

        colis.setStatut("LIVRE");
        colis.setDateLivraison(LocalDateTime.now());
        colisRepo.save(colis);
        return mapToResponse(colis);
    }

    public ColisResponse suivreParOTP(String otp) {
        return mapToResponse(colisRepo.findByCodeOTP(otp)
                .orElseThrow(() -> new RuntimeException("Colis non trouvé")));
    }

    public List<ColisResponse> getMesColis(String email) {
        Utilisateur u = utilisateurRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return colisRepo.findByExpediteur(u)
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public List<ColisResponse> getColisEnAttente() {
        return colisRepo.findByStatut("EN_ATTENTE")
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public List<ColisResponse> getMesLivraisons(String email) {
        Utilisateur u = utilisateurRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return colisRepo.findByConducteur(u)
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    //  Client contre-propose un prix au conducteur
    public ColisResponse contreProposerPrix(Long id, Float prix, String emailClient) {
        Colis colis = colisRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Colis non trouvé"));

        if (!"PRIX_PROPOSE".equals(colis.getStatut())) {
            throw new RuntimeException("Aucune proposition en cours — statut : " + colis.getStatut());
        }

        colis.setPrix(prix);
        colis.setStatut("CONTRE_OFFRE_CLIENT");
        colisRepo.save(colis);
        System.out.println("=== CONTRE-OFFRE CLIENT === " + prix + " MAD");
        return mapToResponse(colis);
    }

    // Conducteur voit toutes les demandes qui le concernent
    public List<ColisResponse> getDemandesParConducteur(String emailConducteur) {
        Utilisateur conducteur = utilisateurRepo.findByEmail(emailConducteur)
                .orElseThrow(() -> new RuntimeException("Conducteur non trouvé"));

        List<Colis> colis = colisRepo.findByOffreLivraison_ConducteurOrConducteur(
                conducteur, conducteur
        );

        return colis.stream()
                .filter(c -> !c.getStatut().equals("LIVRE"))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private ColisResponse mapToResponse(Colis c) {
        ColisResponse r = new ColisResponse();
        r.setId(c.getId());
        r.setDescription(c.getDescription());
        r.setPoids(c.getPoids());
        r.setVilleDepart(c.getVilleDepart());
        r.setVilleArrivee(c.getVilleArrivee());
        r.setNomDestinataire(c.getNomDestinataire());
        r.setTelephoneDestinataire(c.getTelephoneDestinataire());
        r.setPrix(c.getPrix());
        r.setCodeOTP(c.getCodeOTP());
        r.setStatut(c.getStatut());
        r.setDateCreation(c.getDateCreation());
        r.setDateLivraison(c.getDateLivraison());
        if (c.getExpediteur() != null) r.setNomExpediteur(c.getExpediteur().getNom());
        if (c.getConducteur() != null) r.setNomConducteur(c.getConducteur().getNom());
        if (c.getOffreLivraison() != null) r.setOffreLivraisonId(c.getOffreLivraison().getId());
        return r;
    }
}