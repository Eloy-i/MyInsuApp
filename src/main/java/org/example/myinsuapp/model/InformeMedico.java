package org.example.myinsuapp.model;

import java.time.LocalDate;
import java.util.Map;

public class InformeMedico {
    private LocalDate fechaInforme;
    private String nombreUsuarioActual;
    private int edadUsuario;
    private String tipoDiabetesUsuario;
    private double dosisTotal;
    private double promedioInsulinaDia;
    private int totalInyecciones;
    private int totalIncidencias;
    private double picoMaxInsulina;
    private double inyeccionesPorDia;
    private String zonaMasUsada;
    private String zonaMasProblematica;
    private double porcentajeIncidencia;

    private Map<String, Integer> usoZonas;
    private Map<String, Map<String, Integer>> historialIncidenciasPorZona;

    public InformeMedico(LocalDate fechaInforme, String nombreUsuarioActual, int edadUsuario,
                         String tipoDiabetesUsuario, double dosisTotal, double promedioInsulinaDia, double picoMaxInsulina,
                         int totalInyecciones, int totalIncidencias, double inyeccionesPorDia, String zonaMasUsada, String zonaMasProblematica,
                         double porcentajeIncidencia, Map<String, Integer> usoZonas,
                         Map<String, Map<String, Integer>> historialIncidenciasPorZona) {
        this.fechaInforme = fechaInforme;
        this.nombreUsuarioActual = nombreUsuarioActual;
        this.edadUsuario = edadUsuario;
        this.tipoDiabetesUsuario = tipoDiabetesUsuario;
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

    public LocalDate getFechaInforme() {
        return fechaInforme;
    }

    public String getNombreUsuarioActual() {
        return nombreUsuarioActual;
    }

    public int getEdadUsuario() {
        return edadUsuario;
    }

    public String getTipoDiabetesUsuario() {
        return tipoDiabetesUsuario;
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

    public void setFechaInforme(LocalDate fechaInforme) {
        this.fechaInforme = fechaInforme;
    }

    public void setNombreUsuarioActual(String nombreUsuarioActual) {
        this.nombreUsuarioActual = nombreUsuarioActual;
    }

    public void setEdadUsuario(int edadUsuario) {
        this.edadUsuario = edadUsuario;
    }

    public void setTipoDiabetesUsuario(String tipoDiabetesUsuario) {
        this.tipoDiabetesUsuario = tipoDiabetesUsuario;
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
}
