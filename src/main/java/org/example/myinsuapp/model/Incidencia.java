package org.example.myinsuapp.model;

public class Incidencia {
    private int idIncidencia;
    private int idInyeccion;
    private Inyeccion inyeccionIncidencia;
    private TipoIncidencia tipoIncidencia;

    public Incidencia (Inyeccion inyeccionIncidencia, TipoIncidencia tipoIncidencia){
        this.inyeccionIncidencia = inyeccionIncidencia;
        this.tipoIncidencia = tipoIncidencia;
    }

    public Incidencia(int idInyeccion, TipoIncidencia tipoIncidencia){
        this.idInyeccion = idInyeccion;
        this.tipoIncidencia = tipoIncidencia;
    }
    public Incidencia(int idIncidencia, Inyeccion inyeccionIncidencia, TipoIncidencia tipoIncidencia){
        this.idIncidencia = idIncidencia;
        this.inyeccionIncidencia = inyeccionIncidencia;
        this.tipoIncidencia = tipoIncidencia;
    }

    public int getIdInyeccion() {
        return idInyeccion;
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

    public void setIdInyeccion(int idInyeccion) {
        this.idInyeccion = idInyeccion;
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
