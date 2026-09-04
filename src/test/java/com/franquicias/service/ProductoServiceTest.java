package com.franquicias.service;

import com.franquicias.exception.ResourceNotFoundException;
import com.franquicias.model.Producto;
import com.franquicias.model.Sucursal;
import com.franquicias.repository.ProductoRepository;
import com.franquicias.repository.SucursalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private SucursalRepository sucursalRepository;

    private ProductoService service;

    @BeforeEach
    void setUp() {
        service = new ProductoService(productoRepository, sucursalRepository);
    }

    @Test
    void create_shouldSaveProductAssociatedWithSucursal() {
        Sucursal sucursal = new Sucursal();
        sucursal.setId(1L);
        sucursal.setNombre("Zona T");

        when(sucursalRepository.findById(1L)).thenReturn(Optional.of(sucursal));
        when(productoRepository.existsBySucursalIdAndNombre(1L, "Big Mac")).thenReturn(false);
        when(productoRepository.save(any(Producto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Producto result = service.create(1L, "Big Mac", 50);

        assertEquals("Big Mac", result.getNombre());
        assertEquals(50, result.getStock());
        assertSame(sucursal, result.getSucursal());
        verify(productoRepository).save(any(Producto.class));
    }

    @Test
    void create_shouldThrowWhenSucursalDoesNotExist() {
        when(sucursalRepository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.create(99L, "Big Mac", 50)
        );

        assertEquals("Sucursal no encontrada", exception.getMessage());
        verify(productoRepository, never()).save(any());
    }

    @Test
    void create_shouldRejectDuplicateProductInSameSucursal() {
        Sucursal sucursal = new Sucursal();
        sucursal.setId(1L);

        when(sucursalRepository.findById(1L)).thenReturn(Optional.of(sucursal));
        when(productoRepository.existsBySucursalIdAndNombre(1L, "Big Mac")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.create(1L, "Big Mac", 50)
        );

        assertEquals("Producto ya existe", exception.getMessage());
        verify(productoRepository, never()).save(any());
    }

    @Test
    void delete_shouldDeleteProductBelongingToSucursal() {
        Producto producto = new Producto();
        producto.setId(10L);

        when(productoRepository.findByIdAndSucursalId(10L, 1L)).thenReturn(Optional.of(producto));

        service.delete(1L, 10L);

        verify(productoRepository).delete(producto);
    }

    @Test
    void delete_shouldThrowWhenProductDoesNotBelongToSucursal() {
        when(productoRepository.findByIdAndSucursalId(10L, 1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.delete(1L, 10L)
        );

        assertEquals("Producto no encontrado", exception.getMessage());
        verify(productoRepository, never()).delete(any());
    }

    @Test
    void updateNombre_shouldUpdateAndSave() {
        Producto producto = new Producto();
        producto.setId(10L);
        producto.setNombre("Big Mac");

        when(productoRepository.findById(10L)).thenReturn(Optional.of(producto));
        when(productoRepository.save(producto)).thenReturn(producto);

        Producto result = service.updateNombre(10L, "Big Mac Grande");

        assertEquals("Big Mac Grande", result.getNombre());
        verify(productoRepository).save(producto);
    }

    @Test
    void updateNombre_shouldThrowWhenProductDoesNotExist() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.updateNombre(99L, "New Name")
        );

        assertEquals("Producto no encontrado", exception.getMessage());
        verify(productoRepository, never()).save(any());
    }

    @Test
    void updateStock_shouldUpdateAndSaveProduct() {
        Producto producto = new Producto();
        producto.setId(10L);
        producto.setStock(20);

        when(productoRepository.findByIdAndSucursalId(10L, 1L)).thenReturn(Optional.of(producto));
        when(productoRepository.save(producto)).thenReturn(producto);

        Producto result = service.updateStock(1L, 10L, 100);

        assertEquals(100, result.getStock());
        verify(productoRepository).save(producto);
    }

    @Test
    void updateStock_shouldAllowZero() {
        Producto producto = new Producto();
        producto.setId(10L);
        producto.setStock(20);

        when(productoRepository.findByIdAndSucursalId(10L, 1L)).thenReturn(Optional.of(producto));
        when(productoRepository.save(producto)).thenReturn(producto);

        Producto result = service.updateStock(1L, 10L, 0);

        assertEquals(0, result.getStock());
    }

    @Test
    void updateStock_shouldThrowWhenProductDoesNotBelongToSucursal() {
        when(productoRepository.findByIdAndSucursalId(10L, 1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.updateStock(1L, 10L, 100)
        );

        assertEquals("Producto no encontrado", exception.getMessage());
        verify(productoRepository, never()).save(any());
    }
}
