package com.covoLiv.backend.service;

import com.covoLiv.backend.dto.*;
import com.covoLiv.backend.entity.*;
import com.covoLiv.backend.enums.Role;
import com.covoLiv.backend.repository.UtilisateurRepository;
import com.covoLiv.backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        if (utilisateurRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email déjà utilisé");
        }

        Utilisateur utilisateur;

        if (request.getRole() == Role.CONDUCTEUR) {
            Conducteur conducteur = new Conducteur();
            conducteur.setPermisConduire(request.getPermisConduire());
            conducteur.setPieceIdentite(request.getPieceIdentite());
            conducteur.setMarqueVoiture(request.getMarqueVoiture());
            conducteur.setPhotoVoiture(request.getPhotoVoiture());
            conducteur.setEstVerifie(false);
            conducteur.setStatutVerification("EN_ATTENTE");
            utilisateur = conducteur;
        } else if (request.getRole() == Role.ADMIN) {
            utilisateur = new Administrateur();
        } else {
            utilisateur = new Client();
        }

        utilisateur.setNom(request.getNom());
        utilisateur.setEmail(request.getEmail());
        utilisateur.setMotDePasse(passwordEncoder.encode(request.getMotDePasse()));
        utilisateur.setTelephone(request.getTelephone());
        utilisateur.setRole(request.getRole());
        utilisateur.setEstActif(true);

        utilisateurRepository.save(utilisateur);

        var userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(utilisateur.getEmail())
                .password(utilisateur.getMotDePasse())
                .roles(utilisateur.getRole().name())
                .build();

        String token = jwtService.generateToken(userDetails);

        boolean estVerifie = !(utilisateur instanceof Conducteur)
                || Boolean.TRUE.equals(((Conducteur) utilisateur).getEstVerifie());
        String statut = (utilisateur instanceof Conducteur)
                ? ((Conducteur) utilisateur).getStatutVerification()
                : "VERIFIE";

        return new AuthResponse(
                token,
                utilisateur.getRole().name(),
                utilisateur.getNom(),
                utilisateur.getEmail(),
                utilisateur.getId(),
                estVerifie,
                statut
        );   }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getMotDePasse())
        );

        Utilisateur utilisateur = utilisateurRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        var userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(utilisateur.getEmail())
                .password(utilisateur.getMotDePasse())
                .roles(utilisateur.getRole().name())
                .build();

        String token = jwtService.generateToken(userDetails);

        boolean estVerifie = !(utilisateur instanceof Conducteur)
                || Boolean.TRUE.equals(((Conducteur) utilisateur).getEstVerifie());
        String statut = (utilisateur instanceof Conducteur)
                ? ((Conducteur) utilisateur).getStatutVerification()
                : "VERIFIE";

        return new AuthResponse(
                token,
                utilisateur.getRole().name(),
                utilisateur.getNom(),
                utilisateur.getEmail(),
                utilisateur.getId(),
                estVerifie,
                statut
        );
    }
}