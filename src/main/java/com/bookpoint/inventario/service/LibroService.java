package com.bookpoint.inventario.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookpoint.inventario.model.Libro;
import com.bookpoint.inventario.repository.LibroRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional

public class LibroService {

    @Autowired
    private LibroRepository libroRepository;

    public Libro crear(Libro libro) {

        return libroRepository.save(libro);
    }

    public List<Libro> listar() {

        return libroRepository.findAll();
    }

    public Libro buscarPorId(Long id) {

        return libroRepository.findById(id).orElse(null);
    }

    public Libro modificar(Long id, Libro libro) {

        Libro existente =
                libroRepository.findById(id).orElse(null);

        if (existente != null) {

            existente.setTitulo(libro.getTitulo());
            existente.setAutor(libro.getAutor());
            existente.setEditorial(libro.getEditorial());
            existente.setGenero(libro.getGenero());
            existente.setPrecio(libro.getPrecio());
            existente.setUbicacionBodega(
                    libro.getUbicacionBodega());
            existente.setStockBodega(
                    libro.getStockBodega());
            existente.setMostrarCatalogo(
                    libro.getMostrarCatalogo());

            return libroRepository.save(existente);
        }

        return null;
    }

    public void eliminar(Long id) {

        libroRepository.deleteById(id);
    }

    public Libro sumarStock(Long id, int cantidad) {

    Libro libro =
            libroRepository.findById(id).orElse(null);

    if (libro != null) {

        libro.setStockBodega(
                libro.getStockBodega() + cantidad);

        return libroRepository.save(libro);
    }

    return null;
}
}