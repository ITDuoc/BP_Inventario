package com.bookpoint.inventario.model;

import jakarta.persistence.*;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "libros")

public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El titulo es obligatorio")
    @Size(min = 2, max = 100,
            message = "El titulo debe tener entre 2 y 100 caracteres")
    @Column(nullable = false)
    private String titulo;

    @NotBlank(message = "El autor es obligatorio")
    @Column(nullable = false)
    private String autor;

    @NotBlank(message = "La editorial es obligatoria")
    @Column(nullable = false)
    private String editorial;

    @NotBlank(message = "El genero es obligatorio")
    @Column(nullable = false)
    private String genero;

    @NotNull(message = "El precio es obligatorio")
    @Min(value = 0, message = "El precio no puede ser negativo")
    @Column(nullable = false)
    private Integer precio;

    @NotBlank(message = "La ubicacion de bodega es obligatoria")
    @Column(nullable = false)
    private String ubicacionBodega;

    @NotNull(message = "El stock de bodega es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    @Column(nullable = false)
    private Integer stockBodega;

    @Column(nullable = false)
    private Boolean mostrarCatalogo;
}