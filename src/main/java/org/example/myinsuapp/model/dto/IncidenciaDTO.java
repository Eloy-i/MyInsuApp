package org.example.myinsuapp.model.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class IncidenciaDTO {
    @XmlElement(name = "TipoIncidencia")
    String tipoIncidencia;
    @XmlElement(name = "CantidadRegistrada")
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

    public void setTipoIncidencia(String tipoIncidencia) {
        this.tipoIncidencia = tipoIncidencia;
    }

    public int getCantidadTotal() {
        return cantidadTotal;
    }

    public void setCantidadTotal(int cantidadTotal) {
        this.cantidadTotal = cantidadTotal;
    }
}
