package com.franquicias.controller;

import com.franquicias.exception.GlobalExceptionHandler;
import com.franquicias.exception.ResourceNotFoundException;
import com.franquicias.model.Producto;
import com.franquicias.service.ProductoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProductoControllerTest {

    @Mock
    private ProductoService service;

    @InjectMocks
    private ProductoController controller;

    private MockMvc mockMvc() {
        return MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void create_shouldReturn201() throws Exception {
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Big Mac");
        producto.setStock(50);
        when(service.create(10L, "Big Mac", 50)).thenReturn(producto);

        mockMvc().perform(post("/api/sucursales/10/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Big Mac\",\"stock\":50}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Big Mac"))
                .andExpect(jsonPath("$.stock").value(50));
    }

    @Test
    void create_shouldRejectNegativeStock() throws Exception {
        mockMvc().perform(post("/api/sucursales/10/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Big Mac\",\"stock\":-1}"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(service);
    }

    @Test
    void updateStock_shouldUsePatchAndReturn200() throws Exception {
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Big Mac");
        producto.setStock(100);
        when(service.updateStock(10L, 1L, 100)).thenReturn(producto);

        mockMvc().perform(patch("/api/sucursales/10/productos/1/stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"stock\":100}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stock").value(100));
    }

    @Test
    void updateStock_shouldRejectNegativeStock() throws Exception {
        mockMvc().perform(patch("/api/sucursales/10/productos/1/stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"stock\":-10}"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(service);
    }

    @Test
    void updateStock_shouldReturn404WhenProductDoesNotExist() throws Exception {
        when(service.updateStock(10L, 99L, 100))
                .thenThrow(new ResourceNotFoundException("Producto no encontrado"));

        mockMvc().perform(patch("/api/sucursales/10/productos/99/stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"stock\":100}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Producto no encontrado"));
    }

    @Test
    void updateNombre_shouldReturn200() throws Exception {
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Big Mac Grande");
        producto.setStock(50);
        when(service.updateNombre(1L, "Big Mac Grande")).thenReturn(producto);

        mockMvc().perform(patch("/api/productos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Big Mac Grande\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Big Mac Grande"));
    }

    @Test
    void delete_shouldReturn204() throws Exception {
        doNothing().when(service).delete(10L, 1L);

        mockMvc().perform(delete("/api/sucursales/10/productos/1"))
                .andExpect(status().isNoContent());

        verify(service).delete(10L, 1L);
    }

    @Test
    void delete_shouldReturn404WhenProductDoesNotExist() throws Exception {
        doThrow(new ResourceNotFoundException("Producto no encontrado"))
                .when(service).delete(10L, 99L);

        mockMvc().perform(delete("/api/sucursales/10/productos/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Producto no encontrado"));
    }
}
