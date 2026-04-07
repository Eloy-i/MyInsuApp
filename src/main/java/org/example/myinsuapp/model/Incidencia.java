package org.example.myinsuapp.model;

public class Incidencia {
    private int idIncidencia;
    private Inyeccion inyeccionIncidencia;
    private TipoIncidencia tipoIncidencia;

    public Incidencia(int idIncidencia, Inyeccion inyeccionIncidencia, TipoIncidencia tIncidencia){
        this.idIncidencia = idIncidencia;
        this.inyeccionIncidencia = inyeccionIncidencia;
        this.tipoIncidencia = tIncidencia;
    }

    public int getIdIncidencia() {
        return idIncidencia;
    }

    public Inyeccion getInyeccionIncidencia() {
        return inyeccionIncidencia;
    }

    public TipoIncidencia getTipoIncidencia() {
        return tipoIncidencia;
    }

    public void setIdIncidencia(int idIncidencia) {
        this.idIncidencia = idIncidencia;
    }

    public void setInyeccionIncidencia(Inyeccion inyeccionIncidencia) {
        this.inyeccionIncidencia = inyeccionIncidencia;
    }

    public void setTipoIncidencia(TipoIncidencia tipoIncidencia) {
        this.tipoIncidencia = tipoIncidencia;
    }
}
