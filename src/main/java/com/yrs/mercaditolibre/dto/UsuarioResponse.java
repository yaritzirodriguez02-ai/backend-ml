package com.yrs.mercaditolibre.dto;

import com.yrs.mercaditolibre.modelo.UsuarioEntity;

/**
 * DTO de salida para usuarios. Existe para NUNCA exponer el password
 * (ni siquiera el hash) en las respuestas del panel de Admin.
 */
public class UsuarioResponse {

    private Long id;
    private String username;
    private String nombre;
    private String direccion;
    private String telefono;
    private String rol;

    public static UsuarioResponse fromEntity(UsuarioEntity usuario) {
        UsuarioResponse dto = new UsuarioResponse();
        dto.id = usuario.getId();
        dto.username = usuario.getUsername();
        dto.nombre = usuario.getNombre();
        dto.direccion = usuario.getDireccion();
        dto.telefono = usuario.getTelefono();
        dto.rol = usuario.getRol() != null ? usuario.getRol().name() : null;
        return dto;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getNombre() { return nombre; }
    public String getDireccion() { return direccion; }
    public String getTelefono() { return telefono; }
    public String getRol() { return rol; }
}