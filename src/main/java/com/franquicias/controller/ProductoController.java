package com.franquicias.controller;

import com.franquicias.dto.CreateProductoRequest;
import com.franquicias.dto.UpdateNombreRequest;
import com.franquicias.dto.UpdateStockRequest;
import com.franquicias.model.Producto;
import com.franquicias.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ProductoController {

    private final ProductoService service;

    public ProductoController(
            ProductoService service) {
        this.service = service;
    }

    @PostMapping("/sucursales/{sucursalId}/productos")
    public ResponseEntity<Producto> create(
            @PathVariable Long sucursalId,
            @Valid @RequestBody
            CreateProductoRequest request) {

        Producto producto = service.create(
                sucursalId,
                request.nombre(),
                request.stock()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(producto);
    }

    @PatchMapping("/sucursales/{sucursalId}/productos/{productoId}/stock")
    public Producto updateStock(
            @PathVariable Long sucursalId,
            @PathVariable Long productoId,
            @Valid @RequestBody
            UpdateStockRequest request) {

        return service.updateStock(
                sucursalId,
                productoId,
                request.stock()
        );
    }

    @DeleteMapping("/sucursales/{sucursalId}/productos/{productoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long sucursalId,
            @PathVariable Long productoId) {

        service.delete(sucursalId, productoId);
    }


    @PatchMapping("/productos/{productoId}")
    public Producto updateNombre(
            @PathVariable Long productoId,
            @Valid @RequestBody
            UpdateNombreRequest request) {

        return service.updateNombre(
                productoId,
                request.nombre()
        );
    }
}
