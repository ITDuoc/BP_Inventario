package com.bookpoint.inventario.controller;

import com.bookpoint.inventario.model.Libro;
import com.bookpoint.inventario.repository.LibroRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class LibroControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void clean() {
        libroRepository.deleteAll();
    }

    private Libro crearLibroValido() {
        Libro libro = new Libro();
        libro.setTitulo("El Hobbit");
        libro.setAutor("J.R.R. Tolkien");
        libro.setEditorial("Minotauro");
        libro.setGenero("Fantasía");
        libro.setPrecio(15000);
        libro.setUbicacionBodega("A1");
        libro.setStockBodega(10);
        libro.setMostrarCatalogo(true);
        return libro;
    }

    @Test
    void testCrearYListarLibros() throws Exception {

        Libro libro = crearLibroValido();

        mockMvc.perform(post("/api/v1/libros")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(libro)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.titulo").value("El Hobbit"));

        mockMvc.perform(get("/api/v1/libros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void testBuscarPorId() throws Exception {

        Libro libro = libroRepository.save(crearLibroValido());

        mockMvc.perform(get("/api/v1/libros/{id}", libro.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("El Hobbit"))
                .andExpect(jsonPath("$.autor").value("J.R.R. Tolkien"));
    }

    @Test
    void testActualizarLibro() throws Exception {

        Libro libro = libroRepository.save(crearLibroValido());

        libro.setTitulo("El Hobbit 2");
        libro.setPrecio(20000);

        mockMvc.perform(put("/api/v1/libros/{id}", libro.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(libro)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("El Hobbit 2"))
                .andExpect(jsonPath("$.precio").value(20000));
    }

    @Test
    void testEliminarLibro() throws Exception {

        Libro libro = libroRepository.save(crearLibroValido());

        mockMvc.perform(delete("/api/v1/libros/{id}", libro.getId()))
                .andExpect(status().isOk()); // IMPORTANTE: tu controller devuelve 200 OK, no 204
    }

    @Test
    void testBuscarPorId_NotFound() throws Exception {

        mockMvc.perform(get("/api/v1/libros/{id}", 999L))
                .andExpect(status().isNotFound());
    }
}