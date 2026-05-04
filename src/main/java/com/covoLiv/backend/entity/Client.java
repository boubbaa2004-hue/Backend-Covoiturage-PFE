package com.covoLiv.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@DiscriminatorValue("CLIENT")
public class Client extends Utilisateur {

}