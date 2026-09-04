package com.franquicias.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.franquicias.exception.GlobalExceptionHandler;
import com.franquicias.exception.ResourceNotFoundException;
import com.franquicias.model.Sucursal;
import com.franquicias.service.SucursalService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class SucursalControllerTest {

    @Mock
    private SucursalService service;

    @InjectMocks
    private SucursalController controller;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc() {
        return MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void create_shouldReturn201() throws Exception {
        Sucursal sucursal = new Sucursal();
        sucursal.setId(1L);
        sucursal.setNombre("Zona T");
        when(service.create(10L, "Zona T")).thenReturn(sucursal);

        mockMvc().perform(post("/api/franquicias/10/sucursales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Zona T\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Zona T"));
    }

    @Test
    void create_shouldReturn404WhenFranquiciaNotFound() throws Exception {
        when(service.create(99L, "Zona T"))
                .thenThrow(new ResourceNotFoundException("Franquicia no encontrada"));

        mockMvc().perform(post("/api/franquicias/99/sucursales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Zona T\"}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Franquicia no encontrada"));
    }

    @Test
    void create_shouldRejectBlankName() throws Exception {
        mockMvc().perform(post("/api/franquicias/1/sucursales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\" \"}"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(service);
    }

    @Test
    void updateNombre_shouldReturn200() throws Exception {
        Sucursal sucursal = new Sucursal();
        sucursal.setId(1L);
        sucursal.setNombre("Zona T Bogotá");
        when(service.updateNombre(1L, "Zona T Bogotá")).thenReturn(sucursal);

        mockMvc().perform(patch("/api/sucursales/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Zona T Bogotá\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Zona T Bogotá"));
    }
}

