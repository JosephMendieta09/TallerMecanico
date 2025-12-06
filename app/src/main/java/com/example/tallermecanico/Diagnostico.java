package com.example.tallermecanico;

public class Diagnostico {
    private int idDiagnostico;
    private String problema;
    private int idVehiculo;
    private int idMecanico;
    private String fecha;
    private String resultado;
    private String observacion;
    private String estado;

    // Para mostrar en la lista
    private String placaVehiculo;
    private String nombreCliente;
    private String nombreMecanico;

    // Constructor para insertar (sin ID, resultado, observación)
    public Diagnostico(String problema, int idVehiculo, int idMecanico) {
        this.problema = problema;
        this.idVehiculo = idVehiculo;
        this.idMecanico = idMecanico;
    }

    // Constructor completo
    public Diagnostico(int idDiagnostico, String problema, int idVehiculo, int idMecanico,
                       String fecha, String resultado, String observacion, String estado) {
        this.idDiagnostico = idDiagnostico;
        this.problema = problema;
        this.idVehiculo = idVehiculo;
        this.idMecanico = idMecanico;
        this.fecha = fecha;
        this.resultado = resultado;
        this.observacion = observacion;
        this.estado = estado;
    }

    public int getIdDiagnostico() {
        return idDiagnostico;
    }

    public void setIdDiagnostico(int idDiagnostico) {
        this.idDiagnostico = idDiagnostico;
    }

    public String getProblema() {
        return problema;
    }

    public void setProblema(String problema) {
        this.problema = problema;
    }

    public int getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(int idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public int getIdMecanico() {
        return idMecanico;
    }

    public void setIdMecanico(int idMecanico) {
        this.idMecanico = idMecanico;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getPlacaVehiculo() {
        return placaVehiculo;
    }

    public void setPlacaVehiculo(String placaVehiculo) {
        this.placaVehiculo = placaVehiculo;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getNombreMecanico() {
        return nombreMecanico;
    }

    public void setNombreMecanico(String nombreMecanico) {
        this.nombreMecanico = nombreMecanico;
    }
}