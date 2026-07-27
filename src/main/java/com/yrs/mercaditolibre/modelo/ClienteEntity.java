package com.yrs.mercaditolibre.modelo;



import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "clientes")
@Data
public class ClienteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String email;
     private String direccion;
    private String telefono;
   

    
}