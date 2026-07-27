package com.yrs.mercaditolibre.services;



import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yrs.mercaditolibre.modelo.CategoriasEntity;
import com.yrs.mercaditolibre.repository.CategoriasRepository;

import lombok.RequiredArgsConstructor;


@Service @RequiredArgsConstructor 

public class CategoriasService {
    private final CategoriasRepository repository;
//leer todos los registros 
@Transactional(readOnly = true)
    public List<CategoriasEntity> ObtenerTodos() {
        return repository.findAll();
    }
    //obtener por id
@Transactional(readOnly = true)
    public CategoriasEntity ObtenerPorId(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Categoría no encontrada"+id));
    }
//gurdar un registro
@Transactional
    public CategoriasEntity guardarCategoria(CategoriasEntity categoria) {
        return repository.save(categoria);
        //aqui pueden ir todas las validaciones que se requieran para guardar una categoría
    }
    //eliminar  una categoría
@Transactional
public void eliminarCategoria(Long id) {
    if (!repository.existsById(id)) {
        throw new RuntimeException("No se puede eliminar la categoría");
    }
    repository.deleteById(id);
}
//actualizar una categoría
@Transactional
public CategoriasEntity actualizarCategoria (Long id, CategoriasEntity detalleCategoriaEntity){
    CategoriasEntity categoriaExistente = repository.findById(id)
    .orElseThrow(() -> new RuntimeException("Categoría no existe !!"));

    
    BeanUtils.copyProperties(detalleCategoriaEntity, categoriaExistente, "id");
    return repository.save(categoriaExistente);
}
   
}
