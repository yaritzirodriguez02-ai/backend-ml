package com.yrs.mercaditolibre.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yrs.mercaditolibre.dto.RegistroRequest;
import com.yrs.mercaditolibre.dto.UsuarioResponse;
import com.yrs.mercaditolibre.modelo.UsuarioEntity;
import com.yrs.mercaditolibre.repository.UsuarioRepository;
import com.yrs.mercaditolibre.services.UsuarioService;

import lombok.RequiredArgsConstructor;

/**
 * Endpoints EXCLUSIVOS para el panel de Administración.
 * Toda esta ruta está protegida con hasAuthority("ROLE_ADMIN") en SecurityConfig,
 * por lo que solo un administrador autenticado puede crear o listar usuarios
 * (incluyendo la creación de OTROS administradores).
 */
@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;

    // Crear un usuario (Administrador o Cliente) desde el panel de Admin.
    // Aquí SÍ se respeta el rol enviado en el request.
    @PostMapping
    public ResponseEntity<?> crearUsuario(@RequestBody RegistroRequest request) {
        try {
            UsuarioEntity nuevo = usuarioService.saveUsuarioComoAdmin(request);
            return new ResponseEntity<>(UsuarioResponse.fromEntity(nuevo), HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Listar todos los usuarios (para ver qué administradores/clientes existen).
    // No se expone el password en ningún caso.
    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listar() {
        List<UsuarioResponse> usuarios = usuarioRepository.findAll().stream()
                .map(UsuarioResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(usuarios);
    }
}