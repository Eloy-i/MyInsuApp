package org.example.myinsuapp.dao;
import org.example.myinsuapp.database.DBConnection;
import org.example.myinsuapp.database.DBSchem;
import org.example.myinsuapp.model.Incidencia;
import org.example.myinsuapp.model.Inyeccion;
import org.example.myinsuapp.model.TipoIncidencia;
import org.example.myinsuapp.model.Zona;
import org.example.myinsuapp.service.EstadoService;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class InyeccionDAO {
    private Connection connection;
    private ResultSet resultSet;
    private PreparedStatement preparedStatement;

    //TODO Si la inyección tiene incidencia, necesito obtener la ID que se ha generado... Revisar como.
    public void insertInyeccion(Inyeccion inyeccion) throws SQLException {
        connection = DBConnection.getConnection();
        String query = String.format("INSERT INTO %s (%s, %s, %s, %s) VALUES (?, ?, ?, ?);",
                DBSchem.TAB_INYECCION, DBSchem.COL_INY_PLUMA, DBSchem.COL_ZONA_ID,
                DBSchem.COL_INY_DOSIS, DBSchem.COL_INY_FECHA);
        preparedStatement = connection.prepareStatement(query);

        preparedStatement.setInt(1, inyeccion.getPlumaInsulina().getIdPluma());
        preparedStatement.setInt(2, inyeccion.getZona().getIdZona());
        preparedStatement.setDouble(3, inyeccion.getDosis());
        preparedStatement.setObject(4, inyeccion.getHoraInyeccion());
        preparedStatement.executeUpdate();

    }

    public double getTotalDosisPorPluma(int idPluma) throws SQLException {
        connection = DBConnection.getConnection();
        String query = "SELECT SUM(dosis) FROM inyeccion WHERE id_plumaInsulina = ?";

        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, idPluma);
        resultSet = preparedStatement.executeQuery();
        if (resultSet.next()){
            return resultSet.getDouble(1);
        }
        return 0;
    }


    //Todo esto debe recibir de JAva 7 o 30 días... Necesito además otro método si el usuario solo quiere ver sus incidencias
    // TODO Composición coyons... necesito que Incidencias sea parte de Inyección en java para poder discriminar y usar el tableview y necesito que el query devuelva los objetos completos.

    public List<Inyeccion>getInyeccionesRango(int dias) throws SQLException {
        List<Inyeccion> inyecciones = new ArrayList<>();
        connection = DBConnection.getConnection();
        String query = String.format("""
                SELECT i.%s, i.%s, i.%s,
                z.%s, z.%s,
                inc.%s, inc.%s
                FROM %s i
                LEFT JOIN %s inc ON i.%s = inc.%s
                INNER JOIN %s z ON z.%s = i.%s
                WHERE DATE(i.%s) >= DATE_SUB(CURDATE(), INTERVAL ? DAY)
                ORDER BY i.%s DESC;
                """, DBSchem.COL_INY_ID, DBSchem.COL_INY_FECHA, DBSchem.COL_INY_DOSIS,
                DBSchem.COL_ZONA_ID, DBSchem.COL_ZONA_NOMBRE,
                DBSchem.COL_INC_ID, DBSchem.COL_INC_TIPO,
                DBSchem.TAB_INYECCION,
                DBSchem.TAB_INCIDENCIA, DBSchem.COL_INY_ID, DBSchem.COL_INC_INY,
                DBSchem.TAB_ZONA, DBSchem.COL_ZONA_ID, DBSchem.COL_INY_ZONA,
                DBSchem.COL_INY_FECHA,
                DBSchem.COL_INY_FECHA);

        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, dias);

        while (resultSet.next()){
            int idInyeccion = resultSet.getInt(DBSchem.COL_INY_ID);
            LocalDateTime fechaHora = resultSet.getObject(DBSchem.COL_INY_FECHA, LocalDateTime.class);
            double dosis = resultSet.getDouble(DBSchem.COL_INY_DOSIS);
            int idZona = resultSet.getInt(DBSchem.COL_ZONA_ID);
            int idIncidencia = resultSet.getInt(DBSchem.COL_INC_ID);
            Incidencia incidencia = null;
            if (idIncidencia > 0){
                TipoIncidencia tipoIncidencia = TipoIncidencia.valueOf(resultSet.getString(DBSchem.COL_INC_TIPO));
                incidencia = new Incidencia(idIncidencia, tipoIncidencia);
            }
            Zona zona = EstadoService.getInstance().getZonaByID(idZona);
            Inyeccion inyeccion = new Inyeccion(idInyeccion, dosis, fechaHora, zona, incidencia);
            inyecciones.add(inyeccion);
        }
        return inyecciones;
    }

    public Inyeccion getUltimaInyeccion(int idPluma) throws SQLException {
        Inyeccion inyeccion = null;
        connection = DBConnection.getConnection();
        String query = """
                SELECT i.dosis, i.fecha_hora, z.id_zona
                FROM inyeccion i
                INNER JOIN zona z ON z.id_zona = i.id_zona
                WHERE i.id_plumaInsulina = ?
                ORDER BY i.fecha_hora DESC
                LIMIT 1""";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, idPluma);
        resultSet = preparedStatement.executeQuery();

        if (resultSet.next()){
            double dosis = resultSet.getDouble(DBSchem.COL_INY_DOSIS);
            LocalDateTime localDateTime = (LocalDateTime) resultSet.getObject(DBSchem.COL_INY_FECHA);
            int idZona = resultSet.getInt(DBSchem.COL_ZONA_ID);
            Zona zona = EstadoService.getInstance().getZonaByID(idZona);
            inyeccion = new Inyeccion(dosis, localDateTime, zona);

        }

        return inyeccion;

    }

    /*

    Me ha parecido demasiado ilegible y he decidido finalmente solo poner en formato los nombres de las tablas
    String query = String.format("""
                SELECT i.%s, i.%s, i.%s,
                z.%s, z.%s,
                inc.%s, inc.%s
                FROM %s i
                LEFT JOIN %s inc ON i.%s = inc.%s
                INNER JOIN zona z ON z.%s = i.%s
                WHERE DATE(i.%s) >= DATE_SUB(CURDATE(), INTERVAL ? DAY)
                ORDER BY i.fecha_hora DESC;
                """, DBSchem.COL_INY_ID, DBSchem.COL_INY_FECHA, DBSchem.COL_INY_DOSIS,
                        DBSchem.COL_ZONA_ID, DBSchem.COL_ZONA_NOMBRE,
                        DBSchem.COL_INC_ID, DBSchem.COL_INC_TIPO,
                        DBSchem.TAB_INYECCION,
                        DBSchem.TAB_INCIDENCIA, DBSchem.COL_INC_INY, DBSchem.COL_INY_ID,
                        DBSchem.TAB_ZONA, DBSchem.COL_ZONA_ID, DBSchem.COL_INY_ZONA,
                        DBSchem.COL_INY_FECHA,
               DBSchem.COL_INY_FECHA);
     */
}

