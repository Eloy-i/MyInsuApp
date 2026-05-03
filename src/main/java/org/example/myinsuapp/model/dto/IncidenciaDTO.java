package org.example.myinsuapp.model.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class IncidenciaDTO {
    @XmlAttribute(name = "tipo")
    String tipoIncidencia;
    @XmlAttribute(name = "cantidad")
    int cantidadTotal;

    public IncidenciaDTO(String tipoIncidencia, int cantidadTotal) {
        this.tipoIncidencia = tipoIncidencia;
        this.cantidadTotal = cantidadTotal;
    }

    public IncidenciaDTO() {
    }

    public String getTipoIncidencia() {
        return tipoIncidencia;
    }

    public int getCantidadTotal() {
        return cantidadTotal;
    }


}
