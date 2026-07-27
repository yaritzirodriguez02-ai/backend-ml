package com.yrs.mercaditolibre.modelo;


import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "detalleVenta")
@Data
public class DetalleVentaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer cantidad;
    private Double precioUnitario;
     private Double subtotal;
  
     //------ Relaciones de llaves FK------
    @ManyToOne
    @JoinColumn(name = "venta_id") //llave foranea de venta
    @JsonIgnore
    private VentasEntity venta;

    @ManyToOne
    @JoinColumn(name = "producto_id") //llave foranea de producto
    private ProductoEntity producto;
    

    
}