package com.covoLiv.backend.dto;

import com.covoLiv.backend.enums.Role;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Email invalide")
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    private String motDePasse;

    private String telephone;

    @NotNull(message = "Le rôle est obligatoire")
    private Role role;

    // Conducteur uniquement
    private String permisConduire;
    private String pieceIdentite;
    private String marqueVoiture;
    private String photoVoiture;
}