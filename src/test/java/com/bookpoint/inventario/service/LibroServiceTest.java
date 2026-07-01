package com.bookpoint.inventario.service;

import com.bookpoint.inventario.model.Libro;
import com.bookpoint.inventario.repository.LibroRepository;
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
class LibroServiceTest {

    @Mock
    private LibroRepository libroRepository;

    @InjectMocks
    private LibroService libroService;

    @Test
    void crear() {
        Libro libro = new Libro();
        when(libroRepository.save(libro)).thenReturn(libro);

        Libro result = libroService.crear(libro);

        assertNotNull(result);
        verify(libroRepository).save(libro);
    }

    @Test
    void listar() {
        when(libroRepository.findAll()).thenReturn(List.of(new Libro()));

        List<Libro> result = libroService.listar();

        assertEquals(1, result.size());
        verify(libroRepository).findAll();
    }

    @Test
    void buscarPorId_existe() {
        Libro libro = new Libro();
        when(libroRepository.findById(1L)).thenReturn(Optional.of(libro));

        Libro result = libroService.buscarPorId(1L);

        assertNotNull(result);
    }

    @Test
    void buscarPorId_noExiste() {
        when(libroRepository.findById(1L)).thenReturn(Optional.empty());

        Libro result = libroService.buscarPorId(1L);

        assertNull(result);
    }

    @Test
    void modificar_existe() {
        Libro existente = new Libro();
        Libro nuevo = new Libro();

        nuevo.setTitulo("Titulo");
        nuevo.setAutor("Autor");
        nuevo.setEditorial("Editorial");
        nuevo.setGenero("Genero");
        nuevo.setPrecio(100);
        nuevo.setUbicacionBodega("B1");
        nuevo.setStockBodega(5);
        nuevo.setMostrarCatalogo(true);

        when(libroRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(libroRepository.save(any())).thenReturn(existente);

        Libro result = libroService.modificar(1L, nuevo);

        assertNotNull(result);
        verify(libroRepository).save(existente);
    }

    @Test
    void modificar_noExiste() {
        when(libroRepository.findById(1L)).thenReturn(Optional.empty());

        Libro result = libroService.modificar(1L, new Libro());

        assertNull(result);
    }

    @Test
    void eliminar() {
        libroService.eliminar(1L);
        verify(libroRepository).deleteById(1L);
    }

    @Test
    void sumarStock_existe() {
        Libro libro = new Libro();
        libro.setStockBodega(10);

        when(libroRepository.findById(1L)).thenReturn(Optional.of(libro));
        when(libroRepository.save(any())).thenReturn(libro);

        Libro result = libroService.sumarStock(1L, 5);

        assertNotNull(result);
        assertEquals(15, libro.getStockBodega());
    }

    @Test
    void sumarStock_noExiste() {
        when(libroRepository.findById(1L)).thenReturn(Optional.empty());

        Libro result = libroService.sumarStock(1L, 5);

        assertNull(result);
    }
}