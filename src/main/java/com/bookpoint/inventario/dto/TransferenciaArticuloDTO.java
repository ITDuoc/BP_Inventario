package com.bookpoint.inventario.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class TransferenciaArticuloDTO {

    @NotNull(message =
            "El id del articulo es obligatorio")
    private Long articuloId;

    @NotNull(message =
            "La sucursal origen es obligatoria")
    private Long sucursalOrigenId;

    @NotNull(message =
            "La sucursal destino es obligatoria")
    private Long sucursalDestinoId;

    @NotNull(message =
            "La cantidad es obligatoria")
    @Min(value = 1,
            message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;
}