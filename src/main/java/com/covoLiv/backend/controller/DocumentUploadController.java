package com.covoLiv.backend.controller;

import com.covoLiv.backend.entity.Conducteur;
import com.covoLiv.backend.entity.Utilisateur;
import com.covoLiv.backend.repository.UtilisateurRepository;
import com.covoLiv.backend.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.covoLiv.backend.dto.ConducteurResponse;

import java.util.List;
import java.util.stream.Collectors;
import java.io.IOException;
import java.nio.file.*;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentUploadController {

    private final FileStorageService fileStorageService;
    private final UtilisateurRepository utilisateurRepository;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadDocuments(
            @RequestParam(value = "permis", required = false) MultipartFile permis,
            @RequestParam(value = "cin", required = false) MultipartFile cin,
            @RequestParam(value = "photoVoiture", required = false) MultipartFile photoVoiture,
            @RequestParam(value = "carteGrise", required = false) MultipartFile carteGrise,
            @RequestParam(value = "marqueVoiture", required = false) String marqueVoiture,
            @AuthenticationPrincipal UserDetails userDetails) {

        try {
            Utilisateur u = utilisateurRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

            if (!(u instanceof Conducteur c)) {
                return ResponseEntity.badRequest().body("Seul un conducteur peut uploader des documents");
            }

            if (permis != null && !permis.isEmpty()) {
                String nom = fileStorageService.sauvegarderFichier(permis, "permis_" + u.getId());
                c.setPermisConduire(nom);
            }
            if (cin != null && !cin.isEmpty()) {
                String nom = fileStorageService.sauvegarderFichier(cin, "cin_" + u.getId());
                c.setPieceIdentite(nom);
            }
            if (photoVoiture != null && !photoVoiture.isEmpty()) {
                String nom = fileStorageService.sauvegarderFichier(photoVoiture, "voiture_" + u.getId());
                c.setPhotoVoiture(nom);
            }
            if (carteGrise != null && !carteGrise.isEmpty()) {
                String nom = fileStorageService.sauvegarderFichier(carteGrise, "carte_grise_" + u.getId());
                c.setCarteGrise(nom);
            }
            if (marqueVoiture != null) {
                c.setMarqueVoiture(marqueVoiture);
            }

            c.setStatutVerification("EN_ATTENTE");
            utilisateurRepository.save(c);

            return ResponseEntity.ok("Documents soumis avec succès");

        } catch (IOException e) {
            return ResponseEntity.status(500).body("Erreur: " + e.getMessage());
        }
    }

    @GetMapping("/fichier/{nomFichier}")
    public ResponseEntity<byte[]> getFichier(@PathVariable String nomFichier) {
        try {
            System.out.println("=== GET FICHIER === " + nomFichier);
            byte[] contenu = fileStorageService.lireFichier(nomFichier);

            MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
            String nomLower = nomFichier.toLowerCase();
            if (nomLower.endsWith(".jpg") || nomLower.endsWith(".jpeg")) {
                mediaType = MediaType.IMAGE_JPEG;
            } else if (nomLower.endsWith(".png")) {
                mediaType = MediaType.IMAGE_PNG;
            } else if (nomLower.endsWith(".pdf")) {
                mediaType = MediaType.APPLICATION_PDF;
            }

            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + nomFichier + "\"")
                    .header("Access-Control-Allow-Origin", "*")
                    .body(contenu);
        } catch (IOException e) {
            System.out.println("=== ERREUR FICHIER === " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/en-attente")
    public ResponseEntity<List<ConducteurResponse>> getConducteursEnAttente() {
        List<ConducteurResponse> liste = utilisateurRepository.findAll().stream()
                .filter(u -> u instanceof Conducteur)
                .filter(u -> "EN_ATTENTE".equals(((Conducteur) u).getStatutVerification()))
                .map(u -> {
                    Conducteur c = (Conducteur) u;
                    ConducteurResponse r = new ConducteurResponse();
                    r.setId(c.getId());
                    r.setNom(c.getNom());
                    r.setEmail(c.getEmail());
                    r.setTelephone(c.getTelephone());
                    r.setPermisConduire(c.getPermisConduire());
                    r.setPieceIdentite(c.getPieceIdentite());
                    r.setPhotoVoiture(c.getPhotoVoiture());
                    r.setCarteGrise(c.getCarteGrise());
                    r.setMarqueVoiture(c.getMarqueVoiture());
                    r.setEstVerifie(c.getEstVerifie());
                    r.setStatutVerification(c.getStatutVerification());
                    r.setEstActif(c.getEstActif());
                    return r;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(liste);
    }

    @PatchMapping("/{id}/valider")
    public ResponseEntity<Void> valider(@PathVariable Long id) {
        Utilisateur u = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Non trouvé"));
        if (u instanceof Conducteur c) {
            c.setEstVerifie(true);
            c.setStatutVerification("VERIFIE");
            utilisateurRepository.save(c);
        }
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/rejeter")
    public ResponseEntity<Void> rejeter(@PathVariable Long id) {
        Utilisateur u = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Non trouvé"));
        if (u instanceof Conducteur c) {
            c.setEstVerifie(false);
            c.setStatutVerification("REJETE");
            utilisateurRepository.save(c);
        }
        return ResponseEntity.ok().build();
    }
}