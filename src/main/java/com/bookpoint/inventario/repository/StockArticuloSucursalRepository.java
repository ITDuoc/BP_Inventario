package com.bookpoint.inventario.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookpoint.inventario.model.StockArticuloSucursal;

public interface StockArticuloSucursalRepository
        extends JpaRepository<StockArticuloSucursal, Long> {

    Optional<StockArticuloSucursal>
    findByArticuloIdAndSucursalId(Long articuloId, Long sucursalId);

    List<StockArticuloSucursal>
    findBySucursalId(Long sucursalId);
}