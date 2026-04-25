package org.example.myinsuapp.model;

public enum TipoIncidencia {
    SANGRADO("sangrado"),
    BOLA_INSULINA("bola insulina"),
    DOLOR_AGUDO("dolor agudo");

    private String nombreVisual;

    TipoIncidencia(String nombreVisual){
        this.nombreVisual = nombreVisual;
    }

    @Override
    public String toString() { return nombreVisual; }
}
