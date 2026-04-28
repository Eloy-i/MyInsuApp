package org.example.myinsuapp.model.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;

import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class ZonaUsoDTO {
    @XmlElement(name = "Zona")
    private String nombreZona;
    @XmlElement(name = "TotalInyecciones")
    private int usoTotal;
    @XmlElement(name = "TipoIncidenciasRegistradas")
    private List<IncidenciaDTO> listaIncidenciasZona;

    public ZonaUsoDTO(String nombreZona, int usoTotal, List<IncidenciaDTO> listaIncidenciasZona) {
        this.nombreZona = nombreZona;
        this.usoTotal = usoTotal;
        this.listaIncidenciasZona = listaIncidenciasZona;
    }

    public ZonaUsoDTO() {}

    public String getNombreZona() {
        return nombreZona;
    }

    public int getUsoTotal() {
        return usoTotal;
    }

    public List<IncidenciaDTO> getListaIncidenciasZona() {
        return listaIncidenciasZona;
    }
}
