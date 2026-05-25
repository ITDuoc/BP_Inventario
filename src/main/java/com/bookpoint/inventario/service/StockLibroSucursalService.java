package com.bookpoint.inventario.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bookpoint.inventario.dto.DistribucionLibroDTO;
import com.bookpoint.inventario.dto.TransferenciaLibroDTO;

import com.bookpoint.inventario.model.Libro;
import com.bookpoint.inventario.model.StockLibroSucursal;

import com.bookpoint.inventario.repository.LibroRepository;
import com.bookpoint.inventario.repository.StockLibroSucursalRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional

public class StockLibroSucursalService {

    @Autowired
    private StockLibroSucursalRepository stockRepository;

    @Autowired
    private LibroRepository libroRepository;

    private final RestTemplate restTemplate =
            new RestTemplate();

    private final String SUCURSALES_URL =
            "http://localhost:8093/api/v1/sucursales";

    public List<StockLibroSucursal> listar() {

        return stockRepository.findAll();
    }

    public List<StockLibroSucursal>
    listarPorSucursal(Long sucursalId) {

        validarSucursal(sucursalId);

        return stockRepository.findBySucursalId(
                sucursalId);
    }

    public String distribuirLibro(
            DistribucionLibroDTO dto) {

        validarSucursal(dto.getSucursalId());

        Libro libro =
                libroRepository.findById(
                        dto.getLibroId())
                .orElse(null);

        if (libro == null) {

            throw new RuntimeException(
                    "Libro no encontrado");
        }

        if (libro.getStockBodega()
                < dto.getCantidad()) {

            throw new RuntimeException(
                    "Stock insuficiente en bodega");
        }

        libro.setStockBodega(
                libro.getStockBodega()
                - dto.getCantidad());

        libroRepository.save(libro);

        Optional<StockLibroSucursal>
                stockExistente =

                stockRepository
                .findByLibroIdAndSucursalId(

                        dto.getLibroId(),
                        dto.getSucursalId());

        if (stockExistente.isPresent()) {

            StockLibroSucursal stock =
                    stockExistente.get();

            stock.setStock(
                    stock.getStock()
                    + dto.getCantidad());

            stockRepository.save(stock);

        } else {

            StockLibroSucursal nuevoStock =
                    new StockLibroSucursal();

            nuevoStock.setLibroId(
                    dto.getLibroId());

            nuevoStock.setSucursalId(
                    dto.getSucursalId());

            nuevoStock.setStock(
                    dto.getCantidad());

            nuevoStock.setStockMinimo(5);

            stockRepository.save(
                    nuevoStock);
        }

        return
                "Distribucion realizada correctamente";
    }

    public String transferirLibro(
            TransferenciaLibroDTO dto) {

        validarSucursal(
                dto.getSucursalOrigenId());

        validarSucursal(
                dto.getSucursalDestinoId());

        StockLibroSucursal stockOrigen =

                stockRepository
                .findByLibroIdAndSucursalId(

                        dto.getLibroId(),
                        dto.getSucursalOrigenId())

                .orElseThrow(() ->

                        new RuntimeException(

                                "No existe stock en sucursal origen"));

        if (stockOrigen.getStock()
                < dto.getCantidad()) {

            throw new RuntimeException(
                    "Stock insuficiente en sucursal origen");
        }

        stockOrigen.setStock(

                stockOrigen.getStock()
                - dto.getCantidad());

        stockRepository.save(
                stockOrigen);

        Optional<StockLibroSucursal>
                stockDestinoExistente =

                stockRepository
                .findByLibroIdAndSucursalId(

                        dto.getLibroId(),
                        dto.getSucursalDestinoId());

        if (stockDestinoExistente.isPresent()) {

            StockLibroSucursal stockDestino =
                    stockDestinoExistente.get();

            stockDestino.setStock(

                    stockDestino.getStock()
                    + dto.getCantidad());

            stockRepository.save(
                    stockDestino);

        } else {

            StockLibroSucursal nuevoStock =
                    new StockLibroSucursal();

            nuevoStock.setLibroId(
                    dto.getLibroId());

            nuevoStock.setSucursalId(
                    dto.getSucursalDestinoId());

            nuevoStock.setStock(
                    dto.getCantidad());

            nuevoStock.setStockMinimo(5);

            stockRepository.save(
                    nuevoStock);
        }

        return
                "Transferencia realizada correctamente";
    }

    public String verificarStockMinimo(
            Long sucursalId) {

        validarSucursal(sucursalId);

        List<StockLibroSucursal> stocks =

                stockRepository
                .findBySucursalId(
                        sucursalId);

        String alerta = "";

        for (StockLibroSucursal stock
                : stocks) {

            if (stock.getStock()
                    <= stock.getStockMinimo()) {

                alerta +=
                        "Libro ID "
                        + stock.getLibroId()
                        + " con stock minimo.\n";
            }
        }

        if (alerta.isEmpty()) {

            return
                    "No hay alertas de stock minimo";
        }

        return alerta;
    }

    private void validarSucursal(
            Long sucursalId) {

        try {

            Object sucursal =

                    restTemplate.getForObject(

                            SUCURSALES_URL
                            + "/"
                            + sucursalId,

                            Object.class);

            if (sucursal == null) {

                throw new RuntimeException(
                        "Sucursal no encontrada");
            }

        } catch (Exception e) {

            throw new RuntimeException(
                    "Sucursal no encontrada");
        }
    }
}