package com.franquicias.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateProductoRequest(
        @NotBlank
        @Size(max = 150)
        String nombre,

        @NotNull
        @Min(0)
        Integer stock
) {}
