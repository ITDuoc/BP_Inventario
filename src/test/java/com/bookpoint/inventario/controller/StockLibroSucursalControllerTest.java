package com.bookpoint.inventario.controller;

import com.bookpoint.inventario.dto.DistribucionLibroDTO;
import com.bookpoint.inventario.dto.TransferenciaLibroDTO;
import com.bookpoint.inventario.model.StockLibroSucursal;
import com.bookpoint.inventario.service.StockLibroSucursalService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StockLibroSucursalController.class)
class StockLibroSucursalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private StockLibroSucursalService stockService;

    @Test
    void getStocks_ok() throws Exception {
        StockLibroSucursal stock = new StockLibroSucursal();
        when(stockService.listar()).thenReturn(List.of(stock));

        mockMvc.perform(get("/api/v1/stock-libros"))
                .andExpect(status().isOk());
    }

    @Test
    void getStocks_empty() throws Exception {
        when(stockService.listar()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/stock-libros"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No hay stock registrado"));
    }

    @Test
    void getStockSucursal_ok() throws Exception {
        StockLibroSucursal stock = new StockLibroSucursal();
        when(stockService.listarPorSucursal(1L)).thenReturn(List.of(stock));

        mockMvc.perform(get("/api/v1/stock-libros/sucursal/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getStockSucursal_empty() throws Exception {
        when(stockService.listarPorSucursal(1L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/stock-libros/sucursal/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No hay stock en esta sucursal"));
    }

    @Test
    void distribuir_ok() throws Exception {
        when(stockService.distribuirLibro(any())).thenReturn("OK");

        DistribucionLibroDTO dto = new DistribucionLibroDTO();

        mockMvc.perform(post("/api/v1/stock-libros/distribuir")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));
    }

    @Test
    void distribuir_error() throws Exception {
        when(stockService.distribuirLibro(any()))
                .thenThrow(new RuntimeException("fail"));

        DistribucionLibroDTO dto = new DistribucionLibroDTO();

        mockMvc.perform(post("/api/v1/stock-libros/distribuir")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Error al distribuir libro")));
    }

    @Test
    void transferir_ok() throws Exception {
        when(stockService.transferirLibro(any())).thenReturn("OK");

        TransferenciaLibroDTO dto = new TransferenciaLibroDTO();

        mockMvc.perform(post("/api/v1/stock-libros/transferir")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));
    }

    @Test
    void transferir_error() throws Exception {
        when(stockService.transferirLibro(any()))
                .thenThrow(new RuntimeException("fail"));

        TransferenciaLibroDTO dto = new TransferenciaLibroDTO();

        mockMvc.perform(post("/api/v1/stock-libros/transferir")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Error al transferir libro")));
    }

    @Test
    void alertas_empty() throws Exception {
        when(stockService.verificarStockMinimo(1L)).thenReturn("");

        mockMvc.perform(get("/api/v1/stock-libros/alertas/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("No hay alertas de stock"));
    }

    @Test
    void alertas_ok() throws Exception {
        when(stockService.verificarStockMinimo(1L)).thenReturn("ALERTA");

        mockMvc.perform(get("/api/v1/stock-libros/alertas/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("ALERTA"));
    }
}