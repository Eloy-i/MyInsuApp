package org.example.myinsuapp.model.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class UsuarioDTO {
    @XmlElement(name = "Nombre")
    private String nombreCompleto;
    @XmlElement(name = "Edad")
    private int edad;
    @XmlElement(name = "TipoDiabetes")
    private String TipoDiabetes;

    public UsuarioDTO(String nombreCompleto, int edad, String tipoDiabetes) {
        this.nombreCompleto = nombreCompleto;
        this.edad = edad;
        TipoDiabetes = tipoDiabetes;
    }

    public UsuarioDTO(){}

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public int getEdad() {
        return edad;
    }

    public String getTipoDiabetes() {
        return TipoDiabetes;
    }


    public void setEdad(int edad) {
        this.edad = edad;
    }

}
