package com.yrs.mercaditolibre.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yrs.mercaditolibre.modelo.ProveedorEntity;

@Repository
public interface ProveedorRepository extends JpaRepository<ProveedorEntity, Long> {

}