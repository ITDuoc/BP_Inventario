package com.bookpoint.inventario.service;

import com.bookpoint.inventario.model.Articulo;
import com.bookpoint.inventario.repository.ArticuloRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArticuloServiceTest {

    @Mock
    private ArticuloRepository articuloRepository;

    @InjectMocks
    private ArticuloService articuloService;

    @Test
    void crear() {
        Articulo articulo = new Articulo();
        when(articuloRepository.save(articulo)).thenReturn(articulo);

        Articulo result = articuloService.crear(articulo);

        assertNotNull(result);
        verify(articuloRepository).save(articulo);
    }

    @Test
    void listar() {
        when(articuloRepository.findAll()).thenReturn(List.of(new Articulo()));

        List<Articulo> result = articuloService.listar();

        assertEquals(1, result.size());
        verify(articuloRepository).findAll();
    }

    @Test
    void buscarPorId_existe() {
        Articulo articulo = new Articulo();
        when(articuloRepository.findById(1L)).thenReturn(Optional.of(articulo));

        Articulo result = articuloService.buscarPorId(1L);

        assertNotNull(result);
    }

    @Test
    void buscarPorId_noExiste() {
        when(articuloRepository.findById(1L)).thenReturn(Optional.empty());

        Articulo result = articuloService.buscarPorId(1L);

        assertNull(result);
    }

    @Test
    void modificar_existe() {
        Articulo existente = new Articulo();
        Articulo nuevo = new Articulo();
        nuevo.setNombre("A");
        nuevo.setMarca("B");
        nuevo.setPrecio(10);
        nuevo.setUbicacionBodega("X");
        nuevo.setStockBodega(5);
        nuevo.setMostrarCatalogo(true);

        when(articuloRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(articuloRepository.save(any())).thenReturn(existente);

        Articulo result = articuloService.modificar(1L, nuevo);

        assertNotNull(result);
        verify(articuloRepository).save(existente);
    }

    @Test
    void modificar_noExiste() {
        when(articuloRepository.findById(1L)).thenReturn(Optional.empty());

        Articulo result = articuloService.modificar(1L, new Articulo());

        assertNull(result);
    }

    @Test
    void eliminar() {
        articuloService.eliminar(1L);
        verify(articuloRepository).deleteById(1L);
    }

    @Test
    void sumarStock_existe() {
        Articulo articulo = new Articulo();
        articulo.setStockBodega(10);

        when(articuloRepository.findById(1L)).thenReturn(Optional.of(articulo));
        when(articuloRepository.save(any())).thenReturn(articulo);

        Articulo result = articuloService.sumarStock(1L, 5);

        assertNotNull(result);
        assertEquals(15, articulo.getStockBodega());
    }

    @Test
    void sumarStock_noExiste() {
        when(articuloRepository.findById(1L)).thenReturn(Optional.empty());

        Articulo result = articuloService.sumarStock(1L, 5);

        assertNull(result);
    }
}