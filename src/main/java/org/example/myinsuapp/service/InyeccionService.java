package org.example.myinsuapp.service;

import org.example.myinsuapp.dao.IncidenciaDAO;
import org.example.myinsuapp.dao.InyeccionDAO;
import org.example.myinsuapp.model.*;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class InyeccionService {

    private InyeccionDAO inyeccionDAO;
    private IncidenciaDAO incidenciaDAO;

    public InyeccionService(){
        this.inyeccionDAO = new InyeccionDAO();
        this.incidenciaDAO = new IncidenciaDAO();
    }

    public String ultimaInyeccionTexto(PlumaInsulina pluma) {
        try {
            if (pluma == null) {
                throw new RuntimeException("No hay pluma activa");
            }

            Inyeccion inyeccion = inyeccionDAO.getUltimaInyeccion(pluma.getIdPluma());

            if (inyeccion == null) {
                throw new RuntimeException("Aún no hay dosis registradas");
            }

            DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("dd MMM 'a las' HH:mm");
            return String.format("%.1f unidades en %s el %s",
                    inyeccion.getDosis(),
                    inyeccion.getZona().getZonaCuerpo(),
                    inyeccion.getHoraInyeccion().format(formatoHora));

        } catch (SQLException e) {
            System.err.println("Error en BD: " + e.getMessage());
            throw new RuntimeException("Error al conectar con la base de datos");
        }
    }

    public double getUnidadesUsadasPluma(PlumaInsulina pluma) {
        try {
            return inyeccionDAO.getTotalDosisPorPluma(pluma.getIdPluma());
        } catch (SQLException e) {
            System.err.println("Error al obtener dosis usadas: " + e.getMessage());
            return 0.0; // Si hay fallo, asumimos que no se ha gastado nada
        }
    }

    public double getUnidadesRestantesPluma(PlumaInsulina pluma) {
        double usadas = getUnidadesUsadasPluma(pluma);
        return pluma.getDepositoInicial() - usadas;
    }

    public double getPorcentajeRestantePluma(PlumaInsulina pluma) {
        double restantes = getUnidadesRestantesPluma(pluma);
        return restantes / pluma.getDepositoInicial();
    }

    public void registrarInyeccion (PlumaInsulina pluma, Zona zona, double dosis,
                                         TipoIncidencia enumIncidencia) throws SQLException {
        validarInyeccion(pluma, dosis);

        Inyeccion inyeccion = new Inyeccion(pluma, zona, dosis,
                LocalDateTime.now());
        inyeccionDAO.insertInyeccion(inyeccion);

        if (enumIncidencia != null){
            Incidencia incidencia = new Incidencia(inyeccion.getIdInyeccion(), enumIncidencia);
            incidenciaDAO.insertIncidencia(incidencia);

        }
    }


    private void validarInyeccion(PlumaInsulina pluma, double dosis){
        if (dosis <= 0){
            throw new RuntimeException("Debes registrar un número de unidades mayor a 0");
        }
        if (dosis > 30) {
            throw new RuntimeException("Debes registrar un número de unidades inferior a 30");
        }
        if (dosis % 0.5 != 0){
            throw new RuntimeException("La dosis debe registrarse en intervalos de 0.5 unidades");
        }
        //Todo... por coherencia debería validar aquí que la zona no sea null o en FX... Pensar en ello

        double restante = getUnidadesRestantesPluma(pluma) - dosis;
        if (restante < 0){
            throw new RuntimeException("Los datos indican no queda suficiente insulina, inicia una nueva pluma.");
        }

    }

    public List<Inyeccion> listaInyecciones(int dias) {
        try {
            return inyeccionDAO.getInyeccionesRango(dias);
        } catch (SQLException e) {
            throw new RuntimeException(e); //capturar o lanzar personalizada?
        }
    }

    public List<Inyeccion> listaInyeccionesConIncidencia(int dias) {
        try {
            return inyeccionDAO.getInyeccionesIncidenciaRango(dias);
        } catch (SQLException e) {
            throw new RuntimeException(e); //capturar o lanzar personalizada?
        }
    }


}
