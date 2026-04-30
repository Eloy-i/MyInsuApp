package org.example.myinsuapp.model;
import java.time.LocalDate;

/*
 MVP Fase 1: Entidad base para la gestión de plumas de insulina.

 Escalabilidad futura, de cara a llevar este proyecto a segundo:

 Esta clase está diseñada con la previsión de evolucionar a una clase Abstracta.
 El objetivo futuro es aplicar herencia para crear subtipos específicos
 (PlumaInsulinaLenta, PlumaInsulinaRapida, PlumaInsulinaSemanal, PlumaInsulinaPediatrica..). Todos compartirán este patrón
 de datos común, pero permitirán métodos con comportamientos
 propios
  */

public class PlumaInsulina {

    public static final int DEPOSITO_ESTANDAR = 300;

    private int idPluma;
    private int depositoInicial;
    private Usuario usuario;
    private boolean activo;
    private LocalDate fechaApertura;

    public PlumaInsulina(){}

    //Constructor para Inserts
    public PlumaInsulina(int depositoInicial, Usuario usuario, boolean activo, LocalDate fechaApertura){
        this.depositoInicial = depositoInicial;
        this.usuario = usuario;
        this.activo = activo;
        this.fechaApertura = fechaApertura;
    }

    //Constructor para Select
    public PlumaInsulina(int idPluma, int depositoInicial, Usuario usuario, boolean activo, LocalDate fechaApertura){
        this.idPluma = idPluma;
        this.depositoInicial = depositoInicial;
        this.usuario = usuario;
        this.activo = activo;
        this.fechaApertura = fechaApertura;
    }


    public int getIdPluma() {
        return idPluma;
    }

    public Usuario getUsuario() {
        return usuario;
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
