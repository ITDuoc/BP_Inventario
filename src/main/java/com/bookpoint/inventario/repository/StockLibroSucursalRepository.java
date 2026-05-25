package com.bookpoint.inventario.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookpoint.inventario.model.StockLibroSucursal;

public interface StockLibroSucursalRepository
        extends JpaRepository<StockLibroSucursal, Long> {

    Optional<StockLibroSucursal>
    findByLibroIdAndSucursalId(Long libroId, Long sucursalId);

    List<StockLibroSucursal>
    findBySucursalId(Long sucursalId);
}