package com.covoLiv.backend.service;

import com.covoLiv.backend.dto.NegociationRequest;
import com.covoLiv.backend.dto.NegociationResponse;
import com.covoLiv.backend.entity.Negociation;
import com.covoLiv.backend.entity.Trajet;
import com.covoLiv.backend.entity.Utilisateur;
import com.covoLiv.backend.repository.NegociationRepository;
import com.covoLiv.backend.repository.TrajetRepository;
import com.covoLiv.backend.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NegociationService {

    private final NegociationRepository negociationRepository;
    private final TrajetRepository trajetRepository;
    private final UtilisateurRepository utilisateurRepository;

    public NegociationResponse creerNegociation(NegociationRequest request, String emailClient) {
        Utilisateur client = utilisateurRepository.findByEmail(emailClient)
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));

        Trajet trajet = trajetRepository.findById(request.getTrajetId())
                .orElseThrow(() -> new RuntimeException("Trajet non trouvé"));

        Negociation negociation = Negociation.builder()
                .offreClient(request.getOffreClient())
                .statut("EN_COURS")
                .dateCreation(LocalDateTime.now())
                .client(client)
                .trajet(trajet)
                .build();

        negociationRepository.save(negociation);
        return mapToResponse(negociation);
    }

    public NegociationResponse repondreNegociation(Long id, Float offreConducteur, String decision) {
        Negociation negociation = negociationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Négociation non trouvée"));

        if (decision.equals("ACCEPTE")) {
            negociation.setStatut("ACCEPTE");
        } else if (decision.equals("REFUSE")) {
            negociation.setStatut("REFUSE");
        } else {
            negociation.setOffreConducteur(offreConducteur);
            negociation.setStatut("EN_COURS");
        }

        negociationRepository.save(negociation);
        return mapToResponse(negociation);
    }

    public List<NegociationResponse> getMesNegociations(String emailClient) {
        Utilisateur client = utilisateurRepository.findByEmail(emailClient)
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));
        return negociationRepository.findByClient(client)
                .stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<NegociationResponse> getNegociationsTrajet(Long trajetId) {
        Trajet trajet = trajetRepository.findById(trajetId)
                .orElseThrow(() -> new RuntimeException("Trajet non trouvé"));
        return negociationRepository.findByTrajet(trajet)
                .stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private NegociationResponse mapToResponse(Negociation n) {
        NegociationResponse response = new NegociationResponse();
        response.setId(n.getId());
        response.setOffreClient(n.getOffreClient());
        response.setOffreConducteur(n.getOffreConducteur());
        response.setStatut(n.getStatut());
        response.setDateCreation(n.getDateCreation());
        response.setNomClient(n.getClient().getNom());
        response.setVilleDepart(n.getTrajet().getVilleDepart());
        response.setVilleArrivee(n.getTrajet().getVilleArrivee());
        response.setTrajetId(n.getTrajet().getId());
        return response;
    }
}