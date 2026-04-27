package org.example.myinsuapp.service;

import org.example.myinsuapp.dao.InformeDAO;
import org.example.myinsuapp.model.InformeMedico;
import org.example.myinsuapp.model.Usuario;
import org.example.myinsuapp.util.XmlExportUtil;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Map;

public class InformeService {
    private InformeDAO informeDAO;
    private XmlExportUtil xmlExportUtil;
    public InformeService() {
        this.informeDAO = new InformeDAO();
        this.xmlExportUtil = new XmlExportUtil();
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

        InformeMedico informeMedico;

        String fechaInforme = LocalDate.now().toString();
        int rangoDias = dias;
        InformeMedico.DatosUsuarioInforme datosUsuario = getDatosUsuarioInforme();
        try {
            double dosisTotal = informeDAO.dosisTotalPeriodo(dias);
            double promedioInsulina = promedioInsulinaDiaria(dosisTotal, dias);
            double dosisMax = informeDAO.picoMaxInsulinaPeriodo(dias);
            int totalInyecciones = informeDAO.totalInyeccionesPeriodo(dias);
            int totalIncidencias = informeDAO.totalIncidenciasPeriodo(dias);
            double inyeccionesPorDia = informeDAO.mediaInyeccionesDiarias(dias);
            String zonaMasUsada = informeDAO.zonaMasUsadaPeriodo(dias);
            String zonaMasIncidencias = informeDAO.zonaMasIncidencias(dias);
            double porcentajeIncidencias = porcentejeIncidencias(totalIncidencias, totalInyecciones);
            Map<String, Integer> usoZonas = informeDAO.usoZonasPeriodo(dias);
            Map<String, Map<String, Integer>> incidenciasPorZonazona = informeDAO.incidenciasPorZona(dias);

            informeMedico = new InformeMedico(fechaInforme, rangoDias, datosUsuario,
                    dosisTotal, promedioInsulina, dosisMax, totalInyecciones, totalIncidencias, inyeccionesPorDia, zonaMasUsada,
                    zonaMasIncidencias, porcentajeIncidencias, usoZonas, incidenciasPorZonazona);

            xmlExportUtil.exportarXmlInforme(informeMedico);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return informeMedico;

    }

    private double promedioInsulinaDiaria (double totalDosis, int dias) throws SQLException {
        return totalDosis / dias;
    }
    private double porcentejeIncidencias (int totalIncidencias, int totalInyecciones){
        if (totalInyecciones == 0){
            return 0;
        }
        return ((double) totalIncidencias / totalInyecciones) * 100;

    }

    private InformeMedico.DatosUsuarioInforme getDatosUsuarioInforme(){
        Usuario usuario = EstadoService.getInstance().getUsuario();

        String nombreComplero = usuario.getNombre()+" "+usuario.getApellidos();

        int edadCalculada = java.time.Period.between(usuario.getFechaNacimiento(),
                LocalDate.now()).getYears();

        String tDiabetes = usuario.getTipoDiabetes().toString();

        return new InformeMedico.DatosUsuarioInforme(nombreComplero, edadCalculada, tDiabetes);
    }

}
