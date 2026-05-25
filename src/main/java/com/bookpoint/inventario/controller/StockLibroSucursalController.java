package com.bookpoint.inventario.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bookpoint.inventario.dto.DistribucionLibroDTO;
import com.bookpoint.inventario.dto.TransferenciaLibroDTO;

import com.bookpoint.inventario.model.StockLibroSucursal;

import com.bookpoint.inventario.service
        .StockLibroSucursalService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/stock-libros")

public class StockLibroSucursalController {

    @Autowired
    private StockLibroSucursalService
            stockService;

    @GetMapping
    public ResponseEntity<?> getStocks() {

        List<StockLibroSucursal> lista =
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

        List<StockLibroSucursal> lista =
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
    public ResponseEntity<?> distribuirLibro(

            @Valid
            @RequestBody
            DistribucionLibroDTO dto) {

        try {

            String respuesta =
                    stockService
                    .distribuirLibro(dto);

            return ResponseEntity.ok(
                    respuesta);

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(
                    "Error al distribuir libro: "
                    + e.getMessage());
        }
    }

    @PostMapping("/transferir")
    public ResponseEntity<?> transferirLibro(

            @Valid
            @RequestBody
            TransferenciaLibroDTO dto) {

        try {

            String respuesta =
                    stockService
                    .transferirLibro(dto);

            return ResponseEntity.ok(
                    respuesta);

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(
                    "Error al transferir libro: "
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