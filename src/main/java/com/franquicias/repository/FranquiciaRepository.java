package com.franquicias.repository;

import com.franquicias.model.Franquicia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FranquiciaRepository
        extends JpaRepository<Franquicia, Long> {

    boolean existsByNombre(String nombre);
}
