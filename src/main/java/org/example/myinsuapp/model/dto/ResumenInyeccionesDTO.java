package org.example.myinsuapp.model.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class ResumenInyeccionesDTO {
    @XmlElement(name = "TotalInsulinaAdministrada")
    private double dosisTotal;
    @XmlElement (name = "PromedioInsulinaDiaria")
    private double promedioInsulinaDia;
    @XmlElement (name = "InyeccionesTotales")
    private int totalInyecciones;
    @XmlElement(name = "IncidenciasTotales")
    private int totalIncidencias;
    @XmlElement(name = "DosisMáxima")
    private double picoMaxInsulina;
    @XmlElement(name = "MediaInyeccionesDiarías")
    private double inyeccionesPorDia;

    public ResumenInyeccionesDTO(double dosisTotal, double promedioInsulinaDia, int totalInyecciones,
                                 int totalIncidencias, double picoMaxInsulina, double inyeccionesPorDia) {
        this.dosisTotal = dosisTotal;
        this.promedioInsulinaDia = promedioInsulinaDia;
        this.totalInyecciones = totalInyecciones;
        this.totalIncidencias = totalIncidencias;
        this.picoMaxInsulina = picoMaxInsulina;
        this.inyeccionesPorDia = inyeccionesPorDia;
    }

    public ResumenInyeccionesDTO(){}

    public double getDosisTotal() {
        return dosisTotal;
    }

    public void setDosisTotal(double dosisTotal) {
        this.dosisTotal = dosisTotal;
    }

    public double getPromedioInsulinaDia() {
        return promedioInsulinaDia;
    }

    public void setPromedioInsulinaDia(double promedioInsulinaDia) {
        this.promedioInsulinaDia = promedioInsulinaDia;
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

    public void setTotalIncidencias(int totalIncidencias) {
        this.totalIncidencias = totalIncidencias;
    }

    public double getPicoMaxInsulina() {
        return picoMaxInsulina;
    }

    public void setPicoMaxInsulina(double picoMaxInsulina) {
        this.picoMaxInsulina = picoMaxInsulina;
    }

    public double getInyeccionesPorDia() {
        return inyeccionesPorDia;
    }

    public void setInyeccionesPorDia(double inyeccionesPorDia) {
        this.inyeccionesPorDia = inyeccionesPorDia;
    }
}
