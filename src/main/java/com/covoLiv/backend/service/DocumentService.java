package com.covoLiv.backend.service;

import com.covoLiv.backend.dto.DocumentRequest;
import com.covoLiv.backend.entity.Conducteur;
import com.covoLiv.backend.entity.Utilisateur;
import com.covoLiv.backend.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final UtilisateurRepository utilisateurRepository;

    public void soumettreDocuments(String email, DocumentRequest request) {
        Utilisateur u = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (u instanceof Conducteur c) {
            c.setPermisConduire(request.getPermisConduire());
            c.setPieceIdentite(request.getPieceIdentite());
            c.setCarteGrise(request.getCarteGrise());
            c.setStatutVerification("EN_ATTENTE");
            utilisateurRepository.save(c);
        } else {
            throw new RuntimeException("Seul un conducteur peut soumettre des documents");
        }
    }

    public List<Utilisateur> getConducteursEnAttente() {
        return utilisateurRepository.findAll().stream()
                .filter(u -> u instanceof Conducteur)
                .filter(u -> {
                    Conducteur c = (Conducteur) u;
                    return "EN_ATTENTE".equals(c.getStatutVerification());
                })
                .collect(Collectors.toList());
    }

    public void validerConducteur(Long id) {
        Utilisateur u = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conducteur non trouvé"));
        if (u instanceof Conducteur c) {
            c.setEstVerifie(true);
            c.setStatutVerification("VERIFIE");
            utilisateurRepository.save(c);
        }
    }

    public void rejeterConducteur(Long id) {
        Utilisateur u = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conducteur non trouvé"));
        if (u instanceof Conducteur c) {
            c.setEstVerifie(false);
            c.setStatutVerification("REJETE");
            utilisateurRepository.save(c);
        }
    }
}