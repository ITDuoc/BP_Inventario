package com.bookpoint.inventario.model;

import jakarta.persistence.*;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "stock_articulo_sucursal")

public class StockArticuloSucursal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El id del articulo es obligatorio")
    @Column(nullable = false)
    private Long articuloId;

    @NotNull(message = "El id de la sucursal es obligatorio")
    @Column(nullable = false)
    private Long sucursalId;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    @Column(nullable = false)
    private Integer stock;

    @NotNull(message = "El stock minimo es obligatorio")
    @Min(value = 0, message = "El stock minimo no puede ser negativo")
    @Column(nullable = false)
    private Integer stockMinimo;
}