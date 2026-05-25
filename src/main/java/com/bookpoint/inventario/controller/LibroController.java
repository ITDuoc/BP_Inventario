package com.bookpoint.inventario.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bookpoint.inventario.model.Libro;
import com.bookpoint.inventario.service.LibroService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/libros")

public class LibroController {

    @Autowired
    private LibroService libroService;

    @PostMapping
    public ResponseEntity<?> postLibro(
            @Valid @RequestBody Libro libro) {

        try {

            Libro nuevoLibro =
                    libroService.crear(libro);

            return ResponseEntity.ok(nuevoLibro);

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body("Error al crear libro: "
                            + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getLibros() {

        List<Libro> lista =
                libroService.listar();

        if (lista.isEmpty()) {

            return ResponseEntity
                    .status(404)
                    .body("No hay libros registrados");
        }

        return ResponseEntity.ok(lista);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getLibroById(
            @PathVariable Long id) {

        try {

            Libro libro =
                    libroService.buscarPorId(id);

            if (libro == null) {

                return ResponseEntity
                        .status(404)
                        .body("Libro no encontrado");
            }

            return ResponseEntity.ok(libro);

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body("Error al buscar libro: "
                            + e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<?> putLibro(
            @PathVariable Long id,
            @Valid @RequestBody Libro libro) {

        try {

            Libro libroActualizado =
                    libroService.modificar(id, libro);

            return ResponseEntity.ok(libroActualizado);

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body("Error al actualizar libro: "
                            + e.getMessage());
        }
    }

    @PutMapping("{id}/sumar-stock")
public ResponseEntity<?> sumarStock(
        @PathVariable Long id,
        @RequestParam int cantidad) {

    try {

        Libro libroActualizado =
                libroService.sumarStock(id, cantidad);

        if (libroActualizado == null) {

            return ResponseEntity
                    .status(404)
                    .body("Libro no encontrado");
        }

        return ResponseEntity.ok(libroActualizado);

    } catch (Exception e) {

        return ResponseEntity
                .badRequest()
                .body("Error al sumar stock: "
                        + e.getMessage());
    }
}

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteLibro(
            @PathVariable Long id) {

        try {

            libroService.eliminar(id);

            return ResponseEntity
                    .ok("Libro eliminado correctamente");

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body("Error al eliminar libro: "
                            + e.getMessage());
        }
    }

    
}