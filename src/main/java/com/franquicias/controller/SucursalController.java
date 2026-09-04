package com.franquicias.controller;

import com.franquicias.dto.CreateSucursalRequest;
import com.franquicias.dto.UpdateNombreRequest;
import com.franquicias.model.Sucursal;
import com.franquicias.service.SucursalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SucursalController {

    private final SucursalService service;

    public SucursalController(
            SucursalService service) {
        this.service = service;
    }

    @PostMapping("/franquicias/{franquiciaId}/sucursales")
    public ResponseEntity<Sucursal> create(
            @PathVariable Long franquiciaId,
            @Valid @RequestBody
            CreateSucursalRequest request) {

        Sucursal sucursal = service.create(
                franquiciaId,
                request.nombre()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(sucursal);
    }

    @PatchMapping("/sucursales/{id}")
    public Sucursal updateNombre(
            @PathVariable Long id,
            @Valid @RequestBody
            UpdateNombreRequest request) {

        return service.updateNombre(
                id,
                request.nombre()
        );
    }
}

