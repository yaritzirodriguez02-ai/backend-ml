package com.yrs.mercaditolibre.modelo;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "ventas")
@Data
public class VentasEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private java.time.LocalDate fecha;
    private Double total;
     private String estadoPago;
  
   
//------ Relaciones de llaves FK------
    @ManyToOne
    @JoinColumn(name = "cliente_id") //llave foranea de cliente
    @JsonIgnore
    private ClienteEntity cliente;
    
    //aqui estamos mapeando ventas con detalleVenta, es decir, una venta puede tener muchos detalles de venta
    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleVentaEntity> detalleVentas = new ArrayList<>();
}