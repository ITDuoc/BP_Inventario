package com.bookpoint.inventario.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookpoint.inventario.model.Libro;

public interface LibroRepository
        extends JpaRepository<Libro, Long> {

}