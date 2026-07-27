package com.yrs.mercaditolibre.services;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yrs.mercaditolibre.modelo.ClienteEntity;
import com.yrs.mercaditolibre.modelo.DetalleVentaEntity;
import com.yrs.mercaditolibre.modelo.ProductoEntity;
import com.yrs.mercaditolibre.modelo.VentasEntity;
import com.yrs.mercaditolibre.repository.ClienteRepository;
import com.yrs.mercaditolibre.repository.ProductoRepository;
import com.yrs.mercaditolibre.repository.VentasRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor
public class VentasService {

    private final VentasRepository repository;
    private final ClienteRepository clienteRepository; // 1. Inyectamos ClienteRepository
   private final ProductoRepository productoRepository;

    // Método para procesar venta
    @Transactional
    public VentasEntity procesarVenta(VentasEntity ventaRequest, String email) {
        // Busca al cliente por email o username según tu modelo
        ClienteEntity cliente = clienteRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Cliente no encontrado con email: " + email));

        ventaRequest.setCliente(cliente);
        ventaRequest.setFecha(java.time.LocalDate.now());
        ventaRequest.setEstadoPago("PENDIENTE");
         
        double total = 0.0;
        
        // Recorremos los detalles de la venta para validar stock y calcular subtotales
        for (DetalleVentaEntity detalle : ventaRequest.getDetalleVentas()) {
            ProductoEntity producto = 
            productoRepository.findById(detalle.getProducto().getId())
                .orElseThrow(() -> new RuntimeException("Producto no existe"));

            // Validamos si hay suficiente stock
            if (producto.getStock() < detalle.getCantidad()) {
                throw new RuntimeException("Stock insuficiente del producto: " + producto.getNombre());
            }

            // Descontamos del inventario
            producto.setStock(producto.getStock() - detalle.getCantidad());
            productoRepository.save(producto);

            // Asignamos precios y vinculamos con la venta
            detalle.setPrecioUnitario(producto.getPrecio());
            detalle.setSubtotal(producto.getPrecio() * detalle.getCantidad());
            detalle.setVenta(ventaRequest);

            total += detalle.getSubtotal();
        }

        ventaRequest.setTotal(total);
        //  RETURN FINAL
        return repository.save(ventaRequest);
    }
    // Método para procesar/confirmar pago
    @Transactional
    public VentasEntity confirmarPago(Long idVenta) {
        VentasEntity venta = repository.findById(idVenta)
            .orElseThrow(() -> new RuntimeException("Venta no encontrada con ID: " + idVenta));

        venta.setEstadoPago("PAGADO");
        return repository.save(venta);
    }


    @Transactional(readOnly = true)
    public List<VentasEntity> ObtenerTodos() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public List<VentasEntity> obtenerVentasPorCliente(String email) {
        return repository.findByClienteEmail(email); 
        
    }

    @Transactional(readOnly = true)
    public VentasEntity ObtenerPorId(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Venta no encontrada " + id));
    }

    @Transactional
    public VentasEntity guardarVenta(VentasEntity venta) {
        return repository.save(venta);
    }

    @Transactional
    public void eliminarVenta(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar la venta");
        }
        repository.deleteById(id);
    }

    @Transactional
    public VentasEntity actualizarVenta(Long id, VentasEntity detalleVentaEntity) {
        VentasEntity ventaExistente = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Venta no existente !"));

        BeanUtils.copyProperties(detalleVentaEntity, ventaExistente, "id");
        return repository.save(ventaExistente);
    }
}