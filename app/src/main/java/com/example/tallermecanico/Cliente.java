package com.example.tallermecanico;

public class Cliente {
    private int idCliente;
    private String nombre;
    private int carnet;
    private String direccion;
    private String correo;
    private String telefono;

    public Cliente(String nombre, int carnet, String direccion, String correo, String telefono) {
        this.nombre = nombre;
        this.carnet = carnet;
        this.direccion = direccion;
        this.correo = correo;
        this.telefono = telefono;
    }

    public Cliente(int idCliente, String nombre, int carnet, String direccion, String correo, String telefono) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.carnet = carnet;
        this.direccion = direccion;
        this.correo = correo;
        this.telefono = telefono;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
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
}
