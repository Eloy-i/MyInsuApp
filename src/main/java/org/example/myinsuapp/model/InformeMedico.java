package org.example.myinsuapp.model;
import jakarta.xml.bind.annotation.*;
import java.util.Map;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Informe")
public class InformeMedico {

    @XmlAttribute(name = "fecha")
    private String fechaInforme;
    @XmlAttribute
    private int rangoDias;
    @XmlElement(name = "Paciente")
    private DatosUsuarioInforme datosUsuario;
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
    @XmlElement(name = "ZonaMasUsada")
    private String zonaMasUsada;
    @XmlElement(name = "ZonaMasIncidencias")
    private String zonaMasProblematica;
    @XmlElement(name = "FrecuenciaIncidencias")
    private double porcentajeIncidencia;

    //Todo (como coyons trato los mapas?
    @XmlTransient
    private Map<String, Integer> usoZonas;
    @XmlTransient
    private Map<String, Map<String, Integer>> historialIncidenciasPorZona;

    public InformeMedico(String fechaInforme, int rangoDias, DatosUsuarioInforme datosUsuario,
                         double dosisTotal, double promedioInsulinaDia, double picoMaxInsulina, int totalInyecciones,
                         int totalIncidencias, double inyeccionesPorDia, String zonaMasUsada, String zonaMasProblematica,
                         double porcentajeIncidencia, Map<String, Integer> usoZonas,
                         Map<String, Map<String, Integer>> historialIncidenciasPorZona) {
        this.fechaInforme = fechaInforme;
        this.rangoDias = rangoDias;
        this.datosUsuario = datosUsuario;
        this.dosisTotal = dosisTotal;
        this.totalInyecciones = totalInyecciones;
        this.totalIncidencias = totalIncidencias;
        this.promedioInsulinaDia = promedioInsulinaDia;
        this.picoMaxInsulina = picoMaxInsulina;
        this.inyeccionesPorDia = inyeccionesPorDia;
        this.zonaMasUsada = zonaMasUsada;
        this.zonaMasProblematica = zonaMasProblematica;
        this.porcentajeIncidencia = porcentajeIncidencia;
        this.usoZonas = usoZonas;
        this.historialIncidenciasPorZona = historialIncidenciasPorZona;
    }

    /*
    1 counts of IllegalAnnotationExceptions
    org.example.myinsuapp.model.InformeMedico no tiene un constructor por defecto sin argumentos.
    Ha sido ver eso y me he acordado de que hablaste de la importancia del constructor vacio cuando llegaramos a ficheros.
     */
    public InformeMedico(){}

    public String getFechaInforme() {
        return fechaInforme;
    }

    public int getRangoDias() { return rangoDias; }

    public DatosUsuarioInforme getDatosUsuario() {
        return datosUsuario;
    }

    public double getDosisTotal() {
        return dosisTotal;
    }

    public double getPromedioInsulinaDia() {
        return promedioInsulinaDia;
    }

    public double getPicoMaxInsulina() {
        return picoMaxInsulina;
    }

    public int getTotalInyecciones() {
        return totalInyecciones;
    }

    public int getTotalIncidencias() {
        return totalIncidencias;
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

    public Map<String, Integer> getUsoZonas() {
        return usoZonas;
    }

    public Map<String, Map<String, Integer>> getHistorialIncidenciasPorZona() {
        return historialIncidenciasPorZona;
    }

    public double getPorcentajeIncidencia() {
        return porcentajeIncidencia;
    }

    public void setDosisTotal(double dosisTotal) {
        this.dosisTotal = dosisTotal;
    }

    public void setPromedioInsulinaDia(double promedioInsulinaDia) {
        this.promedioInsulinaDia = promedioInsulinaDia;
    }

    public void setPicoMaxInsulina(double picoMaxInsulina) {
        this.picoMaxInsulina = picoMaxInsulina;
    }

    public void setInyeccionesPorDia(double inyeccionesPorDia) {
        this.inyeccionesPorDia = inyeccionesPorDia;
    }

    public void setZonaMasUsada(String zonaMasUsada) {
        this.zonaMasUsada = zonaMasUsada;
    }

    public void setZonaMasProblematica(String zonaMasProblematica) {
        this.zonaMasProblematica = zonaMasProblematica;
    }

    public void setPorcentajeIncidencia(double porcentajeIncidencia) {
        this.porcentajeIncidencia = porcentajeIncidencia;
    }

    public void setUsoZonas(Map<String, Integer> usoZonas) {
        this.usoZonas = usoZonas;
    }

    public void setHistorialIncidenciasPorZona(Map<String, Map<String, Integer>> historialIncidenciasPorZona) {
        this.historialIncidenciasPorZona = historialIncidenciasPorZona;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class DatosUsuarioInforme{
        @XmlElement(name = "NombreCompleto")
        private String nombreCompleto;
        @XmlElement(name = "Edad")
        private int edad;
        @XmlElement(name = "TipoDiabetes")
        private String tipoDiabetes;

        public DatosUsuarioInforme(){}

        public DatosUsuarioInforme(String nombreCompleto, int edad, String tipoDiabetes){
            this.nombreCompleto = nombreCompleto;
            this.edad = edad;
            this.tipoDiabetes = tipoDiabetes;
        }
    }
}
