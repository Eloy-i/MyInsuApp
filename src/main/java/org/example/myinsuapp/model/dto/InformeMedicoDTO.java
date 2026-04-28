package org.example.myinsuapp.model.dto;

import jakarta.xml.bind.annotation.*;

import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Informe")
public class InformeMedicoDTO {
    @XmlAttribute(name = "fecha")
    private String fechaInforme;
    @XmlAttribute
    private int rangoDias;
    @XmlElement(name = "Paciente")
    private UsuarioDTO usuarioDTO;
    @XmlElement(name = "ResumenEstadisticoInyecciones")
    private ResumenInyeccionesDTO resumenInyeccionesDTO;
    @XmlElementWrapper(name = "ResumenEstadisticoZonas")
    @XmlElement(name = "Zona")
    private List<ZonaUsoDTO> listaUsoZonas;

    public InformeMedicoDTO(String fechaInforme, int rangoDias, UsuarioDTO usuarioDTO,
                            ResumenInyeccionesDTO resumenInyeccionesDTO, List<ZonaUsoDTO> listaUsoZonas) {
        this.fechaInforme = fechaInforme;
        this.rangoDias = rangoDias;
        this.usuarioDTO = usuarioDTO;
        this.resumenInyeccionesDTO = resumenInyeccionesDTO;
        this.listaUsoZonas = listaUsoZonas;
    }

    public String getFechaInforme() {
        return fechaInforme;
    }

    public int getRangoDias() {
        return rangoDias;
    }

    public UsuarioDTO getUsuarioDTO() {
        return usuarioDTO;
    }

    public ResumenInyeccionesDTO getResumenInyeccionesDTO() {
        return resumenInyeccionesDTO;
    }

    public List<ZonaUsoDTO> getListaUsoZonas() {
        return listaUsoZonas;
    }
}
