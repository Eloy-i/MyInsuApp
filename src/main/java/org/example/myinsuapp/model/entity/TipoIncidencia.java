package org.example.myinsuapp.model.entity;

public enum TipoIncidencia {
    SANGRADO("sangrado"),
    BOLA_INSULINA("bola insulina"),
    DOLOR_AGUDO("dolor agudo");

    private final String nombreVisual;

    TipoIncidencia(String nombreVisual){
        this.nombreVisual = nombreVisual;
    }

    @Override
    public String toString() { return nombreVisual; }

    public static TipoIncidencia desdeBD(String texto) {
        for (TipoIncidencia tipo : TipoIncidencia.values()) {
            if (tipo.nombreVisual.equalsIgnoreCase(texto)) {
                return tipo;
            }
        }
        return null;
    }
}
