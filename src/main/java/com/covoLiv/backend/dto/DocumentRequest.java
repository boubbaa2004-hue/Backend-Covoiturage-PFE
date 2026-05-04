package com.covoLiv.backend.dto;

import lombok.Data;

@Data
public class DocumentRequest {
    private String permisConduire;
    private String pieceIdentite;
    private String carteGrise;
}