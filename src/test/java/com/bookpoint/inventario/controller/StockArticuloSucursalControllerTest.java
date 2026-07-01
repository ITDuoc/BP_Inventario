package com.bookpoint.inventario.controller;

import com.bookpoint.inventario.dto.DistribucionArticuloDTO;
import com.bookpoint.inventario.dto.TransferenciaArticuloDTO;
import com.bookpoint.inventario.model.StockArticuloSucursal;
import com.bookpoint.inventario.service.StockArticuloSucursalService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StockArticuloSucursalController.class)
class StockArticuloSucursalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StockArticuloSucursalService stockService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getStocks_ok() throws Exception {
        StockArticuloSucursal s = new StockArticuloSucursal(1L, 1L, 1L, 10, 2);

        Mockito.when(stockService.listar()).thenReturn(Arrays.asList(s));

        mockMvc.perform(get("/api/v1/stock-articulos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].stock", is(10)));
    }

    @Test
    void getStocks_empty() throws Exception {
        Mockito.when(stockService.listar()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/stock-articulos"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No hay stock registrado"));
    }

    @Test
    void getStockSucursal_ok() throws Exception {
        StockArticuloSucursal s = new StockArticuloSucursal(1L, 1L, 1L, 5, 2);

        Mockito.when(stockService.listarPorSucursal(1L)).thenReturn(Arrays.asList(s));

        mockMvc.perform(get("/api/v1/stock-articulos/sucursal/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sucursalId", is(1)));
    }

    @Test
    void getStockSucursal_empty() throws Exception {
        Mockito.when(stockService.listarPorSucursal(1L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/stock-articulos/sucursal/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No hay stock en esta sucursal"));
    }

    @Test
    void distribuir_ok() throws Exception {
        DistribucionArticuloDTO dto = new DistribucionArticuloDTO();

        Mockito.when(stockService.distribuirArticulo(any())).thenReturn("OK");

        mockMvc.perform(post("/api/v1/stock-articulos/distribuir")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));
    }

    @Test
    void distribuir_error() throws Exception {
        DistribucionArticuloDTO dto = new DistribucionArticuloDTO();

        Mockito.when(stockService.distribuirArticulo(any()))
                .thenThrow(new RuntimeException("fail"));

        mockMvc.perform(post("/api/v1/stock-articulos/distribuir")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Error al distribuir articulo")));
    }

    @Test
    void transferir_ok() throws Exception {
        TransferenciaArticuloDTO dto = new TransferenciaArticuloDTO();

        Mockito.when(stockService.transferirArticulo(any())).thenReturn("OK");

        mockMvc.perform(post("/api/v1/stock-articulos/transferir")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));
    }

    @Test
    void transferir_error() throws Exception {
        TransferenciaArticuloDTO dto = new TransferenciaArticuloDTO();

        Mockito.when(stockService.transferirArticulo(any()))
                .thenThrow(new RuntimeException("fail"));

        mockMvc.perform(post("/api/v1/stock-articulos/transferir")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Error al transferir articulo")));
    }

    @Test
    void alertas_conAlerta() throws Exception {
        Mockito.when(stockService.verificarStockMinimo(1L)).thenReturn("ALERTA STOCK");

        mockMvc.perform(get("/api/v1/stock-articulos/alertas/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("ALERTA STOCK"));
    }

    @Test
    void alertas_sinAlerta() throws Exception {
        Mockito.when(stockService.verificarStockMinimo(1L)).thenReturn("");

        mockMvc.perform(get("/api/v1/stock-articulos/alertas/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("No hay alertas de stock"));
    }
}