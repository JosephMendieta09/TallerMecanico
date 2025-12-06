package com.example.tallermecanico;

public class Mecanico {
    private int idMecanico;
    private String nombre;
    private int carnet;
    private String correo;
    private String telefono;
    private String estado;

    // Constructor para insertar (sin ID)
    public Mecanico(String nombre, int carnet, String correo, String telefono) {
        this.nombre = nombre;
        this.carnet = carnet;
        this.correo = correo;
        this.telefono = telefono;
        this.estado = "Disponible";
    }

    // Constructor completo (con ID)
    public Mecanico(int idMecanico, String nombre, int carnet, String correo, String telefono, String estado) {
        this.idMecanico = idMecanico;
        this.nombre = nombre;
        this.carnet = carnet;
        this.correo = correo;
        this.telefono = telefono;
        this.estado = estado;
    }

    public int getIdMecanico() {
        return idMecanico;
    }

    public void setIdMecanico(int idMecanico) {
        this.idMecanico = idMecanico;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCarnet() {
        return carnet;
    }

    public void setCarnet(int carnet) {
        this.carnet = carnet;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
