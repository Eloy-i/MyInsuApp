package org.example.myinsuapp.model.entity;

import java.time.LocalDateTime;

public class Inyeccion {
    private int idInyeccion;
    private PlumaInsulina plumaInsulina;
    private Zona zona;
    private double dosis;
    private LocalDateTime horaInyeccion;
    private Incidencia incidencia;

    // Dos constructores por sobrecarga para los Insert... Uno se activará cuando haya incidencia.
    public Inyeccion(PlumaInsulina plumaInsulina, Zona zona, double dosis, LocalDateTime horaInyeccion) {
        this.plumaInsulina = plumaInsulina;
        this.zona = zona;
        this.dosis = dosis;
        this.horaInyeccion = horaInyeccion;
    }

    public Inyeccion(PlumaInsulina plumaInsulina, Zona zona, double dosis, LocalDateTime horaInyeccion, Incidencia incidencia) {
        this.plumaInsulina = plumaInsulina;
        this.zona = zona;
        this.dosis = dosis;
        this.horaInyeccion = horaInyeccion;
        this.incidencia = incidencia;
    }

    //Constructor para select
    public Inyeccion(int idInyeccion, PlumaInsulina plumaInsulina, Zona zona, double dosis, LocalDateTime horaInyeccion, Incidencia incidencia) {
        this.idInyeccion = idInyeccion;
        this.plumaInsulina = plumaInsulina;
        this.zona = zona;
        this.dosis = dosis;
        this.horaInyeccion = horaInyeccion;
        this.incidencia = incidencia;
    }

    // Este constructor no incluye la pluma... Se usa pata el select del tableview que permite además aplicar la operación DELETE
    public Inyeccion(int idInyeccion, double dosis, LocalDateTime horaInyeccion, Zona zona, Incidencia incidencia) {
        this.idInyeccion = idInyeccion;
        this.dosis = dosis;
        this.horaInyeccion = horaInyeccion;
        this.zona = zona;
        this.incidencia = incidencia;
        this.plumaInsulina = null;
    }
    //Constructor simple para muestra de datos en la vista principal
    public Inyeccion (double dosis, LocalDateTime horaInyeccion, Zona zona){
        this.dosis = dosis;
        this.horaInyeccion = horaInyeccion;
        this.zona = zona;
    }



    public int getIdInyeccion() {
        return idInyeccion;
    }

    public PlumaInsulina getPlumaInsulina() {
        return plumaInsulina;
    }

    public Zona getZona() {
        return zona;
    }

    public double getDosis() {
        return dosis;
    }

    public LocalDateTime getHoraInyeccion() {
        return horaInyeccion;
    }

    public Incidencia getIncidencia() { return incidencia; }

    public void setIdInyeccion(int idInyeccion) {
        this.idInyeccion = idInyeccion;
    }

    public void setPlumaInsulina(PlumaInsulina plumaInsulina) {
        this.plumaInsulina = plumaInsulina;
    }

    public void setZona(Zona zona) {
        this.zona = zona;
    }

    public void setDosis(double dosis) {
        this.dosis = dosis;
    }

    public void setHoraInyeccion(LocalDateTime horaInyeccion) {
        this.horaInyeccion = horaInyeccion;
    }

    public void setIncidencia(Incidencia incidencia) {
        this.incidencia = incidencia;
    }
}
