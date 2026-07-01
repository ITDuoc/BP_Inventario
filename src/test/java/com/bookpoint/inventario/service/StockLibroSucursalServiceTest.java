package com.bookpoint.inventario.service;

import com.bookpoint.inventario.dto.DistribucionLibroDTO;
import com.bookpoint.inventario.dto.TransferenciaLibroDTO;
import com.bookpoint.inventario.model.Libro;
import com.bookpoint.inventario.model.StockLibroSucursal;
import com.bookpoint.inventario.repository.LibroRepository;
import com.bookpoint.inventario.repository.StockLibroSucursalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockLibroSucursalServiceTest {

    @Mock
    private StockLibroSucursalRepository stockRepository;

    @Mock
    private LibroRepository libroRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private StockLibroSucursalService service;

    @BeforeEach
    void setup() {
        try {
            var field = StockLibroSucursalService.class.getDeclaredField("restTemplate");
            field.setAccessible(true);
            field.set(service, restTemplate);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        lenient()
                .when(restTemplate.getForObject(anyString(), eq(Object.class)))
                .thenReturn(new Object());
    }

    @Test
    void listar() {
        when(stockRepository.findAll())
                .thenReturn(List.of(new StockLibroSucursal()));

        List<StockLibroSucursal> result = service.listar();

        assertEquals(1, result.size());
    }

    @Test
    void listarPorSucursal() {
        when(stockRepository.findBySucursalId(1L))
                .thenReturn(List.of(new StockLibroSucursal()));

        List<StockLibroSucursal> result = service.listarPorSucursal(1L);

        assertEquals(1, result.size());
    }

    @Test
    void distribuirLibro_libroNoExiste() {
        DistribucionLibroDTO dto = new DistribucionLibroDTO();
        dto.setLibroId(1L);
        dto.setSucursalId(1L);
        dto.setCantidad(2);

        when(libroRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.distribuirLibro(dto));

        assertEquals("Libro no encontrado", ex.getMessage());
    }

    @Test
    void distribuirLibro_stockInsuficiente() {
        DistribucionLibroDTO dto = new DistribucionLibroDTO();
        dto.setLibroId(1L);
        dto.setSucursalId(1L);
        dto.setCantidad(10);

        Libro libro = new Libro();
        libro.setStockBodega(5);

        when(libroRepository.findById(1L)).thenReturn(Optional.of(libro));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.distribuirLibro(dto));

        assertEquals("Stock insuficiente en bodega", ex.getMessage());
    }

    @Test
    void distribuirLibro_nuevoStock() {
        DistribucionLibroDTO dto = new DistribucionLibroDTO();
        dto.setLibroId(1L);
        dto.setSucursalId(1L);
        dto.setCantidad(2);

        Libro libro = new Libro();
        libro.setStockBodega(10);

        when(libroRepository.findById(1L)).thenReturn(Optional.of(libro));
        when(stockRepository.findByLibroIdAndSucursalId(1L, 1L))
                .thenReturn(Optional.empty());
        when(stockRepository.save(any())).thenReturn(new StockLibroSucursal());

        String result = service.distribuirLibro(dto);

        assertEquals("Distribucion realizada correctamente", result);
    }

    @Test
    void distribuirLibro_stockExistente() {
        DistribucionLibroDTO dto = new DistribucionLibroDTO();
        dto.setLibroId(1L);
        dto.setSucursalId(1L);
        dto.setCantidad(2);

        Libro libro = new Libro();
        libro.setStockBodega(10);

        StockLibroSucursal stock = new StockLibroSucursal();
        stock.setStock(5);

        when(libroRepository.findById(1L)).thenReturn(Optional.of(libro));
        when(stockRepository.findByLibroIdAndSucursalId(1L, 1L))
                .thenReturn(Optional.of(stock));
        when(stockRepository.save(any())).thenReturn(stock);

        String result = service.distribuirLibro(dto);

        assertEquals("Distribucion realizada correctamente", result);
    }

    @Test
    void transferirLibro_noExisteOrigen() {
        TransferenciaLibroDTO dto = new TransferenciaLibroDTO();
        dto.setLibroId(1L);
        dto.setSucursalOrigenId(1L);
        dto.setSucursalDestinoId(2L);
        dto.setCantidad(2);

        when(stockRepository.findByLibroIdAndSucursalId(1L, 1L))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.transferirLibro(dto));

        assertEquals("No existe stock en sucursal origen", ex.getMessage());
    }

    @Test
    void transferirLibro_existeDestino() {
        TransferenciaLibroDTO dto = new TransferenciaLibroDTO();
        dto.setLibroId(1L);
        dto.setSucursalOrigenId(1L);
        dto.setSucursalDestinoId(2L);
        dto.setCantidad(2);

        StockLibroSucursal origen = new StockLibroSucursal();
        origen.setStock(10);

        StockLibroSucursal destino = new StockLibroSucursal();
        destino.setStock(3);

        when(stockRepository.findByLibroIdAndSucursalId(1L, 1L))
                .thenReturn(Optional.of(origen));

        when(stockRepository.findByLibroIdAndSucursalId(1L, 2L))
                .thenReturn(Optional.of(destino));

        when(stockRepository.save(any())).thenReturn(destino);

        String result = service.transferirLibro(dto);

        assertEquals("Transferencia realizada correctamente", result);
    }

    @Test
    void verificarStockMinimo_sinAlerta() {
        StockLibroSucursal stock = new StockLibroSucursal();
        stock.setLibroId(1L);
        stock.setStock(10);
        stock.setStockMinimo(5);

        when(stockRepository.findBySucursalId(1L))
                .thenReturn(List.of(stock));

        String result = service.verificarStockMinimo(1L);

        assertEquals("No hay alertas de stock minimo", result);
    }

    @Test
    void verificarStockMinimo_conAlerta() {
        StockLibroSucursal stock = new StockLibroSucursal();
        stock.setLibroId(1L);
        stock.setStock(2);
        stock.setStockMinimo(5);

        when(stockRepository.findBySucursalId(1L))
                .thenReturn(List.of(stock));

        String result = service.verificarStockMinimo(1L);

        assertTrue(result.contains("Libro ID 1"));
    }
}