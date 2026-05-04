package com.covoLiv.backend.entity;

import com.covoLiv.backend.enums.Role;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "utilisateurs")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type_utilisateur")
@Getter
@Setter
@NoArgsConstructor
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String motDePasse;

    private String telephone;
    private String photo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private Float noteMoyenne = 0.0f;
    private Boolean estActif = true;
}