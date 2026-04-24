package org.example.myinsuapp.service;

import org.example.myinsuapp.dao.InyeccionDAO;
import org.example.myinsuapp.model.Inyeccion;
import org.example.myinsuapp.model.PlumaInsulina;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

public class InyeccionService {

    private InyeccionDAO inyeccionDAO;

    public InyeccionService(){
        this.inyeccionDAO = new InyeccionDAO();
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


}
