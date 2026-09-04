package com.franquicias.controller;

import com.franquicias.dto.CreateFranquiciaRequest;
import com.franquicias.dto.UpdateNombreRequest;
import com.franquicias.model.Franquicia;
import com.franquicias.service.FranquiciaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/franquicias")
public class FranquiciaController {

    private final FranquiciaService service;

    public FranquiciaController(
            FranquiciaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Franquicia> create(
            @Valid @RequestBody
            CreateFranquiciaRequest request) {

        Franquicia franquicia =
                service.create(request.nombre());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(franquicia);
    }

    @PatchMapping("/{id}")
    public Franquicia updateNombre(
            @PathVariable Long id,
            @Valid @RequestBody
            UpdateNombreRequest request) {

        return service.updateNombre(
                id,
                request.nombre()
        );
    }
}
