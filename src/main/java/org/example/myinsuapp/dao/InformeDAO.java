package org.example.myinsuapp.dao;

import org.example.myinsuapp.database.DBConnection;
import org.example.myinsuapp.database.DBSchem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

//TODO acabo de caer en la cuenta de algo TERRIBLE: Estoy jodido porque todo esto solo vale si hay 1 solo usuario en la base de datos, si hay dos las estadisticas se van a la mierda.

public class InformeDAO {
    private Connection connection;
    private ResultSet resultSet;
    private PreparedStatement preparedStatement;

    public double dosisTotalPeriodo(int dias) throws SQLException {
        connection = DBConnection.getConnection();
        String query = """
                SELECT SUM(i.dosis) AS total_dosis_semana
                FROM inyeccion i
                WHERE DATE(i.fecha_hora) >= DATE_SUB(CURDATE(), INTERVAL ? DAY);""";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, dias);
        resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getDouble(1);
        }
        return 0.0;
    }

    //todo tasa diaria de pinchazos se calcula en java

    public double picoMaxInsulinaPeriodo(int dias) throws SQLException {
        connection = DBConnection.getConnection();
        String query = """
                SELECT MAX(i.dosis) AS pico_max
                FROM inyeccion i
                WHERE DATE(i.fecha_hora) >= DATE_SUB(CURDATE(), INTERVAL ? DAY);""";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, dias);
        resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getDouble(1);
        }
        return 0.0;

    }

    public int totalInyeccionesPeriodo(int dias) throws SQLException{
        connection = DBConnection.getConnection();
        String query = """
                SELECT COUNT(i.dosis) AS total_inyecciones_semana
                FROM inyeccion i
                WHERE DATE(i.fecha_hora) >= DATE_SUB(CURDATE(), INTERVAL ? DAY);""";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, dias);
        resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return 0;
    }

    public int totalIncidenciasPeriodo(int dias) throws SQLException{
        connection = DBConnection.getConnection();
        String query = """
                SELECT COUNT(i.id_incidencia) AS total_incidencias
                                FROM incidencia i
                                INNER JOIN inyeccion iny
                                ON i.id_inyeccion = iny.id_inyeccion
                                WHERE DATE(iny.fecha_hora) >= DATE_SUB(CURDATE(), INTERVAL ? DAY);""";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, dias);
        resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return 0;
    }

    public double mediaInyeccionesDiarias(int dias) throws SQLException {
        connection = DBConnection.getConnection();
        String query = """
                SELECT COUNT(i.dosis) / ? AS frecuencia_inyecciones_diarias
                FROM inyeccion i
                WHERE DATE(i.fecha_hora) >= DATE_SUB(CURDATE(), INTERVAL ? DAY);""";

        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, dias);
        preparedStatement.setInt(2, dias);
        resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getDouble(1);
        }
        return 0.0;
    }

    public String zonaMasUsadaPeriodo(int dias) throws SQLException {
        connection = DBConnection.getConnection();
        String query = """
                SELECT z.zona_cuerpo AS zona_mas_usada
                            FROM zona z
                            INNER JOIN inyeccion i ON i.id_zona = z.id_zona
                            WHERE DATE(i.fecha_hora) >= DATE_SUB(CURDATE(), INTERVAL ? DAY)
                            GROUP BY z.zona_cuerpo
                            ORDER BY COUNT(i.id_inyeccion) DESC
                            LIMIT 1;""";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, dias);
        resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getString(1);
        }
        return "No disponible";
    }

    public String zonaMasIncidencias(int dias) throws SQLException {
        connection = DBConnection.getConnection();
        String query = """
                SELECT z.zona_cuerpo AS zona_mas_problematica,
                                    COUNT(inc.id_incidencia) AS total_incidencias
                                    FROM zona z
                                    INNER JOIN inyeccion i
                                    ON i.id_zona = z.id_zona
                                    INNER JOIN incidencia inc
                                    ON inc.id_inyeccion = i.id_inyeccion
                                    WHERE DATE(i.fecha_hora) >= DATE_SUB(CURDATE(), INTERVAL ? DAY)
                                    GROUP BY z.zona_cuerpo
                                    ORDER BY total_incidencias DESC
                                    LIMIT 1;""";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, dias);
        resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getString(1);
        }
        return "No disponible";
    }



    public Map<String, Integer> usoZonasPeriodo(int dias) throws SQLException {
        Map<String, Integer> mapaUsoZonas = new HashMap<>();
        connection = DBConnection.getConnection();
        String query = """
            SELECT z.zona_cuerpo, COUNT(i.id_inyeccion) AS total
            FROM zona z
            INNER JOIN inyeccion i ON i.id_zona = z.id_zona
            WHERE DATE(i.fecha_hora) >= DATE_SUB(CURDATE(), INTERVAL ? DAY)
            GROUP BY z.zona_cuerpo;""";

        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, dias);
        resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            mapaUsoZonas.put(resultSet.getString(DBSchem.COL_ZONA_NOMBRE), resultSet.getInt("total"));
        }
        return mapaUsoZonas;
    }

    public Map<String, Map<String, Integer>> incidenciasPorZona(int dias) throws SQLException {
        Map<String, Map<String, Integer>> mapa = new HashMap<>();
        connection = DBConnection.getConnection();
        String query = """
                SELECT z.zona_cuerpo,
                       inc.tipo_incidencia,
                       COUNT(i.id_inyeccion) AS total
                FROM zona z
                INNER JOIN inyeccion i
                ON i.id_zona = z.id_zona
                LEFT JOIN incidencia inc
                ON inc.id_inyeccion = i.id_inyeccion
                WHERE DATE(i.fecha_hora) >= DATE_SUB(CURDATE(), INTERVAL ? DAY)
                GROUP BY z.zona_cuerpo, inc.tipo_incidencia;""";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, dias);
        resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            String zonaCuerpo = resultSet.getString(DBSchem.COL_ZONA_NOMBRE);
            String nombreIncidencia = resultSet.getString(DBSchem.COL_INC_TIPO);
            int cantidad = resultSet.getInt("total");

            if (nombreIncidencia == null){
                nombreIncidencia = "Sin incidencia";
            }
            mapa.putIfAbsent(zonaCuerpo, new HashMap<>());
            mapa.get(zonaCuerpo).put(nombreIncidencia, cantidad);
        }
        return mapa;
    }



}
