package org.example.myinsuapp.model.entity;

public class Incidencia {
    private int idIncidencia;
    private int idInyeccion;
    private TipoIncidencia tipoIncidencia;


    public Incidencia(TipoIncidencia tipoIncidencia){
        this.tipoIncidencia = tipoIncidencia;
    }

    public Incidencia(int idInyeccion, TipoIncidencia tipoIncidencia){
        this.idInyeccion = idInyeccion;
        this.tipoIncidencia = tipoIncidencia;
    }


    public int getIdInyeccion() {
        return idInyeccion;
    }

    public int getIdIncidencia() {
        return idIncidencia;
    }


    public TipoIncidencia getTipoIncidencia() {
        return tipoIncidencia;
    }

    public void setIdInyeccion(int idInyeccion) {
        this.idInyeccion = idInyeccion;
    }

    public void setIdIncidencia(int idIncidencia) {
        this.idIncidencia = idIncidencia;
    }

    public void setTipoIncidencia(TipoIncidencia tipoIncidencia) {
        this.tipoIncidencia = tipoIncidencia;
    }
}
