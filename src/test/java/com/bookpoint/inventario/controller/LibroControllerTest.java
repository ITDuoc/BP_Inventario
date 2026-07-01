package com.bookpoint.inventario.controller;

import com.bookpoint.inventario.model.Libro;
import com.bookpoint.inventario.service.LibroService;
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

@WebMvcTest(LibroController.class)
class LibroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LibroService libroService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Libro buildValidLibro() {
        Libro libro = new Libro();
        libro.setTitulo("Clean Code");
        libro.setAutor("Robert Martin");
        libro.setEditorial("Prentice Hall");
        libro.setGenero("Programación");
        libro.setPrecio(25000);
        libro.setUbicacionBodega("A-01");
        libro.setStockBodega(10);
        libro.setMostrarCatalogo(true);
        return libro;
    }

    private Libro buildLibroResponse() {
        Libro libro = buildValidLibro();
        libro.setId(1L);
        return libro;
    }

    @Test
    void postLibro_ok() throws Exception {
        when(libroService.crear(any())).thenReturn(buildLibroResponse());

        mockMvc.perform(post("/api/v1/libros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildValidLibro())))
                .andExpect(status().isOk());
    }

    @Test
    void postLibro_badRequest_exception() throws Exception {
        when(libroService.crear(any()))
                .thenThrow(new RuntimeException("error"));

        mockMvc.perform(post("/api/v1/libros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildValidLibro())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getLibros_ok() throws Exception {
        when(libroService.listar())
                .thenReturn(Arrays.asList(buildLibroResponse()));

        mockMvc.perform(get("/api/v1/libros"))
                .andExpect(status().isOk());
    }

    @Test
    void getLibros_empty() throws Exception {
        when(libroService.listar())
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/libros"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getLibroById_ok() throws Exception {
        when(libroService.buscarPorId(1L))
                .thenReturn(buildLibroResponse());

        mockMvc.perform(get("/api/v1/libros/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getLibroById_notFound() throws Exception {
        when(libroService.buscarPorId(1L))
                .thenReturn(null);

        mockMvc.perform(get("/api/v1/libros/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getLibroById_exception() throws Exception {
        when(libroService.buscarPorId(1L))
                .thenThrow(new RuntimeException("error"));

        mockMvc.perform(get("/api/v1/libros/1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void putLibro_ok() throws Exception {
        when(libroService.modificar(anyLong(), any()))
                .thenReturn(buildLibroResponse());

        mockMvc.perform(put("/api/v1/libros/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildValidLibro())))
                .andExpect(status().isOk());
    }

    @Test
    void putLibro_exception() throws Exception {
        when(libroService.modificar(anyLong(), any()))
                .thenThrow(new RuntimeException("error"));

        mockMvc.perform(put("/api/v1/libros/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildValidLibro())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void sumarStock_ok() throws Exception {
        when(libroService.sumarStock(1L, 10))
                .thenReturn(buildLibroResponse());

        mockMvc.perform(put("/api/v1/libros/1/sumar-stock?cantidad=10"))
                .andExpect(status().isOk());
    }

    @Test
    void sumarStock_notFound() throws Exception {
        when(libroService.sumarStock(1L, 10))
                .thenReturn(null);

        mockMvc.perform(put("/api/v1/libros/1/sumar-stock?cantidad=10"))
                .andExpect(status().isNotFound());
    }

    @Test
    void sumarStock_exception() throws Exception {
        when(libroService.sumarStock(1L, 10))
                .thenThrow(new RuntimeException("error"));

        mockMvc.perform(put("/api/v1/libros/1/sumar-stock?cantidad=10"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteLibro_ok() throws Exception {
        doNothing().when(libroService).eliminar(1L);

        mockMvc.perform(delete("/api/v1/libros/1"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteLibro_exception() throws Exception {
        doThrow(new RuntimeException("error"))
                .when(libroService).eliminar(1L);

        mockMvc.perform(delete("/api/v1/libros/1"))
                .andExpect(status().isBadRequest());
    }
}