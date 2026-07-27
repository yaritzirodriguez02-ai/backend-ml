package com.yrs.mercaditolibre.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yrs.mercaditolibre.modelo.UsuarioEntity;



@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

    Optional<UsuarioEntity> findByUsername(String username);
    boolean existsByUsername(String username);

}