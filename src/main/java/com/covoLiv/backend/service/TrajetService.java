package com.covoLiv.backend.service;

import com.covoLiv.backend.dto.TrajetRequest;
import com.covoLiv.backend.dto.TrajetResponse;
import com.covoLiv.backend.entity.Conducteur;
import com.covoLiv.backend.entity.Trajet;
import com.covoLiv.backend.entity.Utilisateur;
import com.covoLiv.backend.repository.TrajetRepository;
import com.covoLiv.backend.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrajetService {

    private final TrajetRepository trajetRepository;
    private final UtilisateurRepository utilisateurRepository;

    public TrajetResponse creerTrajet(TrajetRequest request, String emailConducteur) {
        Utilisateur conducteur = utilisateurRepository.findByEmail(emailConducteur)
                .orElseThrow(() -> new RuntimeException("Conducteur non trouvé"));
// Vérification que le conducteur est validé
        if (conducteur instanceof Conducteur c) {
            if (!Boolean.TRUE.equals(c.getEstVerifie())) {
                throw new RuntimeException("Votre compte n'est pas encore vérifié. Attendez la validation de l'administrateur.");
            }
        }
        Trajet trajet = Trajet.builder()
                .villeDepart(request.getVilleDepart())
                .villeArrivee(request.getVilleArrivee())
                .dateHeure(request.getDateHeure())
                .placesDisponibles(request.getPlacesDisponibles())
                .volumeCoffre(request.getVolumeCoffre())
                .prixParPlace(request.getPrixParPlace())
                .description(request.getDescription())
                .statut("ACTIF")
                .conducteur(conducteur)
                .build();

        trajetRepository.save(trajet);
        return mapToResponse(trajet);
    }

    public List<TrajetResponse> getTousLesTrajets() {
        return trajetRepository.findByStatut("ACTIF")
                .stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<TrajetResponse> rechercherTrajets(String villeDepart, String villeArrivee) {
        return trajetRepository.findByVilleDepartAndVilleArriveeAndStatut(villeDepart, villeArrivee, "ACTIF")
                .stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public TrajetResponse getTrajetById(Long id) {
        Trajet trajet = trajetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trajet non trouvé"));
        return mapToResponse(trajet);
    }

    public List<TrajetResponse> getMesTrajets(String emailConducteur) {
        Utilisateur conducteur = utilisateurRepository.findByEmail(emailConducteur)
                .orElseThrow(() -> new RuntimeException("Conducteur non trouvé"));
        return trajetRepository.findByConducteur(conducteur)
                .stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public void annulerTrajet(Long id) {
        Trajet trajet = trajetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trajet non trouvé"));
        trajet.setStatut("ANNULE");
        trajetRepository.save(trajet);
    }

    private TrajetResponse mapToResponse(Trajet t) {
        TrajetResponse r = new TrajetResponse();
        r.setId(t.getId());
        r.setVilleDepart(t.getVilleDepart());
        r.setVilleArrivee(t.getVilleArrivee());
        r.setDateHeure(t.getDateHeure());
        r.setPlacesDisponibles(t.getPlacesDisponibles());
        r.setPrixParPlace(t.getPrixParPlace());
        r.setVolumeCoffre(t.getVolumeCoffre());
        r.setDescription(t.getDescription());
        r.setStatut(t.getStatut());

        if (t.getConducteur() != null) {
            r.setConducteurId(t.getConducteur().getId());
            r.setNomConducteur(t.getConducteur().getNom());
            r.setNoteConducteur(t.getConducteur().getNoteMoyenne());

            // Ajoute photo voiture et marque
            if (t.getConducteur() instanceof com.covoLiv.backend.entity.Conducteur c) {
                r.setPhotoVoiture(c.getPhotoVoiture());
                r.setMarqueVoiture(c.getMarqueVoiture());
                r.setPhotoProfile(c.getPhotoProfile());
            }
        }
        return r;
    }
}
