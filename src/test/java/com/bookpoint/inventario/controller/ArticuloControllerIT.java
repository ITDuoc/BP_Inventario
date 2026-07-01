package com.bookpoint.inventario.controller;

import com.bookpoint.inventario.model.Articulo;
import com.bookpoint.inventario.repository.ArticuloRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ArticuloControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ArticuloRepository articuloRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void cleanDb() {
        articuloRepository.deleteAll();
    }

    // ===== CREATE =====
    @Test
    void testCrearArticulo() throws Exception {

        Articulo articulo = new Articulo(
                null,
                "Mouse Gamer",
                "Logitech",
                15000,
                "Bodega A1",
                10,
                true
        );

        mockMvc.perform(post("/api/v1/articulos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(articulo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nombre").value("Mouse Gamer"));
    }

    // ===== GET BY ID OK =====
    @Test
    void testGetArticuloById_ok() throws Exception {

        Articulo articulo = articuloRepository.save(new Articulo(
                null,
                "Teclado Mecánico",
                "Redragon",
                20000,
                "Bodega B1",
                5,
                true
        ));

        mockMvc.perform(get("/api/v1/articulos/" + articulo.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Teclado Mecánico"))
                .andExpect(jsonPath("$.marca").value("Redragon"));
    }

    // ===== GET BY ID NOT FOUND =====
    @Test
    void testGetArticuloById_notFound() throws Exception {

        mockMvc.perform(get("/api/v1/articulos/999"))
                .andExpect(status().isNotFound());
    }

    // ===== GET ALL =====
    @Test
    void testGetAllArticulos() throws Exception {

        articuloRepository.save(new Articulo(
                null,
                "Monitor",
                "Samsung",
                120000,
                "Bodega C1",
                3,
                true
        ));

        mockMvc.perform(get("/api/v1/articulos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Monitor"));
    }

    // ===== GET ALL EMPTY =====
    @Test
    void testGetAllArticulos_empty() throws Exception {

        mockMvc.perform(get("/api/v1/articulos"))
                .andExpect(status().isNotFound());
    }

    // ===== UPDATE =====
    @Test
    void testActualizarArticulo() throws Exception {

        Articulo articulo = articuloRepository.save(new Articulo(
                null,
                "Impresora",
                "HP",
                90000,
                "Bodega D1",
                2,
                true
        ));

        Articulo actualizado = new Articulo(
                null,
                "Impresora Pro",
                "HP",
                95000,
                "Bodega D1",
                8,
                true
        );

        mockMvc.perform(put("/api/v1/articulos/" + articulo.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(actualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Impresora Pro"))
                .andExpect(jsonPath("$.stockBodega").value(8));
    }

    // ===== SUMAR STOCK =====
    @Test
    void testSumarStock() throws Exception {

        Articulo articulo = articuloRepository.save(new Articulo(
                null,
                "Webcam",
                "Dell",
                30000,
                "Bodega E1",
                4,
                true
        ));

        mockMvc.perform(put("/api/v1/articulos/" + articulo.getId() + "/sumar-stock")
                .param("cantidad", "6"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stockBodega").value(10));
    }

    // ===== DELETE =====
    @Test
    void testEliminarArticulo() throws Exception {

        Articulo articulo = articuloRepository.save(new Articulo(
                null,
                "Router",
                "TP-Link",
                25000,
                "Bodega F1",
                1,
                false
        ));

        mockMvc.perform(delete("/api/v1/articulos/" + articulo.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Articulo eliminado correctamente"));
    }
}