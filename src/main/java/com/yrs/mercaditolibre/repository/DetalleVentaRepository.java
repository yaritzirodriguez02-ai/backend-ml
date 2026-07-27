package com.yrs.mercaditolibre.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yrs.mercaditolibre.modelo.DetalleVentaEntity;

@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVentaEntity, Long> {



}