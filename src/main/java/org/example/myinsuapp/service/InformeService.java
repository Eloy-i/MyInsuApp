package org.example.myinsuapp.service;

import org.example.myinsuapp.dao.InformeDAO;
import org.example.myinsuapp.model.InformeMedico;
import org.example.myinsuapp.model.Usuario;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Map;

public class InformeService {
    private InformeDAO informeDAO;
    public InformeService() {
        this.informeDAO = new InformeDAO();
    }
    /*
    public InformeMedico(LocalDate fechaInforme, String nombreUsuarioActual, int edadUsuario,
                         String tipoDiabetesUsuario, double dosisTotal, double promedioInsulinaDia, double picoMaxInsulina,
                         double inyeccionesPorDia, String zonaMasUsada, String zonaMasProblematica, double porcentajeIncidencia,
                         Map<String, Integer> usoZonas, Map<String, Map<String, Integer>> historialIncidenciasPorZona) {
        this.fechaInforme = fechaInforme;
        this.nombreUsuarioActual = nombreUsuarioActual;
        this.edadUsuario = edadUsuario;
        this.tipoDiabetesUsuario = tipoDiabetesUsuario;
        this.dosisTotal = dosisTotal;
        this.promedioInsulinaDia = promedioInsulinaDia;
        this.picoMaxInsulina = picoMaxInsulina;
        this.inyeccionesPorDia = inyeccionesPorDia;
        this.zonaMasUsada = zonaMasUsada;
        this.zonaMasProblematica = zonaMasProblematica;
        this.porcentajeIncidencia = porcentajeIncidencia;
        this.usoZonas = usoZonas;
        this.historialIncidenciasPorZona = historialIncidenciasPorZona;
    }
     */

    public InformeMedico informeCompleto (int dias){
        Usuario usuario = EstadoService.getInstance().getUsuario();
        InformeMedico informeMedico = null;
        LocalDate fechaInforme = LocalDate.now();
        String nombreUser = usuario.getNombre()+ " " + usuario.getApellidos();
        int edad = usuario.getFechaNacimiento().getYear();
        String tipoDt = usuario.getTipoDiabetes().toString();
        try {
            double dosisTotal = informeDAO.dosisTotalPeriodo(dias);
            double promedioInsulina = promedioInsulinaDiaria(dias);
            double dosisMax = informeDAO.picoMaxInsulinaPeriodo(dias);
            int totalInyecciones = informeDAO.totalInyeccionesPeriodo(dias);
            int totalIncidencias = informeDAO.totalIncidenciasPeriodo(dias);
            double inyeccionesPorDia = informeDAO.mediaInyeccionesDiarias(dias);
            String zonaMasUsada = informeDAO.zonaMasUsadaPeriodo(dias);
            String zonaMasIncidencias = informeDAO.zonaMasIncidencias(dias);
            double porcentajeIncidencias = porcentejeIncidencias(totalIncidencias, totalInyecciones);
            Map<String, Integer> usoZonas = informeDAO.usoZonasPeriodo(dias);
            Map<String, Map<String, Integer>> incidenciasPorZonazona = informeDAO.incidenciasPorZona(dias);
            informeMedico = new InformeMedico(fechaInforme, nombreUser, edad, tipoDt,
                    dosisTotal, promedioInsulina, dosisMax, totalInyecciones, totalIncidencias, inyeccionesPorDia, zonaMasUsada,
                    zonaMasIncidencias, porcentajeIncidencias, usoZonas, incidenciasPorZonazona);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return informeMedico;

    }

    private double promedioInsulinaDiaria (int dias) throws SQLException {
        return informeDAO.dosisTotalPeriodo(dias) / dias;
    }
    private double porcentejeIncidencias (int totalIncidencias, int totalInyecciones){
        if (totalInyecciones == 0){
            return 0;
        }
        return ((double) totalIncidencias / totalInyecciones) * 100;

    }

}
