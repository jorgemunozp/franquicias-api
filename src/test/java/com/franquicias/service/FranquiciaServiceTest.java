package com.franquicias.service;

import com.franquicias.exception.ResourceNotFoundException;
import com.franquicias.model.Franquicia;
import com.franquicias.repository.FranquiciaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FranquiciaServiceTest {

    @Mock
    private FranquiciaRepository repository;

    private FranquiciaService service;

    @BeforeEach
    void setUp() {
        service = new FranquiciaService(repository);
    }

    @Test
    void create_shouldSaveFranquicia() {
        when(repository.existsByNombre("McDonalds")).thenReturn(false);
        when(repository.save(any(Franquicia.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Franquicia result = service.create("McDonalds");

        assertEquals("McDonalds", result.getNombre());
        verify(repository).existsByNombre("McDonalds");
        verify(repository).save(any(Franquicia.class));
    }

    @Test
    void create_shouldRejectDuplicateName() {
        when(repository.existsByNombre("McDonalds")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.create("McDonalds")
        );

        assertEquals("Franquicia ya existe", exception.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void updateNombre_shouldUpdateAndSave() {
        Franquicia franquicia = new Franquicia();
        franquicia.setId(1L);
        franquicia.setNombre("Old Name");

        when(repository.findById(1L)).thenReturn(Optional.of(franquicia));
        when(repository.save(franquicia)).thenReturn(franquicia);

        Franquicia result = service.updateNombre(1L, "New Name");

        assertEquals("New Name", result.getNombre());
        verify(repository).save(franquicia);
    }

    @Test
    void updateNombre_shouldThrowWhenFranquiciaDoesNotExist() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.updateNombre(99L, "New Name")
        );

        assertEquals("Franquicia no encontrada", exception.getMessage());
        verify(repository, never()).save(any());
    }
}
