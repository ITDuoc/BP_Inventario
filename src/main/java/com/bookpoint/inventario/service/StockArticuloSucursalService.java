package com.bookpoint.inventario.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bookpoint.inventario.dto.DistribucionArticuloDTO;
import com.bookpoint.inventario.dto.TransferenciaArticuloDTO;

import com.bookpoint.inventario.model.Articulo;
import com.bookpoint.inventario.model.StockArticuloSucursal;

import com.bookpoint.inventario.repository.ArticuloRepository;
import com.bookpoint.inventario.repository
        .StockArticuloSucursalRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional

public class StockArticuloSucursalService {

    @Autowired
    private StockArticuloSucursalRepository
            stockRepository;

    @Autowired
    private ArticuloRepository articuloRepository;

    private final RestTemplate restTemplate =
            new RestTemplate();

    private final String SUCURSALES_URL =
            "http://localhost:8093/api/v1/sucursales";

    public List<StockArticuloSucursal> listar() {

        return stockRepository.findAll();
    }

    public List<StockArticuloSucursal>
    listarPorSucursal(Long sucursalId) {

        validarSucursal(sucursalId);

        return stockRepository.findBySucursalId(
                sucursalId);
    }

    public String distribuirArticulo(
            DistribucionArticuloDTO dto) {

        validarSucursal(dto.getSucursalId());

        Articulo articulo =
                articuloRepository.findById(
                        dto.getArticuloId())
                .orElse(null);

        if (articulo == null) {

            throw new RuntimeException(
                    "Articulo no encontrado");
        }

        if (articulo.getStockBodega()
                < dto.getCantidad()) {

            throw new RuntimeException(
                    "Stock insuficiente en bodega");
        }

        articulo.setStockBodega(
                articulo.getStockBodega()
                - dto.getCantidad());

        articuloRepository.save(articulo);

        Optional<StockArticuloSucursal>
                stockExistente =

                stockRepository
                .findByArticuloIdAndSucursalId(

                        dto.getArticuloId(),
                        dto.getSucursalId());

        if (stockExistente.isPresent()) {

            StockArticuloSucursal stock =
                    stockExistente.get();

            stock.setStock(
                    stock.getStock()
                    + dto.getCantidad());

            stockRepository.save(stock);

        } else {

            StockArticuloSucursal nuevoStock =
                    new StockArticuloSucursal();

            nuevoStock.setArticuloId(
                    dto.getArticuloId());

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

    public String transferirArticulo(
            TransferenciaArticuloDTO dto) {

        validarSucursal(
                dto.getSucursalOrigenId());

        validarSucursal(
                dto.getSucursalDestinoId());

        StockArticuloSucursal stockOrigen =

                stockRepository
                .findByArticuloIdAndSucursalId(

                        dto.getArticuloId(),
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

        Optional<StockArticuloSucursal>
                stockDestinoExistente =

                stockRepository
                .findByArticuloIdAndSucursalId(

                        dto.getArticuloId(),
                        dto.getSucursalDestinoId());

        if (stockDestinoExistente.isPresent()) {

            StockArticuloSucursal stockDestino =
                    stockDestinoExistente.get();

            stockDestino.setStock(

                    stockDestino.getStock()
                    + dto.getCantidad());

            stockRepository.save(
                    stockDestino);

        } else {

            StockArticuloSucursal nuevoStock =
                    new StockArticuloSucursal();

            nuevoStock.setArticuloId(
                    dto.getArticuloId());

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

        List<StockArticuloSucursal> stocks =

                stockRepository
                .findBySucursalId(
                        sucursalId);

        String alerta = "";

        for (StockArticuloSucursal stock
                : stocks) {

            if (stock.getStock()
                    <= stock.getStockMinimo()) {

                alerta +=
                        "Articulo ID "
                        + stock.getArticuloId()
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