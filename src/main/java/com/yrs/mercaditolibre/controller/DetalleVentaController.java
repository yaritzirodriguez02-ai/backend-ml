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

import com.yrs.mercaditolibre.modelo.DetalleVentaEntity;
import com.yrs.mercaditolibre.services.DetalleVentaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/detalle-venta") // mapeo general de detalle de ventas
//@CrossOrigin(origins = "http://localhost:5173") // permiso a react
@RequiredArgsConstructor
public class DetalleVentaController {

    private final DetalleVentaService servicio;

    // endpoint para ver todos los detalles de venta
    @GetMapping("/")
    public ResponseEntity<List<DetalleVentaEntity>> Listar() {
        return ResponseEntity.ok(servicio.ObtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalleVentaEntity> ObtenerDetalles(@PathVariable Long id) {
        return ResponseEntity.ok(servicio.ObtenerPorId(id));
    }

    // eliminar por id
    @DeleteMapping("/{id}")
    public ResponseEntity<DetalleVentaEntity> eliminar(@PathVariable Long id) {
        servicio.eliminarDetalleVenta(id);
        return ResponseEntity.noContent().build(); // esto retornara un mensaje 204 no content
    }

    // agregar
    @PostMapping
    public ResponseEntity<DetalleVentaEntity> crearDetalleVenta(@RequestBody DetalleVentaEntity detalleVenta) {
        DetalleVentaEntity nuevo = servicio.guardarDetalleVenta(detalleVenta);
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED); // crear 201
    }

    // actualizar
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody DetalleVentaEntity detalleVenta) {
        try {
            DetalleVentaEntity detalleAct = servicio.actualizarDetalleVenta(id, detalleVenta);
            return ResponseEntity.ok(detalleAct);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

}