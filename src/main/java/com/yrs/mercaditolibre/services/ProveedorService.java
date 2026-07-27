package com.yrs.mercaditolibre.services;


import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.yrs.mercaditolibre.modelo.ProveedorEntity;
import com.yrs.mercaditolibre.repository.ProveedorRepository;
import lombok.RequiredArgsConstructor;

@Service @RequiredArgsConstructor
public class ProveedorService {
    private final ProveedorRepository repository;

    @Transactional(readOnly = true)
    public List<ProveedorEntity> ObtenerTodos() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public ProveedorEntity ObtenerPorId(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Proveedor no encontrado " + id));
    }

    @Transactional
    public ProveedorEntity guardarProveedor(ProveedorEntity proveedor) {
        return repository.save(proveedor);
        // aqui pueden ir todas las validaciones que se requieran para guardar un proveedor
    }

    @Transactional
    public void eliminarProveedor(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar el proveedor");
        }
        repository.deleteById(id);
    }

    // actualizar proveedor
    @Transactional
    public ProveedorEntity actualizarProveedor(Long id, ProveedorEntity detalleProveedorEntity) {
        ProveedorEntity proveedorExistente = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Proveedor no existente !"));

        BeanUtils.copyProperties(detalleProveedorEntity, proveedorExistente, "id");
        return repository.save(proveedorExistente);
    }
}