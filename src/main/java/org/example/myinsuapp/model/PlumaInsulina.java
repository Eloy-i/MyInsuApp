package org.example.myinsuapp.model;

import java.time.LocalDate;

public class PlumaInsulina {
    private int idPluma, depositoInicial;
   //TODO pensar si añadir Usuario usuario (lo normal hasta ahora) o int idUsuario;
    private boolean activo;
    LocalDate fechaApertura;

    //TODO montar constructor mientras pienso como meter al usuario


    public int getIdPluma() {
        return idPluma;
    }

    public int getDepositoInicial() {
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
