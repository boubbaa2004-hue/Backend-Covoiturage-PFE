package com.covoLiv.backend.controller;

import com.covoLiv.backend.entity.Conducteur;
import com.covoLiv.backend.entity.Utilisateur;
import com.covoLiv.backend.repository.UtilisateurRepository;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/conducteurs")
@RequiredArgsConstructor
//@Tag(name = "Conducteur APIS")
public class ConducteurController {

    private final UtilisateurRepository utilisateurRepository;

    @GetMapping("/moi")
//    @Operation(summary = "get a user Detais")
    public ResponseEntity<?> getMoi(@AuthenticationPrincipal UserDetails userDetails) {
        Utilisateur u = utilisateurRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Non trouvé"));

        if (u instanceof Conducteur c) {
            Map<String, Object> response = new HashMap<>();
            response.put("id", c.getId());
            response.put("nom", c.getNom());
            response.put("email", c.getEmail());
            response.put("telephone", c.getTelephone() != null ? c.getTelephone() : "");
            response.put("photoProfile", c.getPhotoProfile() != null ? c.getPhotoProfile() : "");
            response.put("photoVoiture", c.getPhotoVoiture() != null ? c.getPhotoVoiture() : "");
            response.put("marqueVoiture", c.getMarqueVoiture() != null ? c.getMarqueVoiture() : "");
            response.put("estVerifie", c.getEstVerifie() != null ? c.getEstVerifie() : false);
            response.put("statutVerification", c.getStatutVerification() != null ? c.getStatutVerification() : "EN_ATTENTE");
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().build();
    }
}