package org.example.myinsuapp.service;

import org.example.myinsuapp.dao.InformeDAO;
import org.example.myinsuapp.model.Usuario;
import org.example.myinsuapp.model.dto.*;
import org.example.myinsuapp.util.XmlExportUtil;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InformeService {
    private InformeDAO informeDAO;

    private XmlExportUtil xmlExportUtil;
    public InformeService() {
        this.informeDAO = new InformeDAO();
        xmlExportUtil = new XmlExportUtil();
    }

    public InformeMedicoDTO generarInforme(int dias) throws SQLException{
        String fechaInforme = LocalDate.now().toString();
        int rangoDias = dias;

        UsuarioDTO usuarioDTO = getUsuarioDTO();
        ResumenInyeccionesDTO resumenInyeccionesDTO = getResumenInyeccionesDTO(dias);
        List<ZonaUsoDTO> zonaUsoDTOList = listaUsoZonas(dias);
        InformeMedicoDTO informeMedicoDTO = new InformeMedicoDTO(fechaInforme, rangoDias,
                usuarioDTO, resumenInyeccionesDTO, zonaUsoDTOList);
        xmlExportUtil.exportarXmlInforme(informeMedicoDTO);


        return informeMedicoDTO;
    }

    private UsuarioDTO getUsuarioDTO(){
        Usuario usuario = EstadoService.getInstance().getUsuario();

        String nombreCompleto = usuario.getNombre()+" "+ usuario.getApellidos();
        int edadCalculada = java.time.Period.between(usuario.getFechaNacimiento(),
                LocalDate.now()).getYears();
        String tipoDiabetes = usuario.getTipoDiabetes().toString();

        return new UsuarioDTO(nombreCompleto, edadCalculada, tipoDiabetes);
    }

    private ResumenInyeccionesDTO getResumenInyeccionesDTO(int dias) throws SQLException{
        ResumenInyeccionesDTO resumenInyeccionesDTO;

        double dosisTotal = informeDAO.dosisTotalPeriodo(dias);
        double promedioInsulina = promedioInsulinaDiaria(dosisTotal, dias);
        double dosisMax = informeDAO.picoMaxInsulinaPeriodo(dias);
        int totalInyecciones = informeDAO.totalInyeccionesPeriodo(dias);
        int totalIncidencias = informeDAO.totalIncidenciasPeriodo(dias);
        double inyeccionesPorDia = inyeccionesPorDia(totalInyecciones, dias);
        String zonaMasUsada = informeDAO.zonaMasUsadaPeriodo(dias);
        String zonaMasIncidencias = informeDAO.zonaMasIncidencias(dias);
        double porcentajeIncidencias = porcentejeIncidencias(totalIncidencias, totalInyecciones);

        resumenInyeccionesDTO = new ResumenInyeccionesDTO(dosisTotal, promedioInsulina, dosisMax, totalInyecciones,
                    totalIncidencias, inyeccionesPorDia, zonaMasUsada, zonaMasIncidencias, porcentajeIncidencias);

        return resumenInyeccionesDTO;
    }

    //Tres pequeños metodos para calculos... Se que los dos primeros son casi identicos, pero por legibilidad prefiero dejarlo así que rehusar uno,
    private double promedioInsulinaDiaria (double totalDosis, int dias) {
        if (dias <= 0){
            return 0;
        }
        double resultado = totalDosis / dias;
        return Math.round(resultado *100.0) / 100.0;
    }

    private double inyeccionesPorDia(int totalInyecciones, int dias){
        if (dias <= 0) return 0;
        double resultado = (double) totalInyecciones / dias;
        return Math.round(resultado * 100.0) / 100.0;
    }

    private double porcentejeIncidencias (int totalIncidencias, int totalInyecciones){
        if (totalInyecciones == 0){
            return 0;
        }
        double resultado = ((double) totalIncidencias / totalInyecciones) * 100;
        return Math.round(resultado *100.0) / 100.0;
    }

    /*
    Los dos metodos del DAO a los que voy a llamar almacenan los datos en dos map:
        1. El primero almacena Clave: zona - valor: cantidad
        2. El segundo es anidado y almacena Clave: Zona - Valor [clave: incidencia - valor: int de incidencias

    En mi primer intento pasé estos datos en crudo al DTO y me explotaron en la cara al serializarlos con JABX. Por ello
    los he convertido en dos clases DTO y en el service proceso estos datos.

    Nunca he estado mas orgulloso de sacar este méto-do que seguro es MUY mejorable pero me ha "emocionado" genuinamente meterme
    en un embrollo así y ser capaz de extraer las capas... Ahora es cuando descubro que con un lamda en tres líneas se hace lo mismo jeje

     */

    private List<ZonaUsoDTO> listaUsoZonas(int dias) throws SQLException{
        List<ZonaUsoDTO> listaUsoZonasDTO = new ArrayList<>();

        Map<String, Integer> usoZonas = informeDAO.usoZonasPeriodo(dias);
        Map<String, Map<String, Integer>> incidenciasPorZona = informeDAO.incidenciasPorZona(dias);

        for (Map.Entry<String, Integer> stringIntegerEntry : usoZonas.entrySet()) {
            String zona = stringIntegerEntry.getKey();
            int usoTotal = stringIntegerEntry.getValue();

            Map<String, Integer> mapaInternoIncidencias = incidenciasPorZona.get(zona);
            List<IncidenciaDTO> listaIncidenciaDTO = new ArrayList<>();
            if (mapaInternoIncidencias != null){
                for (Map.Entry<String, Integer> integerEntry : mapaInternoIncidencias.entrySet()) {
                    String tipoIncidencia = integerEntry.getKey();
                    int cantidadIncidencia = integerEntry.getValue();
                    listaIncidenciaDTO.add(new IncidenciaDTO(tipoIncidencia, cantidadIncidencia));
                }
                listaUsoZonasDTO.add(new ZonaUsoDTO(zona, usoTotal, listaIncidenciaDTO));
            }
        }
        return listaUsoZonasDTO;
    }




}
