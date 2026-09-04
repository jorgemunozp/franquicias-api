package com.franquicias.service;

import com.franquicias.exception.ResourceNotFoundException;
import com.franquicias.model.Franquicia;
import com.franquicias.model.Sucursal;
import com.franquicias.repository.FranquiciaRepository;
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
class SucursalServiceTest {

    @Mock
    private SucursalRepository sucursalRepository;

    @Mock
    private FranquiciaRepository franquiciaRepository;

    private SucursalService service;

    @BeforeEach
    void setUp() {
        service = new SucursalService(sucursalRepository, franquiciaRepository);
    }

    @Test
    void create_shouldSaveSucursalAssociatedWithFranquicia() {
        Franquicia franquicia = new Franquicia();
        franquicia.setId(1L);
        franquicia.setNombre("McDonalds");

        when(franquiciaRepository.findById(1L)).thenReturn(Optional.of(franquicia));
        when(sucursalRepository.existsByFranquiciaIdAndNombre(1L, "Zona T")).thenReturn(false);
        when(sucursalRepository.save(any(Sucursal.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Sucursal result = service.create(1L, "Zona T");

        assertEquals("Zona T", result.getNombre());
        assertSame(franquicia, result.getFranquicia());
        verify(sucursalRepository).save(any(Sucursal.class));
    }

    @Test
    void create_shouldThrowWhenFranquiciaDoesNotExist() {
        when(franquiciaRepository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.create(99L, "Zona T")
        );

        assertEquals("Franquicia no encontrada", exception.getMessage());
        verify(sucursalRepository, never()).save(any());
    }

    @Test
    void create_shouldRejectDuplicateSucursalInSameFranquicia() {
        Franquicia franquicia = new Franquicia();
        franquicia.setId(1L);

        when(franquiciaRepository.findById(1L)).thenReturn(Optional.of(franquicia));
        when(sucursalRepository.existsByFranquiciaIdAndNombre(1L, "Zona T")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.create(1L, "Zona T")
        );

        assertEquals("Sucursal ya existe", exception.getMessage());
        verify(sucursalRepository, never()).save(any());
    }

    @Test
    void updateNombre_shouldUpdateAndSave() {
        Sucursal sucursal = new Sucursal();
        sucursal.setId(1L);
        sucursal.setNombre("Old Name");

        when(sucursalRepository.findById(1L)).thenReturn(Optional.of(sucursal));
        when(sucursalRepository.save(sucursal)).thenReturn(sucursal);

        Sucursal result = service.updateNombre(1L, "New Name");

        assertEquals("New Name", result.getNombre());
        verify(sucursalRepository).save(sucursal);
    }

    @Test
    void updateNombre_shouldThrowWhenSucursalDoesNotExist() {
        when(sucursalRepository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.updateNombre(99L, "New Name")
        );

        assertEquals("Sucursal no encontrada", exception.getMessage());
        verify(sucursalRepository, never()).save(any());
    }
}
