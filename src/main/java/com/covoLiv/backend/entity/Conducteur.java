package com.covoLiv.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@DiscriminatorValue("CONDUCTEUR")
@Getter
@Setter
public class Conducteur extends Utilisateur {

    private String permisConduire;
    private String pieceIdentite;
    private String marqueVoiture;
    private String photoVoiture;
    private Boolean estVerifie = false;
    private String statutVerification = "EN_ATTENTE";
    private String carteGrise;
    private String photoProfile;
}