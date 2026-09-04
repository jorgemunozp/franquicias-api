package com.franquicias.service;

import com.franquicias.exception.ResourceNotFoundException;
import com.franquicias.model.Franquicia;
import com.franquicias.model.Sucursal;
import com.franquicias.repository.FranquiciaRepository;
import com.franquicias.repository.SucursalRepository;
import org.springframework.stereotype.Service;

@Service
public class SucursalService {

    private final SucursalRepository sucursalRepository;
    private final FranquiciaRepository franquiciaRepository;

    public SucursalService(
            SucursalRepository sucursalRepository,
            FranquiciaRepository franquiciaRepository) {

        this.sucursalRepository = sucursalRepository;
        this.franquiciaRepository = franquiciaRepository;
    }

    public Sucursal create(
            Long franquiciaId,
            String nombre) {

        Franquicia franquicia =
                franquiciaRepository.findById(franquiciaId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Franquicia no encontrada"
                                )
                        );

        if (sucursalRepository
                .existsByFranquiciaIdAndNombre(
                        franquiciaId,
                        nombre)) {

            throw new IllegalArgumentException(
                    "Sucursal ya existe"
            );
        }

        Sucursal sucursal = new Sucursal();
        sucursal.setNombre(nombre);
        sucursal.setFranquicia(franquicia);

        return sucursalRepository.save(sucursal);
    }

    public Sucursal updateNombre(
            Long id,
            String nombre) {

        Sucursal sucursal =
                sucursalRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Sucursal no encontrada"
                                )
                        );
        sucursal.setNombre(nombre);

        return sucursalRepository.save(sucursal);
    }
}
