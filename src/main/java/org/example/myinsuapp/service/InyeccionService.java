package org.example.myinsuapp.service;

import org.example.myinsuapp.dao.IncidenciaDAO;
import org.example.myinsuapp.dao.InyeccionDAO;
import org.example.myinsuapp.exceptions.DataBaseException;
import org.example.myinsuapp.exceptions.ReglaInyeccionException;
import org.example.myinsuapp.model.*;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class InyeccionService {

    private InyeccionDAO inyeccionDAO;
    private IncidenciaDAO incidenciaDAO;

    public InyeccionService(){
        this.inyeccionDAO = new InyeccionDAO();
        this.incidenciaDAO = new IncidenciaDAO();
    }

    public Inyeccion ultimaInyeccion(int idUser) {
        Inyeccion inyeccion = inyeccionDAO.getUltimaInyeccion(idUser);
        return inyeccion;
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

    /*
    El siguiente bloque de tres métod.os relacionados con las inyecciones están relacionados con el controlador de
    vista HistorialController.
    Las dos primeras se encargan de recoger una lista de inyecciones. La primera sin discriminar
    salvo por rango y usuario y la segunda unicamente aquellas con incidencia.

    El tercer metodo permite aplicar el DELETE y he aprovechado para añadir una capa de control que no permita un borrado
    si la inyección tiene más de 2 horas, para no perder datos historicos pero si permitir un borrado inmediato.
     */

    public List<Inyeccion> listaInyecciones(int dias, int idUsuario) throws DataBaseException{

        return inyeccionDAO.getInyeccionesRango(dias, idUsuario);

    }

    public List<Inyeccion> listaInyeccionesConIncidencia(int dias, int idUsuario) throws DataBaseException {

        return inyeccionDAO.getInyeccionesIncidenciaRango(dias, idUsuario);

    }

    public void eliminarInyeccion(Inyeccion inyeccion) throws DataBaseException, ReglaInyeccionException{
        long horasPasadas = ChronoUnit.HOURS.between(inyeccion.getHoraInyeccion(), LocalDateTime.now());

        if (horasPasadas > 2){
            throw new ReglaInyeccionException("No es posible borrar registros pasadas las dos horas.");
        }

        inyeccionDAO.borrarInyeccion(inyeccion.getIdInyeccion());


    }


}
