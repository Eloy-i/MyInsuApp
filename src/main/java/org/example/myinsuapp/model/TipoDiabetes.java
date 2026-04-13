package org.example.myinsuapp.model;

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
}
