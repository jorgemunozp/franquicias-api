package com.franquicias.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateFranquiciaRequest(
        @NotBlank
        @Size(max = 150)
        String nombre
) {}
