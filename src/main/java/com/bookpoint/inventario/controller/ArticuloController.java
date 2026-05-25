package com.bookpoint.inventario.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bookpoint.inventario.model.Articulo;
import com.bookpoint.inventario.service.ArticuloService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/articulos")

public class ArticuloController {

    @Autowired
    private ArticuloService articuloService;

    @PostMapping
    public ResponseEntity<?> postArticulo(
            @Valid @RequestBody Articulo articulo) {

        try {

            Articulo nuevoArticulo =
                    articuloService.crear(articulo);

            return ResponseEntity.ok(nuevoArticulo);

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body("Error al crear articulo: "
                            + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getArticulos() {

        List<Articulo> lista =
                articuloService.listar();

        if (lista.isEmpty()) {

            return ResponseEntity
                    .status(404)
                    .body("No hay articulos registrados");
        }

        return ResponseEntity.ok(lista);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getArticuloById(
            @PathVariable Long id) {

        try {

            Articulo articulo =
                    articuloService.buscarPorId(id);

            if (articulo == null) {

                return ResponseEntity
                        .status(404)
                        .body("Articulo no encontrado");
            }

            return ResponseEntity.ok(articulo);

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body("Error al buscar articulo: "
                            + e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<?> putArticulo(
            @PathVariable Long id,
            @Valid @RequestBody Articulo articulo) {

        try {

            Articulo articuloActualizado =
                    articuloService.modificar(id, articulo);

            return ResponseEntity.ok(articuloActualizado);

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body("Error al actualizar articulo: "
                            + e.getMessage());
        }
    }

    @PutMapping("{id}/sumar-stock")
public ResponseEntity<?> sumarStock(
        @PathVariable Long id,
        @RequestParam int cantidad) {

    try {

        Articulo articuloActualizado =
                articuloService.sumarStock(id, cantidad);

        if (articuloActualizado == null) {

            return ResponseEntity
                    .status(404)
                    .body("Articulo no encontrado");
        }

        return ResponseEntity.ok(articuloActualizado);

    } catch (Exception e) {

        return ResponseEntity
                .badRequest()
                .body("Error al sumar stock: "
                        + e.getMessage());
    }
}

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteArticulo(
            @PathVariable Long id) {

        try {

            articuloService.eliminar(id);

            return ResponseEntity
                    .ok("Articulo eliminado correctamente");

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body("Error al eliminar articulo: "
                            + e.getMessage());
        }
    }
}