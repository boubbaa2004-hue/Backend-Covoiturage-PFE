package com.covoLiv.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@DiscriminatorValue("ADMIN")
public class Administrateur extends Utilisateur {

}