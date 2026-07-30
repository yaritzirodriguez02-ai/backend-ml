package com.yrs.mercaditolibre.dto;



public class AuthResponse {
    private String token;
    private String username;
    private String nombre;
    private String rol;
    private Long clienteId;

   public AuthResponse() {
    }

    public AuthResponse(String token, String username, String nombre, String rol) {
        this.token = token;
        this.username = username;
        this.nombre = nombre;
        this.rol = rol;
    }

    public AuthResponse(String token, String username, String nombre, String rol, Long clienteId) {
        this.token = token;
        this.username = username;
        this.nombre = nombre;
        this.rol = rol;
        this.clienteId = clienteId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

}