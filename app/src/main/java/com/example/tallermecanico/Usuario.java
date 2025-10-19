package com.example.tallermecanico;

import java.util.Date;

public class Usuario {
    private String nombre;
    private String usuario;
    private String rol;
    private String email;
    private String password;

    public Usuario(String nombre, String usuario, String email, String password) {
        this.nombre = nombre;
        this.usuario = usuario;
        this.email = email;
        this.password = password;
    }

    public Usuario() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
