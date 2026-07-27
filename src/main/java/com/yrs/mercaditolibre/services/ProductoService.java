package com.yrs.mercaditolibre.services;

import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.yrs.mercaditolibre.modelo.ProductoEntity;
import com.yrs.mercaditolibre.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;


@Service @RequiredArgsConstructor 

public class ProductoService {
    private final ProductoRepository repository;
//leer todos los registros 
@Transactional(readOnly = true)
    public List<ProductoEntity> ObtenerTodos() {
        return repository.findAll();
    }
    //obtener por id
@Transactional(readOnly = true)
    public ProductoEntity ObtenerPorId(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Producto no encontrado"+id));
    }
//gurdar un registro
@Transactional
    public ProductoEntity guardarProducto(ProductoEntity producto) {
        return repository.save(producto);
        //aqui pueden ir todas las validaciones que se requieran para guardar un producto
    }
    //eliminar  un producto
@Transactional
public void eliminarProducto(Long id) {
    if (!repository.existsById(id)) {
        throw new RuntimeException("No se puede eliminar el producto");
    }
    repository.deleteById(id);
}
//actualizar un producto
@Transactional
public ProductoEntity actualizarProducto (Long id, ProductoEntity detalleProductoEntity){
    ProductoEntity productoExistente = repository.findById(id)
    .orElseThrow(() -> new RuntimeException("Producto no existe !!"));

    
    BeanUtils.copyProperties(detalleProductoEntity, productoExistente, "id");
    return repository.save(productoExistente);
}
   
}
