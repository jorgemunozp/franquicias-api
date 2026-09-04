package com.franquicias.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.franquicias.exception.GlobalExceptionHandler;
import com.franquicias.exception.ResourceNotFoundException;
import com.franquicias.model.Franquicia;
import com.franquicias.service.FranquiciaService;
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
class FranquiciaControllerTest {

    @Mock
    private FranquiciaService service;

    @InjectMocks
    private FranquiciaController controller;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc() {
        return MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void create_shouldReturn201() throws Exception {
        Franquicia franquicia = new Franquicia();
        franquicia.setId(1L);
        franquicia.setNombre("McDonalds");
        when(service.create("McDonalds")).thenReturn(franquicia);

        mockMvc().perform(post("/api/franquicias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new Object() {
                            public final String nombre = "McDonalds";
                        })))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("McDonalds"));

        verify(service).create("McDonalds");
    }

    @Test
    void create_shouldRejectBlankName() throws Exception {
        mockMvc().perform(post("/api/franquicias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"\"}"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(service);
    }

    @Test
    void updateNombre_shouldReturn200() throws Exception {
        Franquicia franquicia = new Franquicia();
        franquicia.setId(1L);
        franquicia.setNombre("McDonalds Colombia");
        when(service.updateNombre(1L, "McDonalds Colombia")).thenReturn(franquicia);

        mockMvc().perform(patch("/api/franquicias/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"McDonalds Colombia\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("McDonalds Colombia"));
    }

    @Test
    void updateNombre_shouldReturn404WhenNotFound() throws Exception {
        when(service.updateNombre(99L, "New Name"))
                .thenThrow(new ResourceNotFoundException("Franquicia no encontrada"));

        mockMvc().perform(patch("/api/franquicias/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"New Name\"}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Franquicia no encontrada"));
    }
}
