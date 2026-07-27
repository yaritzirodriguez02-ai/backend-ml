package com.yrs.mercaditolibre.services;


import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.yrs.mercaditolibre.modelo.ClienteEntity;
import com.yrs.mercaditolibre.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;

@Service @RequiredArgsConstructor
public class ClienteService {
    private final ClienteRepository repository;

    @Transactional(readOnly = true)
    public List<ClienteEntity> ObtenerTodos() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public ClienteEntity ObtenerPorId(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Cliente no encontrado " + id));
    }

    @Transactional
    public ClienteEntity guardarCliente(ClienteEntity cliente) {
        return repository.save(cliente);
        // aqui pueden ir todas las validaciones que se requieran para guardar un cliente
    }

    @Transactional
    public void eliminarCliente(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar el cliente");
        }
        repository.deleteById(id);
    }

    // actualizar cliente
    @Transactional
    public ClienteEntity actualizarCliente(Long id, ClienteEntity detalleClienteEntity) {
        ClienteEntity clienteExistente = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cliente no existente !"));

        BeanUtils.copyProperties(detalleClienteEntity, clienteExistente, "id");
        return repository.save(clienteExistente);
    }
}