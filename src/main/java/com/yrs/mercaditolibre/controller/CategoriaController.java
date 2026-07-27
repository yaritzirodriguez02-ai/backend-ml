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

import com.yrs.mercaditolibre.modelo.CategoriasEntity;
import com.yrs.mercaditolibre.services.CategoriasService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/categorias")//mapeo general de categorias
@CrossOrigin(origins = "http://localhost:5173")//permiso a react
@RequiredArgsConstructor
public class CategoriaController{
private final CategoriasService servicio;

//endpoint para ver todos los productos
@GetMapping("/")
public ResponseEntity<List<CategoriasEntity>> Listar() {
    return ResponseEntity.ok(servicio.ObtenerTodos());
}
@GetMapping("/{id}")
public ResponseEntity<CategoriasEntity> ObtenerDetalles(@PathVariable Long id) {
    return ResponseEntity.ok(servicio.ObtenerPorId(id));

}
//eliminar por id
@DeleteMapping("/{id}")
public ResponseEntity<CategoriasEntity> eliminar(@PathVariable Long id) {
    servicio.eliminarCategoria(id);
    return ResponseEntity.noContent().build();//esto retornara un mensaje 204 no content
}
//agregar
@PostMapping
public ResponseEntity<CategoriasEntity> crearCategoria(@RequestBody CategoriasEntity categoria) {
    CategoriasEntity nueva=servicio.guardarCategoria(categoria);
    return new ResponseEntity<>(nueva, HttpStatus.CREATED); //error 201 created
}
//actualizar 
@PutMapping("/{id}")
public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody CategoriasEntity categoria){
  try{
    CategoriasEntity categoriaAct = servicio.actualizarCategoria(id,categoria);
  return ResponseEntity.ok(categoriaAct);  
  }catch(RuntimeException e){
    return ResponseEntity.status(404).body(e.getMessage());
  }

}}