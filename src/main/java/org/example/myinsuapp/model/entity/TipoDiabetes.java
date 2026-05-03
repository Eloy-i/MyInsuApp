package org.example.myinsuapp.model.entity;

public enum TipoDiabetes {

    TIPO_1("Tipo 1"),
    TIPO_2("Tipo 2"),
    GESTACIONAL("Gestacional"),
    LADA("LADA");

    private final  String nombreVisual;

    TipoDiabetes(String nombreVisual){
        this.nombreVisual = nombreVisual;
    }

    @Override
    public String toString() {
        return nombreVisual;
    }

    public static TipoDiabetes desdeBD(String texto) {
        for (TipoDiabetes tipo : TipoDiabetes.values()) {
            if (tipo.nombreVisual.equalsIgnoreCase(texto)) {
                return tipo;
            }
        }
        return null;
    }
}
