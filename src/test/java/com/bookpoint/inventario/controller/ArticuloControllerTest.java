package com.bookpoint.inventario.controller;

import com.bookpoint.inventario.model.Articulo;
import com.bookpoint.inventario.service.ArticuloService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ArticuloController.class)
class ArticuloControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArticuloService articuloService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Articulo buildValidArticulo() {
        Articulo articulo = new Articulo();
        articulo.setNombre("Lapiz");

        // Completa TODOS los campos obligatorios de tu entidad
        articulo.setPrecio(1000);
        articulo.setStockBodega(10);

        return articulo;
    }

    private Articulo buildArticuloResponse() {
        Articulo articulo = buildValidArticulo();
        articulo.setId(1L);
        return articulo;
    }

    @Test
    void postArticulo_ok() throws Exception {
        when(articuloService.crear(any()))
                .thenReturn(buildArticuloResponse());

        mockMvc.perform(post("/api/v1/articulos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(buildValidArticulo())))
                .andExpect(status().isOk());
    }

    @Test
    void postArticulo_badRequest_exception() throws Exception {
        when(articuloService.crear(any()))
                .thenThrow(new RuntimeException("error"));

        mockMvc.perform(post("/api/v1/articulos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(buildValidArticulo())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getArticulos_ok() throws Exception {
        when(articuloService.listar())
                .thenReturn(Arrays.asList(buildArticuloResponse()));

        mockMvc.perform(get("/api/v1/articulos"))
                .andExpect(status().isOk());
    }

    @Test
    void getArticulos_empty() throws Exception {
        when(articuloService.listar())
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/articulos"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getArticuloById_ok() throws Exception {
        when(articuloService.buscarPorId(1L))
                .thenReturn(buildArticuloResponse());

        mockMvc.perform(get("/api/v1/articulos/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getArticuloById_notFound() throws Exception {
        when(articuloService.buscarPorId(1L))
                .thenReturn(null);

        mockMvc.perform(get("/api/v1/articulos/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getArticuloById_exception() throws Exception {
        when(articuloService.buscarPorId(1L))
                .thenThrow(new RuntimeException("error"));

        mockMvc.perform(get("/api/v1/articulos/1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void putArticulo_ok() throws Exception {
        when(articuloService.modificar(anyLong(), any()))
                .thenReturn(buildArticuloResponse());

        mockMvc.perform(put("/api/v1/articulos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(buildValidArticulo())))
                .andExpect(status().isOk());
    }

    @Test
    void putArticulo_exception() throws Exception {
        when(articuloService.modificar(anyLong(), any()))
                .thenThrow(new RuntimeException("error"));

        mockMvc.perform(put("/api/v1/articulos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(buildValidArticulo())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void sumarStock_ok() throws Exception {
        when(articuloService.sumarStock(1L, 10))
                .thenReturn(buildArticuloResponse());

        mockMvc.perform(put("/api/v1/articulos/1/sumar-stock")
                .param("cantidad", "10"))
                .andExpect(status().isOk());
    }

    @Test
    void sumarStock_notFound() throws Exception {
        when(articuloService.sumarStock(1L, 10))
                .thenReturn(null);

        mockMvc.perform(put("/api/v1/articulos/1/sumar-stock")
                .param("cantidad", "10"))
                .andExpect(status().isNotFound());
    }

    @Test
    void sumarStock_exception() throws Exception {
        when(articuloService.sumarStock(1L, 10))
                .thenThrow(new RuntimeException("error"));

        mockMvc.perform(put("/api/v1/articulos/1/sumar-stock")
                .param("cantidad", "10"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteArticulo_ok() throws Exception {
        doNothing().when(articuloService).eliminar(1L);

        mockMvc.perform(delete("/api/v1/articulos/1"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteArticulo_exception() throws Exception {
        doThrow(new RuntimeException("error"))
                .when(articuloService).eliminar(1L);

        mockMvc.perform(delete("/api/v1/articulos/1"))
                .andExpect(status().isBadRequest());
    }

}