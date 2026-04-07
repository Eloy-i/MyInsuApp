package org.example.myinsuapp.model;

import java.time.LocalDate;

public class PlumaInsulina {
    private int idPluma;
    private double depositoInicial;
    private Usuario usuario;
    private boolean activo;
    private LocalDate fechaApertura;

    public PlumaInsulina(){}

    public PlumaInsulina(int idPluma, double depositoInicial, Usuario usuario, boolean activo, LocalDate fechaApertura){
        this.idPluma = idPluma;
        this.depositoInicial = depositoInicial;
        this.usuario = usuario;
        this.activo = activo; //hacerlo true por defecto o es innecesario este dato en java
        this.fechaApertura = fechaApertura;
    }


    public int getIdPluma() {
        return idPluma;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public double getDepositoInicial() {
        return depositoInicial;
    }

    public boolean isActivo() {
        return activo;
    }

    public LocalDate getFechaApertura() {
        return fechaApertura;
    }

    public void setIdPluma(int idPluma) {
        this.idPluma = idPluma;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setDepositoInicial(int depositoInicial) {
        this.depositoInicial = depositoInicial;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public void setFechaApertura(LocalDate fechaApertura) {
        this.fechaApertura = fechaApertura;
    }
}
