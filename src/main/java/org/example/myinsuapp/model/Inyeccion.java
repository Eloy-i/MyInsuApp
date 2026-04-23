package org.example.myinsuapp.model;

import java.time.LocalDateTime;

public class Inyeccion {
    private int idInyeccion;
    private PlumaInsulina plumaInsulina;
    private Zona zona;
    private double dosis;
    private LocalDateTime horaInyeccion;

    //Constructor Insert
    public Inyeccion(PlumaInsulina plumaInsulina, Zona zona, double dosis, LocalDateTime horaInyeccion) {
        this.plumaInsulina = plumaInsulina;
        this.zona = zona;
        this.dosis = dosis;
        this.horaInyeccion = horaInyeccion;
    }
    //Constructor para select
    public Inyeccion(int idInyeccion, PlumaInsulina plumaInsulina, Zona zona, double dosis, LocalDateTime horaInyeccion) {
        this.idInyeccion = idInyeccion;
        this.plumaInsulina = plumaInsulina;
        this.zona = zona;
        this.dosis = dosis;
        this.horaInyeccion = horaInyeccion;
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
}
