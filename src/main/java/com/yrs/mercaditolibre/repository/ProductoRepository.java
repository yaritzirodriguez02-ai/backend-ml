package com.yrs.mercaditolibre.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yrs.mercaditolibre.modelo.ProductoEntity;

@Repository
public interface ProductoRepository extends JpaRepository<ProductoEntity, Long> {
    

}
