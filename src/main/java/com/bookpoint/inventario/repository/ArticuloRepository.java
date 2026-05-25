package com.bookpoint.inventario.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookpoint.inventario.model.Articulo;

public interface ArticuloRepository
        extends JpaRepository<Articulo, Long> {

}