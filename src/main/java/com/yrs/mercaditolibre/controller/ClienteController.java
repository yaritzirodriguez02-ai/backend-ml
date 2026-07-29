package com.yrs.mercaditolibre.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yrs.mercaditolibre.modelo.ClienteEntity;
import com.yrs.mercaditolibre.modelo.ProductoEntity;
import com.yrs.mercaditolibre.repository.ProductoRepository;
import com.yrs.mercaditolibre.services.ClienteService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/clientes") // mapeo general de clientes
//@CrossOrigin(origins = "http://localhost:5173") // permiso a react
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService servicio;
    private final ProductoRepository productoRepository; // Repositorio inyectado para consultar las compras

    // endpoint para ver todos los clientes
    @GetMapping("/")
    public ResponseEntity<List<ClienteEntity>> Listar() {
        return ResponseEntity.ok(servicio.ObtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteEntity> ObtenerDetalles(@PathVariable Long id) {
        return ResponseEntity.ok(servicio.ObtenerPorId(id));
    }

    // eliminar por id
    @DeleteMapping("/{id}")
    public ResponseEntity<ClienteEntity> eliminar(@PathVariable Long id) {
        servicio.eliminarCliente(id);
        return ResponseEntity.noContent().build(); // esto retornara un mensaje 204 no content
    }

    // agregar
    @PostMapping
    public ResponseEntity<ClienteEntity> crearCliente(@RequestBody ClienteEntity cliente) {
        ClienteEntity nuevo = servicio.guardarCliente(cliente);
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED); // crear 201
    }

    // actualizar perfil / cliente
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody ClienteEntity cliente) {
        try {
            ClienteEntity clienteAct = servicio.actualizarCliente(id, cliente);
            return ResponseEntity.ok(clienteAct);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    // NUEVO ENDPOINT: Obtener compras del cliente logueado (Para clienteDashboard.jsx)
    @GetMapping("/{clienteId}/mis-compras")
    public ResponseEntity<List<ProductoEntity>> obtenerMisCompras(@PathVariable Long clienteId) {
        List<ProductoEntity> productos = productoRepository.findProductosCompradosPorCliente(clienteId);
        return ResponseEntity.ok(productos);
    }
}