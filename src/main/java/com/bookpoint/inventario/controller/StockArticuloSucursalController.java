package com.bookpoint.inventario.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bookpoint.inventario.dto
        .DistribucionArticuloDTO;

import com.bookpoint.inventario.dto
        .TransferenciaArticuloDTO;

import com.bookpoint.inventario.model
        .StockArticuloSucursal;

import com.bookpoint.inventario.service
        .StockArticuloSucursalService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/stock-articulos")

public class StockArticuloSucursalController {

    @Autowired
    private StockArticuloSucursalService
            stockService;

    @GetMapping
    public ResponseEntity<?> getStocks() {

        List<StockArticuloSucursal> lista =
                stockService.listar();

        if (lista.isEmpty()) {

            return ResponseEntity
                    .status(404)
                    .body(
                    "No hay stock registrado");
        }

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/sucursal/{id}")
    public ResponseEntity<?> getStockSucursal(
            @PathVariable Long id) {

        List<StockArticuloSucursal> lista =
                stockService
                .listarPorSucursal(id);

        if (lista.isEmpty()) {

            return ResponseEntity
                    .status(404)
                    .body(
                    "No hay stock en esta sucursal");
        }

        return ResponseEntity.ok(lista);
    }

    @PostMapping("/distribuir")
    public ResponseEntity<?> distribuirArticulo(

            @Valid
            @RequestBody
            DistribucionArticuloDTO dto) {

        try {

            String respuesta =
                    stockService
                    .distribuirArticulo(dto);

            return ResponseEntity.ok(
                    respuesta);

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(
                    "Error al distribuir articulo: "
                    + e.getMessage());
        }
    }

    @PostMapping("/transferir")
    public ResponseEntity<?> transferirArticulo(

            @Valid
            @RequestBody
            TransferenciaArticuloDTO dto) {

        try {

            String respuesta =
                    stockService
                    .transferirArticulo(dto);

            return ResponseEntity.ok(
                    respuesta);

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(
                    "Error al transferir articulo: "
                    + e.getMessage());
        }
    }

    @GetMapping("/alertas/{sucursalId}")
    public ResponseEntity<?> alertasStock(
            @PathVariable Long sucursalId) {

        String alerta =
                stockService
                .verificarStockMinimo(
                        sucursalId);

        if (alerta.isEmpty()) {

            return ResponseEntity.ok(
                    "No hay alertas de stock");
        }

        return ResponseEntity.ok(alerta);
    }
}