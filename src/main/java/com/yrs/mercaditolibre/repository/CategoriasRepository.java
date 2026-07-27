package com.yrs.mercaditolibre.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yrs.mercaditolibre.modelo.CategoriasEntity;

@Repository
public interface CategoriasRepository extends JpaRepository<CategoriasEntity, Long> {
    

}