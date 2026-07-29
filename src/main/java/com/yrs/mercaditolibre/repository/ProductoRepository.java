package com.yrs.mercaditolibre.repository;

import com.yrs.mercaditolibre.modelo.ProductoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<ProductoEntity, Long> {

    // Consulta para obtener únicamente los productos comprados por un cliente
    @Query("SELECT DISTINCT dv.producto FROM DetalleVentaEntity dv WHERE dv.venta.cliente.id = :clienteId")
    List<ProductoEntity> findProductosCompradosPorCliente(@Param("clienteId") Long clienteId);
}