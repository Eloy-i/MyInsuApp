package org.example.myinsuapp.model;

public class Incidencia {
    private int idIncidencia;
    private Inyeccion inyeccionIncidencia;
    private TipoIncidencia tipoIncidencia;

    public Incidencia (Inyeccion inyeccionIncidencia, TipoIncidencia tipoIncidencia){
        this.inyeccionIncidencia = inyeccionIncidencia;
        this.tipoIncidencia = tipoIncidencia;
    }
    public Incidencia(int idIncidencia, Inyeccion inyeccionIncidencia, TipoIncidencia tipoIncidencia){
        this.idIncidencia = idIncidencia;
        this.inyeccionIncidencia = inyeccionIncidencia;
        this.tipoIncidencia = tipoIncidencia;
    }

    /*
    Mientras creaba la tabla informe he necesitado pasar Incidencia como Objeto a Inyeccion porque como objeto es comporta de forma
    natural mejor así... Esto me lleva a una redundancia aquí en java al tener Inyeccion aquí dentro
    TODO probar el constructor sin Inyeccion si va bien quitar el objeto de la clase.
     */
    public Incidencia(int idIncidencia, TipoIncidencia tipo){
        this.idIncidencia = idIncidencia;
        this.tipoIncidencia = tipo;
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
