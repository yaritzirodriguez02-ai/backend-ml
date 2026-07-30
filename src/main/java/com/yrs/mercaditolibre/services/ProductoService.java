package com.yrs.mercaditolibre.services;

import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.yrs.mercaditolibre.modelo.ProductoEntity;
import com.yrs.mercaditolibre.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;

@Service 
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    // leer todos los registros
    @Transactional(readOnly = true)
    public List<ProductoEntity> obtenerTodos() {
        return productoRepository.findAll();
    }

    // obtener por id
    @Transactional(readOnly = true)
    public ProductoEntity obtenerPorId(Long id) {
        return productoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado " + id));
    }

    // guardar un registro
    @Transactional
    public ProductoEntity guardarProducto(ProductoEntity producto) {
        return productoRepository.save(producto);
        // aquí pueden ir todas las validaciones que se requieran para guardar un producto
    }

    // eliminar un producto
    @Transactional
    public void eliminarProducto(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar el producto");
        }
        productoRepository.deleteById(id);
    }

    // actualizar un producto
    @Transactional
    public ProductoEntity actualizarProducto(Long id, ProductoEntity productoDetalles) {
        // 1. Buscamos el producto existente en la base de datos
        ProductoEntity productoExistente = productoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));

        // 2. Actualizamos los campos básicos
        productoExistente.setNombre(productoDetalles.getNombre());
        productoExistente.setPrecio(productoDetalles.getPrecio());
        productoExistente.setStock(productoDetalles.getStock());
        productoExistente.setDescripcion(productoDetalles.getDescripcion());

        // 3. Actualizamos la URL de la imagen
        productoExistente.setImagenUrl(productoDetalles.getImagenUrl());

        // 4. Actualizamos relaciones si vienen en la petición
        if (productoDetalles.getCategoria() != null) {
            productoExistente.setCategoria(productoDetalles.getCategoria());
        }
        if (productoDetalles.getProveedor() != null) {
            productoExistente.setProveedor(productoDetalles.getProveedor());
        }

        // 5. Guardamos los cambios
        return productoRepository.save(productoExistente);
    }
}