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

import com.yrs.mercaditolibre.modelo.ProveedorEntity;
import com.yrs.mercaditolibre.services.ProveedorService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/proveedores") // mapeo general de proveedores
//@CrossOrigin(origins = "http://localhost:5173") // permiso a react
@RequiredArgsConstructor
public class ProveedorController {

    private final ProveedorService servicio;

    // endpoint para ver todos los proveedores
    @GetMapping("/")
    public ResponseEntity<List<ProveedorEntity>> Listar() {
        return ResponseEntity.ok(servicio.ObtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProveedorEntity> ObtenerDetalles(@PathVariable Long id) {
        return ResponseEntity.ok(servicio.ObtenerPorId(id));
    }

    // eliminar por id
    @DeleteMapping("/{id}")
    public ResponseEntity<ProveedorEntity> eliminar(@PathVariable Long id) {
        servicio.eliminarProveedor(id);
        return ResponseEntity.noContent().build(); // esto retornara un mensaje 204 no content
    }

    // agregar
    @PostMapping
    public ResponseEntity<ProveedorEntity> crearProveedor(@RequestBody ProveedorEntity proveedor) {
        ProveedorEntity nuevo = servicio.guardarProveedor(proveedor);
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED); // crear 201
    }

    // actualizar
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody ProveedorEntity proveedor) {
        try {
            ProveedorEntity proveedorAct = servicio.actualizarProveedor(id, proveedor);
            return ResponseEntity.ok(proveedorAct);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

}