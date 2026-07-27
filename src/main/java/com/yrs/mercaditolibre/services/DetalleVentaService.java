package com.yrs.mercaditolibre.services;


import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.yrs.mercaditolibre.modelo.DetalleVentaEntity;
import com.yrs.mercaditolibre.repository.DetalleVentaRepository;
import lombok.RequiredArgsConstructor;

@Service @RequiredArgsConstructor
public class DetalleVentaService {
    private final DetalleVentaRepository repository;

    @Transactional(readOnly = true)
    public List<DetalleVentaEntity> ObtenerTodos() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public DetalleVentaEntity ObtenerPorId(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Detalle de venta no encontrado " + id));
    }

    @Transactional
    public DetalleVentaEntity guardarDetalleVenta(DetalleVentaEntity detalleVenta) {
        return repository.save(detalleVenta);
        // aqui pueden ir todas las validaciones que se requieran para guardar un detalle de venta
    }

    @Transactional
    public void eliminarDetalleVenta(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar el detalle de venta");
        }
        repository.deleteById(id);
    }

    // actualizar detalle de venta
    @Transactional
    public DetalleVentaEntity actualizarDetalleVenta(Long id, DetalleVentaEntity detalleVentaEntity) {
        DetalleVentaEntity detalleExistente = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Detalle de venta no existente !"));

        BeanUtils.copyProperties(detalleVentaEntity, detalleExistente, "id");
        return repository.save(detalleExistente);
    }
}