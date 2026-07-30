package com.yrs.mercaditolibre.services;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.yrs.mercaditolibre.dto.RegistroRequest;
import com.yrs.mercaditolibre.modelo.ClienteEntity;
import com.yrs.mercaditolibre.modelo.Rol;
import com.yrs.mercaditolibre.modelo.UsuarioEntity;
import com.yrs.mercaditolibre.repository.UsuarioRepository;
import com.yrs.mercaditolibre.repository.ClienteRepository;

import jakarta.transaction.Transactional;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
        ClienteRepository clienteRepository, PasswordEncoder passwordEncoder){
            this.usuarioRepository = usuarioRepository;
            this.clienteRepository = clienteRepository;
            this.passwordEncoder = passwordEncoder;

        }

        /**
         * Registro PÚBLICO (endpoint /api/v1/auth/registro).
         * SIEMPRE crea un usuario con rol ROLE_CLIENTE, sin importar
         * qué venga en el request. Nadie puede auto-asignarse ROLE_ADMIN
         * desde este endpoint, ni siquiera manipulando el JSON directamente.
         */
        @Transactional
        public UsuarioEntity saveUsuario(RegistroRequest request){

            if(usuarioRepository.existsByUsername(request.getUsername())){
                throw new IllegalArgumentException("El nombre de usuario ya esta en uso ");
            }

            UsuarioEntity usuario = new UsuarioEntity();
            usuario.setUsername(request.getUsername());
            usuario.setPassword(passwordEncoder.encode(request.getPassword()));
            usuario.setNombre(request.getNombre());
            usuario.setDireccion(request.getDireccion());
            usuario.setTelefono(request.getTelefono());

            // Forzado: el registro público JAMÁS crea administradores.
            usuario.setRol(Rol.ROLE_CLIENTE);

            UsuarioEntity savedUsuario = usuarioRepository.save(usuario);

            ClienteEntity cliente = new ClienteEntity();
            cliente.setNombre(request.getNombre());
            cliente.setEmail(request.getUsername());
            cliente.setDireccion(request.getDireccion());
            cliente.setTelefono(request.getTelefono());
            clienteRepository.save(cliente);

            return savedUsuario;
        }

        /**
         * Registro PRIVILEGIADO (uso exclusivo del panel de Administración).
         * Aquí SÍ se respeta el rol indicado en el request, porque este método
         * solo debe ser invocado desde un endpoint protegido con
         * hasAuthority("ROLE_ADMIN") en SecurityConfig.
         */
        @Transactional
        public UsuarioEntity saveUsuarioComoAdmin(RegistroRequest request){

            if(usuarioRepository.existsByUsername(request.getUsername())){
                throw new IllegalArgumentException("El nombre de usuario ya esta en uso ");
            }

            UsuarioEntity usuario = new UsuarioEntity();
            usuario.setUsername(request.getUsername());
            usuario.setPassword(passwordEncoder.encode(request.getPassword()));
            usuario.setNombre(request.getNombre());
            usuario.setDireccion(request.getDireccion());
            usuario.setTelefono(request.getTelefono());

            Rol role = Rol.ROLE_CLIENTE;
            if (request.getRol() != null && request.getRol().equalsIgnoreCase("ROLE_ADMIN")) {
                role = Rol.ROLE_ADMIN;
            }
            usuario.setRol(role);

            UsuarioEntity savedUsuario = usuarioRepository.save(usuario);

            if (role == Rol.ROLE_CLIENTE) {
                ClienteEntity cliente = new ClienteEntity();
                cliente.setNombre(request.getNombre());
                cliente.setEmail(request.getUsername());
                cliente.setDireccion(request.getDireccion());
                cliente.setTelefono(request.getTelefono());
                clienteRepository.save(cliente);
            }

            return savedUsuario;
        }

}