package com.covoLiv.backend.service;

import com.covoLiv.backend.dto.*;
import com.covoLiv.backend.entity.*;
import com.covoLiv.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LitigeService {

    private final LitigeRepository litigeRepository;
    private final UtilisateurRepository utilisateurRepository;

    public LitigeResponse creerLitige(LitigeRequest request, String emailPlaignant) {
        Utilisateur plaignant = utilisateurRepository.findByEmail(emailPlaignant)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        Utilisateur accuse = utilisateurRepository.findById(request.getAccuseId())
                .orElseThrow(() -> new RuntimeException("Accusé non trouvé"));

        Litige litige = Litige.builder()
                .type(request.getType())
                .description(request.getDescription())
                .statut("OUVERT")
                .dateCreation(LocalDateTime.now())
                .plaignant(plaignant)
                .accuse(accuse)
                .build();

        litigeRepository.save(litige);
        return mapToResponse(litige);
    }

    public List<LitigeResponse> getTousLesLitiges() {
        return litigeRepository.findAll()
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public List<LitigeResponse> getLitigesOuverts() {
        return litigeRepository.findByStatut("OUVERT")
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public LitigeResponse resoudreLitige(Long id) {
        Litige litige = litigeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Litige non trouvé"));
        litige.setStatut("RESOLU");
        litige.setDateResolution(LocalDateTime.now());
        litigeRepository.save(litige);
        return mapToResponse(litige);
    }

    public LitigeResponse mettreEnCours(Long id) {
        Litige litige = litigeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Litige non trouvé"));
        litige.setStatut("EN_COURS");
        litigeRepository.save(litige);
        return mapToResponse(litige);
    }

    private LitigeResponse mapToResponse(Litige l) {
        LitigeResponse r = new LitigeResponse();
        r.setId(l.getId());
        r.setType(l.getType());
        r.setDescription(l.getDescription());
        r.setStatut(l.getStatut());
        r.setDateCreation(l.getDateCreation());
        r.setDateResolution(l.getDateResolution());
        r.setNomPlaignant(l.getPlaignant().getNom());
        r.setNomAccuse(l.getAccuse().getNom());
        return r;
    }
}