package org.example.myinsuapp.service;

import org.example.myinsuapp.dao.InformeDAO;
import org.example.myinsuapp.exceptions.DataBaseException;
import org.example.myinsuapp.exceptions.DatosInsuficientesException;
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
        this.xmlExportUtil = new XmlExportUtil();
    }

    public InformeMedicoDTO generarInforme(Usuario usuario, int dias){

        //Primera validación si no cumple lanza excepcion personalizada y corta
        validarRangoInforme(usuario.getIdUsuario(), dias);

        String fechaInforme = LocalDate.now().toString();
        int rangoDias = dias;

        UsuarioDTO usuarioDTO = getUsuarioDTO(usuario);

        ResumenInyeccionesDTO resumenInyeccionesDTO = getResumenInyeccionesDTO(usuario.getIdUsuario(), dias);

        List<ZonaUsoDTO> zonaUsoDTOList = listaUsoZonas(usuario.getIdUsuario(), dias);

        return new InformeMedicoDTO(fechaInforme, rangoDias,
                usuarioDTO, resumenInyeccionesDTO, zonaUsoDTOList);
    }

    //Aprovecho la validación de rango para lanzar una excepción
    private void validarRangoInforme(int idUsuario, int rangoDias){
        int diasDesdePrimerRegistro = informeDAO.diasDesdePrimerRegistro(idUsuario);
        //Salvo que me haya liado con las mates, esto es para permitir ver los registros si acaban de empezar y el rango es 1
        if ((diasDesdePrimerRegistro + 1) < rangoDias){
            throw new DatosInsuficientesException("No tienes suficientes registros para generar un informe de " + rangoDias + " días.");
        }
    }

    private UsuarioDTO getUsuarioDTO(Usuario usuario){
        String nombreCompleto = usuario.getNombre()+" "+ usuario.getApellidos();
        int edadCalculada = java.time.Period.between(usuario.getFechaNacimiento(),
                LocalDate.now()).getYears();
        String tipoDiabetes = usuario.getTipoDiabetes().toString();

        return new UsuarioDTO(nombreCompleto, edadCalculada, tipoDiabetes);
    }

    private ResumenInyeccionesDTO getResumenInyeccionesDTO(int idusuario, int dias) throws DataBaseException {
        ResumenInyeccionesDTO resumenInyeccionesDTO;

        double dosisTotal = informeDAO.dosisTotalPeriodo(idusuario, dias);
        double promedioInsulina = promedioInsulinaDiaria(dosisTotal, dias);
        double dosisMax = informeDAO.picoMaxInsulinaPeriodo(idusuario, dias);
        int totalInyecciones = informeDAO.totalInyeccionesPeriodo(idusuario, dias);
        int totalIncidencias = informeDAO.totalIncidenciasPeriodo(idusuario, dias);
        double inyeccionesPorDia = inyeccionesPorDia(totalInyecciones, dias);
        String zonaMasUsada = informeDAO.zonaMasUsadaPeriodo(idusuario, dias);
        String zonaMasIncidencias = informeDAO.zonaMasIncidencias(idusuario, dias);
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
    los he convertido en una lista que convertir en dos clases DTO.

    Nunca he estado mas orgulloso de sacar este méto-do que seguro es MUY mejorable pero me ha "emocionado" genuinamente meterme
    en un embrollo así y ser capaz de extraer las capas... Ahora es cuando descubro que con un lambda en tres líneas se hace lo mismo jeje

    Procedo a explicar el embrollo.
     */

    private List<ZonaUsoDTO> listaUsoZonas(int idUsuario, int dias) {
        //List del objeto ZonaUsoDTO que contiene como atributos cantidad, nombre y una lista con la clase IncidenciaDTO
        List<ZonaUsoDTO> listaUsoZonasDTO = new ArrayList<>();

        //Este primer mapa nos da desde la bd el nombre de las zonas -> cantidad inyecciones por zona
        Map<String, Integer> usoZonas = informeDAO.usoZonasPeriodo(idUsuario, dias);
        //Este segundo el nombre de la zona también y en un map interno el topo de incidencia y la cantidad de cada incidencia
        Map<String, Map<String, Integer>> incidenciasPorZona = informeDAO.incidenciasPorZona(idUsuario, dias);

        //Recorro el prime mapa para construir cada objeto zona uno a uno
        for (Map.Entry<String, Integer> stringIntegerEntry : usoZonas.entrySet()) {
            //Esto es poca cosa, sacar el nombre y el uso total. Las variables primitivas.
            String zona = stringIntegerEntry.getKey();
            int usoTotal = stringIntegerEntry.getValue();

            //Este mapa va a contener el mapa interno que pertenece a la zona en concreto
            Map<String, Integer> mapaInternoIncidencias = incidenciasPorZona.get(zona);
            //ZonaDTO contiene una lista de IncidenciasDTO, preparo la lista aquí.
            List<IncidenciaDTO> listaIncidenciaDTO = new ArrayList<>();
            //Pequeño filtro de seguridad, recorro el mapa y con cada par de key y value creo un objeto Incidencia para finalmente meterlo en su ZonaDTO
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

    //Llamada al XMLExporter para llevar el DTO a un XML
    public void exportarInforme(InformeMedicoDTO informeMedicoDTO){
        xmlExportUtil.exportarXmlInforme(informeMedicoDTO);
    }




}
