package com.franquicias.service;

import com.franquicias.exception.ResourceNotFoundException;
import com.franquicias.model.Producto;
import com.franquicias.model.Sucursal;
import com.franquicias.repository.ProductoRepository;
import com.franquicias.repository.SucursalRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final SucursalRepository sucursalRepository;

    public ProductoService(
            ProductoRepository productoRepository,
            SucursalRepository sucursalRepository) {

        this.productoRepository = productoRepository;
        this.sucursalRepository = sucursalRepository;
    }

    public Producto create(
            Long sucursalId,
            String nombre,
            Integer stock) {

        Sucursal sucursal =
                sucursalRepository.findById(sucursalId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Sucursal no encontrada"
                                )
                        );

        if (productoRepository
                .existsBySucursalIdAndNombre(
                        sucursalId,
                        nombre)) {

            throw new IllegalArgumentException(
                    "Producto ya existe"
            );
        }

        Producto producto = new Producto();

        producto.setNombre(nombre);
        producto.setStock(stock);
        producto.setSucursal(sucursal);

        return productoRepository.save(producto);
    }

    public void delete(
            Long sucursalId,
            Long productoId) {

        Producto producto =
                productoRepository
                        .findByIdAndSucursalId(
                                productoId,
                                sucursalId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Producto no encontrado"
                                )
                        );

        productoRepository.delete(producto);
    }

    public Producto updateNombre(
            Long productoId,
            String nombre) {

        Producto producto =
                productoRepository.findById(productoId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Producto no encontrado"
                                )
                        );

        producto.setNombre(nombre);

        return productoRepository.save(producto);
    }

    public Producto updateStock(
            Long sucursalId,
            Long productoId,
            Integer stock) {

        Producto producto =
                productoRepository.findByIdAndSucursalId(
                        productoId,
                        sucursalId
                )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Producto no encontrado"
                                )
                        );

        producto.setStock(stock);

        return productoRepository.save(producto);
    }
}
