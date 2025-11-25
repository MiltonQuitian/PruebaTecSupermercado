package com.todocodeacademy.PruebaTecSupermercado.service;

import com.todocodeacademy.PruebaTecSupermercado.dto.ProductoDTO;
import com.todocodeacademy.PruebaTecSupermercado.mapper.Mapper;
import com.todocodeacademy.PruebaTecSupermercado.model.DetalleVenta;
import com.todocodeacademy.PruebaTecSupermercado.model.Producto;
import com.todocodeacademy.PruebaTecSupermercado.model.Venta;
import com.todocodeacademy.PruebaTecSupermercado.repository.ProductoRepository;
import com.todocodeacademy.PruebaTecSupermercado.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EstadisticasService implements IEstadisticasService {

    @Autowired
    private VentaRepository ventaRepo;
    @Autowired
    private ProductoRepository productoRepo;

    @Override
    public ProductoDTO productoMasVendido() {
        List<Venta> ventas = ventaRepo.findAll();

        Map<Long, Integer> cantidadesProd = new HashMap<>();

        for (Venta v : ventas) {
            for (DetalleVenta d : v.getDetalle()) {

                Long idProd = d.getProd().getId();
                Integer cantidad = d.getCantProd();

                cantidadesProd.merge(idProd, cantidad, Integer::sum);
            }
        }

        Long idProdMasVendido = cantidadesProd.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        Producto productoMasVendido = null;

        if (idProdMasVendido != null) {
            productoMasVendido = productoRepo.findById(idProdMasVendido).orElse(null);
        }

        return Mapper.toDTO(productoMasVendido);
    }
}
