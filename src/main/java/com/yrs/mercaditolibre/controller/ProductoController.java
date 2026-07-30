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

import com.yrs.mercaditolibre.modelo.ProductoEntity;
import com.yrs.mercaditolibre.services.ProductoService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/v1/productos")//mapeo genearl de productos
//@CrossOrigin(origins = "http://localhost:5173")//permiso a react
@RequiredArgsConstructor
public class ProductoController {
private final ProductoService servicio;

//endpoint para ver todos los productos
@GetMapping("/")
public ResponseEntity<List<ProductoEntity>> Listar() {
    return ResponseEntity.ok(servicio.obtenerTodos());
}
@GetMapping("/{id}")
public ResponseEntity<ProductoEntity> ObtenerDetalles(@PathVariable Long id) {
    return ResponseEntity.ok(servicio.obtenerPorId(id));

}
//eliminar por id
@DeleteMapping("/{id}")
public ResponseEntity<ProductoEntity> eliminar(@PathVariable Long id) {
    servicio.eliminarProducto(id);
    return ResponseEntity.noContent().build();//esto retornara un mensaje 204 no content
}
//agregar
@PostMapping
public ResponseEntity<ProductoEntity> crearProducto(@RequestBody ProductoEntity producto) {
    ProductoEntity nuevo=servicio.guardarProducto(producto);
    return new ResponseEntity<>(nuevo, HttpStatus.CREATED); //error 201 created
}
//actualizar 
@PutMapping("/{id}")
public ResponseEntity<ProductoEntity> actualizarProducto(@PathVariable Long id, @RequestBody ProductoEntity productoDetalles) {
    ProductoEntity actualizado = servicio.actualizarProducto(id, productoDetalles);
    return ResponseEntity.ok(actualizado);
}

}