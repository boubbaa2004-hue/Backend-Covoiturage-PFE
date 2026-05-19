package com.covoLiv.backend.service;

import com.covoLiv.backend.dto.OffreLivraisonRequest;
import com.covoLiv.backend.dto.OffreLivraisonResponse;
import com.covoLiv.backend.entity.Conducteur;
import com.covoLiv.backend.entity.OffreLivraison;
import com.covoLiv.backend.entity.Utilisateur;
import com.covoLiv.backend.repository.OffreLivraisonRepository;
import com.covoLiv.backend.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OffreLivraisonService {

    private final OffreLivraisonRepository offreRepo;
    private final UtilisateurRepository utilisateurRepo;

    public OffreLivraisonResponse publierOffre(
            OffreLivraisonRequest request, String emailConducteur) {

        Utilisateur u = utilisateurRepo.findByEmail(emailConducteur)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (!(u instanceof Conducteur c) || !Boolean.TRUE.equals(c.getEstVerifie())) {
            throw new RuntimeException("Seul un conducteur vérifié peut publier une offre");
        }

        OffreLivraison offre = OffreLivraison.builder()
                .villeDepart(request.getVilleDepart())
                .villeArrivee(request.getVilleArrivee())
                .prixParKg(request.getPrixParKg())
                .poidsMax(request.getPoidsMax())
                .description(request.getDescription())
                .statut("ACTIF")
                .dateCreation(LocalDateTime.now())
                .conducteur(u)
                .build();

        offreRepo.save(offre);
        return mapToResponse(offre);
    }

    public List<OffreLivraisonResponse> getOffresDisponibles() {
        return offreRepo.findByStatut("ACTIF")
                .stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<OffreLivraisonResponse> getMesOffres(String emailConducteur) {
        Utilisateur u = utilisateurRepo.findByEmail(emailConducteur)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return offreRepo.findByConducteur(u)
                .stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public void desactiverOffre(Long id, String email) {
        OffreLivraison offre = offreRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Offre non trouvée"));
        offre.setStatut("INACTIF");
        offreRepo.save(offre);
        offre.setPoidsRestant(offre.getPoidsMax());
    }

    private OffreLivraisonResponse mapToResponse(OffreLivraison o) {
        OffreLivraisonResponse r = new OffreLivraisonResponse();
        r.setId(o.getId());
        r.setVilleDepart(o.getVilleDepart());
        r.setVilleArrivee(o.getVilleArrivee());
        r.setPrixParKg(o.getPrixParKg());
        r.setPoidsMax(o.getPoidsMax());
        r.setDescription(o.getDescription());
        r.setStatut(o.getStatut());
        r.setDateCreation(o.getDateCreation());
        if (o.getConducteur() != null) {
            r.setConducteurId(o.getConducteur().getId());
            r.setNomConducteur(o.getConducteur().getNom());
            if (o.getConducteur() instanceof Conducteur c) {
                r.setPhotoProfile(c.getPhotoProfile());
                r.setMarqueVoiture(c.getMarqueVoiture());
                r.setNoteConducteur(c.getNoteMoyenne());
            }
        }
        return r;
    }
}