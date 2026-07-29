package com.yrs.mercaditolibre.controller;

import java.security.Principal;
import java.util.List;
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

import com.yrs.mercaditolibre.modelo.VentasEntity;
import com.yrs.mercaditolibre.services.ProcesarVentaService;
import com.yrs.mercaditolibre.services.VentasService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/ventas") // mapeo general de ventas
//@CrossOrigin(origins = "http://localhost:5173") // permiso a react
@RequiredArgsConstructor
public class VentasController {

    private final VentasService servicio;
    private final ProcesarVentaService serpProcesarVenta;

    // Endpoint para crear la venta asociando al usuario logueado (Como el maestro)
    @PostMapping
    public ResponseEntity<?> crearVenta(@RequestBody VentasEntity venta, Principal principal) {
        try {
            String email = principal.getName(); 
            VentasEntity nuevaVenta = serpProcesarVenta.ProcesarVenta(venta, email); 
            return ResponseEntity.ok(nuevaVenta);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // Endpoint para ver todas las ventas (Admin)
    @GetMapping
    public ResponseEntity<List<VentasEntity>> listarTodas() {
        return ResponseEntity.ok(servicio.ObtenerTodos());
    }

    // ---  MÉTODO  ---
    @GetMapping("/mis-compras")
    public ResponseEntity<List<VentasEntity>> listarMisCompras(Principal principal) {
        String email = principal.getName();
        return ResponseEntity.ok(servicio.obtenerVentasPorCliente(email));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VentasEntity> ObtenerDetalles(@PathVariable Long id) {
        return ResponseEntity.ok(servicio.ObtenerPorId(id));
    }

    // Eliminar por id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        servicio.eliminarVenta(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    // Actualizar
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody VentasEntity venta) {
        try {
            VentasEntity ventaAct = servicio.actualizarVenta(id, venta);
            return ResponseEntity.ok(ventaAct);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

}