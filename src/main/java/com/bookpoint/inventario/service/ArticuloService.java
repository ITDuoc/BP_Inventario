package com.bookpoint.inventario.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookpoint.inventario.model.Articulo;
import com.bookpoint.inventario.repository.ArticuloRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional

public class ArticuloService {

    @Autowired
    private ArticuloRepository articuloRepository;

    public Articulo crear(Articulo articulo) {

        return articuloRepository.save(articulo);
    }

    public List<Articulo> listar() {

        return articuloRepository.findAll();
    }

    public Articulo buscarPorId(Long id) {

        return articuloRepository.findById(id).orElse(null);
    }

    public Articulo modificar(Long id, Articulo articulo) {

        Articulo existente =
                articuloRepository.findById(id).orElse(null);

        if (existente != null) {

            existente.setNombre(articulo.getNombre());
            existente.setMarca(articulo.getMarca());
            existente.setPrecio(articulo.getPrecio());
            existente.setUbicacionBodega(
                    articulo.getUbicacionBodega());
            existente.setStockBodega(
                    articulo.getStockBodega());
            existente.setMostrarCatalogo(
                    articulo.getMostrarCatalogo());

            return articuloRepository.save(existente);
        }

        return null;
    }

    public void eliminar(Long id) {

        articuloRepository.deleteById(id);
    }

    public Articulo sumarStock(Long id, int cantidad) {

    Articulo articulo =
            articuloRepository.findById(id).orElse(null);

    if (articulo != null) {

        articulo.setStockBodega(
                articulo.getStockBodega() + cantidad);

        return articuloRepository.save(articulo);
    }

    return null;
}
}