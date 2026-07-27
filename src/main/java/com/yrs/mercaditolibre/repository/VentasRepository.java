package com.yrs.mercaditolibre.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.yrs.mercaditolibre.modelo.VentasEntity;

public interface VentasRepository extends JpaRepository<VentasEntity, Long> {
    
     List<VentasEntity> findByClienteEmail(String email);
}