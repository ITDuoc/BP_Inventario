package com.bookpoint.inventario.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class DistribucionLibroDTO {

    @NotNull(message = "El id del libro es obligatorio")
    private Long libroId;

    @NotNull(message = "El id de la sucursal es obligatorio")
    private Long sucursalId;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;
}