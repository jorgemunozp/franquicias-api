package com.franquicias.service;

import com.franquicias.exception.ResourceNotFoundException;
import com.franquicias.model.Franquicia;
import com.franquicias.repository.FranquiciaRepository;
import org.springframework.stereotype.Service;

@Service
public class FranquiciaService {

    private final FranquiciaRepository repository;

    public FranquiciaService(
            FranquiciaRepository repository) {
        this.repository = repository;
    }

    public Franquicia create(String nombre) {

        if(repository.existsByNombre(nombre)) {
            throw new IllegalArgumentException(
                    "Franquicia ya existe"
            );
        }

        Franquicia franquicia = new Franquicia();
        franquicia.setNombre(nombre);

        return repository.save(franquicia);
    }

    public Franquicia updateNombre(
            Long id,
            String nombre) {

        Franquicia franquicia = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Franquicia no encontrada"
                        )
                );

        franquicia.setNombre(nombre);

        return repository.save(franquicia);
    }
}
