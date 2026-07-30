package com.yrs.mercaditolibre.controller;

import java.security.Principal;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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

    // Generar ticket HTML de una venta
    @GetMapping("/{id}/ticket")
    public ResponseEntity<String> generarTicket(@PathVariable Long id) {
        VentasEntity venta = servicio.ObtenerPorId(id);
        
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html lang='es'><head><meta charset='UTF-8'>")
            .append("<title>Ticket #").append(venta.getId()).append(" - Mercadito Libre</title>")
            .append("<style>")
            .append("* { margin: 0; padding: 0; box-sizing: border-box; }")
            .append("body { font-family: 'Courier New', monospace; background: #0a0a12; color: #e4d8f5; padding: 40px; display: flex; justify-content: center; }")
            .append(".ticket { max-width: 480px; width: 100%; background: #13131f; border: 1px solid #2d2d4a; border-radius: 16px; padding: 32px; }")
            .append(".header { text-align: center; border-bottom: 2px dashed #2d2d4a; padding-bottom: 20px; margin-bottom: 20px; }")
            .append(".header h1 { color: #c084fc; font-size: 22px; margin-bottom: 4px; }")
            .append(".header p { color: #7c6a9a; font-size: 12px; }")
            .append(".info { display: flex; justify-content: space-between; font-size: 13px; color: #b8a8d4; margin-bottom: 16px; }")
            .append(".info span { color: #7c6a9a; }")
            .append(".productos { border-top: 1px solid #2d2d4a; border-bottom: 1px solid #2d2d4a; padding: 16px 0; margin-bottom: 16px; }")
            .append(".producto { display: flex; justify-content: space-between; font-size: 13px; padding: 6px 0; }")
            .append(".producto .nombre { color: #e4d8f5; }")
            .append(".producto .precio { color: #f472b6; font-weight: bold; }")
            .append(".total { text-align: right; font-size: 18px; font-weight: bold; color: #c084fc; margin-bottom: 20px; }")
            .append(".estado { text-align: center; padding: 8px 16px; border-radius: 8px; font-size: 12px; font-weight: bold; display: inline-block; width: 100%; }")
            .append(".estado.PAGADO { background: rgba(52, 211, 153, 0.15); color: #34d399; }")
            .append(".estado.PENDIENTE { background: rgba(251, 191, 36, 0.15); color: #fbbf24; }")
            .append(".estado.CANCELADO { background: rgba(251, 113, 133, 0.15); color: #fb7185; }")
            .append(".estado.REEMBOLSADO { background: rgba(192, 132, 252, 0.15); color: #c084fc; }")
            .append(".footer { text-align: center; margin-top: 20px; font-size: 11px; color: #7c6a9a; }")
            .append("@media print { body { padding: 0; } .ticket { border: none; border-radius: 0; } }")
            .append("</style></head><body>")
            .append("<div class='ticket'>")
            .append("<div class='header'><h1>🧾 MERCADITO LIBRE</h1><p>Ticket de Compra</p></div>")
            .append("<div class='info'><div>Orden #").append(venta.getId()).append("</div><div><span>Fecha:</span> ").append(venta.getFecha() != null ? venta.getFecha() : "N/A").append("</div></div>")
            .append("<div class='info'><div><span>Cliente:</span> ").append(venta.getCliente() != null ? venta.getCliente().getNombre() : "N/A").append("</div></div>");

        html.append("<div class='productos'>");
        if (venta.getDetalleVentas() != null) {
            for (var det : venta.getDetalleVentas()) {
                html.append("<div class='producto'>")
                    .append("<span class='nombre'>").append(det.getProducto() != null ? det.getProducto().getNombre() : "Producto").append(" x").append(det.getCantidad()).append("</span>")
                    .append("<span class='precio'>$").append(String.format("%.2f", det.getSubtotal() != null ? det.getSubtotal() : 0.0)).append("</span>")
                    .append("</div>");
            }
        }
        html.append("</div>");

        html.append("<div class='total'>Total: $").append(String.format("%.2f", venta.getTotal() != null ? venta.getTotal() : 0.0)).append(" MXN</div>")
            .append("<div class='estado ").append(venta.getEstadoPago()).append("'>").append(venta.getEstadoPago()).append("</div>")
            .append("<div class='footer'><p>Gracias por tu compra</p><p>Mercadito Libre © 2026</p></div>")
            .append("</div></body></html>");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_HTML);
        return ResponseEntity.ok().headers(headers).body(html.toString());
    }

    // Cancelar venta (solo si está PENDIENTE)
    @PostMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelar(@PathVariable Long id) {
        try {
            VentasEntity ventaCancelada = servicio.cancelarVenta(id);
            return ResponseEntity.ok(ventaCancelada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Reembolsar venta (solo si está PAGADO)
    @PostMapping("/{id}/reembolsar")
    public ResponseEntity<?> reembolsar(@PathVariable Long id) {
        try {
            VentasEntity ventaReembolsada = servicio.reembolsarVenta(id);
            return ResponseEntity.ok(ventaReembolsada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}