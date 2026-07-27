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
    private final ClienteRepository ClienteRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
        ClienteRepository ClienteRepository, PasswordEncoder passwordEncoder){
            this.usuarioRepository = usuarioRepository;
            this.ClienteRepository = ClienteRepository;
            this.passwordEncoder = passwordEncoder;

        }
        @Transactional
        public UsuarioEntity saveUsuario(RegistroRequest request){
            //
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
            if(request.getRol() != null && request.getRol().equalsIgnoreCase("ROLE_ADMIN")){
            role = Rol.ROLE_ADMIN;

        }
        usuario.setRol(role);
        UsuarioEntity savedUsuario = usuarioRepository.save(usuario);

        if(role == Rol.ROLE_CLIENTE){
            ClienteEntity cliente = new ClienteEntity();
            cliente.setNombre(request.getNombre());
            cliente.setEmail(request.getUsername());
            cliente.setDireccion(request.getDireccion());
            cliente.setTelefono(request.getTelefono());
            ClienteRepository.save(cliente);
                }

        return savedUsuario;

        }

}