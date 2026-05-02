package org.example.myinsuapp.service;

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

    public InyeccionService(){
        this.inyeccionDAO = new InyeccionDAO();
    }

    public Inyeccion ultimaInyeccion(int idUser) {
        return inyeccionDAO.getUltimaInyeccion(idUser);
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
                                         TipoIncidencia enumIncidencia) throws DataBaseException {
        //Primero valido (aunque en controller ya lo tenga delimitado).
        validarInyeccion(pluma, dosis);
        Inyeccion inyeccion = null;
        //Compruebo si es una inyección completa para activar la transacción o la inserción sin incidencia
        if (enumIncidencia != null){
            // problema mi constructor requiere el id inyeccion, cambiar constructor o mandar la Incidencia completa desde arriba?
            Incidencia incidencia = new Incidencia(enumIncidencia);
            inyeccion = new Inyeccion(pluma, zona, dosis, LocalDateTime.now(), incidencia);
            inyeccionDAO.insertInyeccionIncidencia(inyeccion);
        } else {
            //Podría controlar con un break pero aquí prefiero el else, me parece más natural
            inyeccion = new Inyeccion(pluma, zona, dosis, LocalDateTime.now());

            inyeccionDAO.insertInyeccion(inyeccion);
        }

    }


    private void validarInyeccion(PlumaInsulina pluma, double dosis) throws ReglaInyeccionException{
        if (pluma == null){
            throw new ReglaInyeccionException("Necesitas tener una pluma activa para poder registrar la inyección");
        }
        if (dosis <= 0){
            throw new ReglaInyeccionException("Debes registrar un número de unidades mayor a 0");
        }
        if (dosis > 30) {
            throw new ReglaInyeccionException("Debes registrar un número de unidades inferior a 30");
        }
        if (dosis % 0.5 != 0){
            throw new ReglaInyeccionException("La dosis debe registrarse en intervalos de 0.5 unidades");
        }

        /*
        No me gusta dejar código muerto pero aclaro esta... He decidido no controlar de forma tan estricta algo que el
        usuario conoce de sobra. La app no puede saber que insulina me queda de forma mátematica, debido a la perdida
        no contabilizada en purgas... Mi control estricto es el superior que está vinculado a la realidad física.
        Los límites de dosis, en incrementos de 0.5 y la necesidad de tener al menos una pluma registrada para poder
        realizar la insercción.

        double restante = getUnidadesRestantesPluma(pluma) - dosis;
        if (restante < 0){
            throw new ReglaInyeccionException("Los datos indican no queda suficiente insulina, inicia una nueva pluma.");
        }

         */

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
