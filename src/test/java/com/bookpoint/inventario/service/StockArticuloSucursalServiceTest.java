package com.bookpoint.inventario.service;

import com.bookpoint.inventario.dto.DistribucionArticuloDTO;
import com.bookpoint.inventario.dto.TransferenciaArticuloDTO;
import com.bookpoint.inventario.model.Articulo;
import com.bookpoint.inventario.model.StockArticuloSucursal;
import com.bookpoint.inventario.repository.ArticuloRepository;
import com.bookpoint.inventario.repository.StockArticuloSucursalRepository;
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
class StockArticuloSucursalServiceTest {

    @Mock
    private StockArticuloSucursalRepository stockRepository;

    @Mock
    private ArticuloRepository articuloRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private StockArticuloSucursalService service;

    @BeforeEach
    void setup() {
        try {
            var field = StockArticuloSucursalService.class.getDeclaredField("restTemplate");
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
        when(stockRepository.findAll()).thenReturn(List.of(new StockArticuloSucursal()));

        List<StockArticuloSucursal> result = service.listar();

        assertEquals(1, result.size());
        verify(stockRepository).findAll();
    }

    @Test
    void listarPorSucursal() {
        when(stockRepository.findBySucursalId(1L))
                .thenReturn(List.of(new StockArticuloSucursal()));

        List<StockArticuloSucursal> result = service.listarPorSucursal(1L);

        assertEquals(1, result.size());
    }

    @Test
    void distribuirArticulo_articuloNoExiste() {
        DistribucionArticuloDTO dto = new DistribucionArticuloDTO();
        dto.setArticuloId(1L);
        dto.setSucursalId(1L);
        dto.setCantidad(2);

        when(articuloRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.distribuirArticulo(dto));

        assertEquals("Articulo no encontrado", ex.getMessage());
    }

    @Test
    void distribuirArticulo_stockInsuficiente() {
        DistribucionArticuloDTO dto = new DistribucionArticuloDTO();
        dto.setArticuloId(1L);
        dto.setSucursalId(1L);
        dto.setCantidad(10);

        Articulo articulo = new Articulo();
        articulo.setStockBodega(5);

        when(articuloRepository.findById(1L)).thenReturn(Optional.of(articulo));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.distribuirArticulo(dto));

        assertEquals("Stock insuficiente en bodega", ex.getMessage());
    }

    @Test
    void distribuirArticulo_nuevoStock() {
        DistribucionArticuloDTO dto = new DistribucionArticuloDTO();
        dto.setArticuloId(1L);
        dto.setSucursalId(1L);
        dto.setCantidad(2);

        Articulo articulo = new Articulo();
        articulo.setStockBodega(10);

        when(articuloRepository.findById(1L)).thenReturn(Optional.of(articulo));
        when(stockRepository.findByArticuloIdAndSucursalId(1L, 1L))
                .thenReturn(Optional.empty());
        when(stockRepository.save(any())).thenReturn(new StockArticuloSucursal());

        String result = service.distribuirArticulo(dto);

        assertEquals("Distribucion realizada correctamente", result);
    }

    @Test
    void distribuirArticulo_stockExistente() {
        DistribucionArticuloDTO dto = new DistribucionArticuloDTO();
        dto.setArticuloId(1L);
        dto.setSucursalId(1L);
        dto.setCantidad(2);

        Articulo articulo = new Articulo();
        articulo.setStockBodega(10);

        StockArticuloSucursal stock = new StockArticuloSucursal();
        stock.setStock(5);

        when(articuloRepository.findById(1L)).thenReturn(Optional.of(articulo));
        when(stockRepository.findByArticuloIdAndSucursalId(1L, 1L))
                .thenReturn(Optional.of(stock));
        when(stockRepository.save(any())).thenReturn(stock);

        String result = service.distribuirArticulo(dto);

        assertEquals("Distribucion realizada correctamente", result);
    }

    @Test
    void transferirArticulo_noExisteOrigen() {
        TransferenciaArticuloDTO dto = new TransferenciaArticuloDTO();
        dto.setArticuloId(1L);
        dto.setSucursalOrigenId(1L);
        dto.setSucursalDestinoId(2L);
        dto.setCantidad(2);

        when(stockRepository.findByArticuloIdAndSucursalId(1L, 1L))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.transferirArticulo(dto));

        assertEquals("No existe stock en sucursal origen", ex.getMessage());
    }

    @Test
    void transferirArticulo_existeDestino() {
        TransferenciaArticuloDTO dto = new TransferenciaArticuloDTO();
        dto.setArticuloId(1L);
        dto.setSucursalOrigenId(1L);
        dto.setSucursalDestinoId(2L);
        dto.setCantidad(2);

        StockArticuloSucursal origen = new StockArticuloSucursal();
        origen.setStock(10);

        StockArticuloSucursal destino = new StockArticuloSucursal();
        destino.setStock(3);

        when(stockRepository.findByArticuloIdAndSucursalId(1L, 1L))
                .thenReturn(Optional.of(origen));
        when(stockRepository.findByArticuloIdAndSucursalId(1L, 2L))
                .thenReturn(Optional.of(destino));
        when(stockRepository.save(any())).thenReturn(destino);

        String result = service.transferirArticulo(dto);

        assertEquals("Transferencia realizada correctamente", result);
    }

    @Test
    void verificarStockMinimo_sinAlerta() {
        StockArticuloSucursal stock = new StockArticuloSucursal();
        stock.setArticuloId(1L);
        stock.setStock(10);
        stock.setStockMinimo(5);

        when(stockRepository.findBySucursalId(1L))
                .thenReturn(List.of(stock));

        String result = service.verificarStockMinimo(1L);

        assertEquals("No hay alertas de stock minimo", result);
    }

    @Test
    void verificarStockMinimo_conAlerta() {
        StockArticuloSucursal stock = new StockArticuloSucursal();
        stock.setArticuloId(1L);
        stock.setStock(2);
        stock.setStockMinimo(5);

        when(stockRepository.findBySucursalId(1L))
                .thenReturn(List.of(stock));

        String result = service.verificarStockMinimo(1L);

        assertTrue(result.contains("Articulo ID 1"));
    }
}