package com.yrs.mercaditolibre.services;

import org.springframework.stereotype.Service;

import com.yrs.mercaditolibre.modelo.ClienteEntity;
import com.yrs.mercaditolibre.modelo.DetalleVentaEntity;
import com.yrs.mercaditolibre.modelo.ProductoEntity;
import com.yrs.mercaditolibre.modelo.VentasEntity;
import com.yrs.mercaditolibre.repository.ClienteRepository; // <-- Usamos tu ClienteRepository
import com.yrs.mercaditolibre.repository.ProductoRepository;
import com.yrs.mercaditolibre.repository.VentasRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProcesarVentaService {
    private final VentasRepository ventarepo;
    private final ProductoRepository prodrepo;
    private final ClienteRepository clienterepo; // <-- Inyectamos ClienteRepository

    @Transactional
    public VentasEntity ProcesarVenta(VentasEntity ventaRequest, String email) {

        // Buscamos el cliente por su correo/email usando el clienteRepository
        ClienteEntity cliente = clienterepo.findByEmail(email) // O findByEmail(email) según tu repo
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con el correo: " + email));

        // Asignamos el cliente a la venta (usando el atributo de tu VentasEntity)
        ventaRequest.setCliente(cliente); 
        ventaRequest.setFecha(java.time.LocalDate.now());
        ventaRequest.setEstadoPago("PENDIENTE");

        // Calcula totales y descontar el stock
        double total = 0.0;
        for (DetalleVentaEntity detalle : ventaRequest.getDetalleVentas()) {
            ProductoEntity p = prodrepo.findById(detalle.getProducto().getId()).orElseThrow();
            p.setStock(p.getStock() - detalle.getCantidad());

            detalle.setPrecioUnitario(p.getPrecio());
            detalle.setSubtotal(p.getPrecio() * detalle.getCantidad());
            detalle.setVenta(ventaRequest);
            total += detalle.getSubtotal();
        }
        
        ventaRequest.setTotal(total);
        return ventarepo.save(ventaRequest);
    }
}