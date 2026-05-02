package org.example.myinsuapp.model.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class ResumenInyeccionesDTO {
    @XmlElement(name = "TotalInsulinaUsada")
    private double dosisTotal;
    @XmlElement (name = "PromedioDiario")
    private double promedioInsulinaDia;
    @XmlElement(name = "DosisMaxima")
    private double dosisMaxInsulina;
    @XmlElement (name = "TotalInyecciones")
    private int totalInyecciones;
    @XmlElement(name = "TotalIncidencias")
    private int totalIncidencias;
    @XmlElement(name = "MediaInyeccionesDiarias")
    private double inyeccionesPorDia;
    @XmlElement(name = "ZonaMasUsada")
    private String zonaMasUsada;
    @XmlElement(name = "ZonaMasIncidencias")
    private String zonaMasProblematica;
    @XmlElement(name = "PorcentajeIncidencias")
    private double porcentajeIncidencia;

    public ResumenInyeccionesDTO(double dosisTotal, double promedioInsulinaDia, double dosisMaxInsulina,
                                 int totalInyecciones, int totalIncidencias, double inyeccionesPorDia, String zonaMasUsada,
                                 String zonaMasProblematica, double porcentajeIncidencia) {
        this.dosisTotal = dosisTotal;
        this.promedioInsulinaDia = promedioInsulinaDia;
        this.dosisMaxInsulina = dosisMaxInsulina;
        this.totalInyecciones = totalInyecciones;
        this.totalIncidencias = totalIncidencias;
        this.inyeccionesPorDia = inyeccionesPorDia;
        this.zonaMasUsada = zonaMasUsada;
        this.zonaMasProblematica = zonaMasProblematica;
        this.porcentajeIncidencia = porcentajeIncidencia;
    }

    public ResumenInyeccionesDTO(){}

    public double getDosisTotal() {
        return dosisTotal;
    }

    public double getPromedioInsulinaDia() {
        return promedioInsulinaDia;
    }

    public int getTotalInyecciones() {
        return totalInyecciones;
    }

    public void setTotalInyecciones(int totalInyecciones) {
        this.totalInyecciones = totalInyecciones;
    }

    public int getTotalIncidencias() {
        return totalIncidencias;
    }

    public double getDosisMaxInsulina() {
        return dosisMaxInsulina;
    }

    public double getInyeccionesPorDia() {
        return inyeccionesPorDia;
    }

    public String getZonaMasUsada() {
        return zonaMasUsada;
    }

    public String getZonaMasProblematica() {
        return zonaMasProblematica;
    }

    public double getPorcentajeIncidencia() {
        return porcentajeIncidencia;
    }
}
