package com.franquicias.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.franquicias.model.Producto;

import java.util.Optional;

public interface ProductoRepository
        extends JpaRepository<Producto, Long> {

    boolean existsBySucursalIdAndNombre(
            Long sucursalId,
            String nombre
    );

    Optional<Producto> findByIdAndSucursalId(
            Long productId,
            Long branchId
    );
}