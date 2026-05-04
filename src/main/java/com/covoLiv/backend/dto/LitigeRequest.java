package com.covoLiv.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LitigeRequest {
    @NotBlank
    private String type;

    private String description;

    @NotNull
    private Long accuseId;
}