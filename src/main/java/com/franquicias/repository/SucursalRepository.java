package com.franquicias.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.franquicias.model.Sucursal;

public interface SucursalRepository
        extends JpaRepository<Sucursal, Long> {

    boolean existsByFranquiciaIdAndNombre(
            Long franquiciaId,
            String nombre
    );
}