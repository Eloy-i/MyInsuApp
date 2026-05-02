package org.example.myinsuapp.dao;

import org.example.myinsuapp.database.DBConnection;
import org.example.myinsuapp.database.DBSchem;
import org.example.myinsuapp.exceptions.DataBaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class InformeDAO {
    private Connection connection;
    private ResultSet resultSet;
    private PreparedStatement preparedStatement;

    /*
    Esta clase es para las consultas agregadas y compuestas... Teniendo una base de datos así lo ideal era cruzar los datos
    y sacar información útil que sirva al usuario. Está información la mando a objetos dto que usaré para pintar en el
    controller de la vista Informe y sacar un documento xml con los datos recogidos.
    Voy a enmascarar las consultas y parametrizar, encima de cada méto.do pongo la consulta en crudo
     */



    /*
    Esta consulta la he añadido como pequeño filtro previo cuando el usuario es nuevo. Para que genere solo el informe
    si tiene la antiguedad que reclama con su rango de días.
    SELECT DATEDIFF(CURDATE(), MIN(i.fecha_hora)) AS dias_uso
    FROM inyeccion i
        INNER JOIN pluma_insulina pl ON i.id_plumaInsulina = pl.id_plumaInsulina
    WHERE pl.id_usuario = 1;
     */
    public int diasDesdePrimerRegistro(int idUsuerio) throws DataBaseException{
        connection = DBConnection.getConnection();
        String query = String.format("""
                SELECT DATEDIFF(CURDATE(), MIN(i.%s)) AS dias_uso
                FROM %s i
                INNER JOIN %s pl ON i.%s = pl.%s
                WHERE pl.%s = ?;""", DBSchem.COL_INY_FECHA,
                DBSchem.TAB_INYECCION,
                DBSchem.TAB_PLUMA, DBSchem.COL_INY_PLUMA, DBSchem.COL_PLUMA_ID,
                DBSchem.COL_PLUMA_USER);

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idUsuerio);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return resultSet.getInt("dias_uso");
            }
        } catch (SQLException e) {
            throw new DataBaseException("Error de conexión", e);
        }
        return 0;
    }

    /*
    SELECT SUM(i.dosis) AS total_dosis
    FROM inyeccion i
    INNER JOIN pluma_insulina pl
    ON i.id_plumaInsulina = pl.id_plumaInsulina
    WHERE pl.id_usuario = ? AND DATE(i.fecha_hora) >= DATE_SUB(CURDATE(), INTERVAL ? DAY);
     */

    public double dosisTotalPeriodo(int idUser, int dias) throws DataBaseException {
        connection = DBConnection.getConnection();
        String query = String.format("""
                SELECT SUM(i.%s) AS total_dosis
                    FROM %s i
                    INNER JOIN %s pl
                    ON i.%s = pl.%s
                    WHERE pl.%s = ? AND DATE(i.%s) >= DATE_SUB(CURDATE(), INTERVAL ? DAY);""",
                DBSchem.COL_INY_DOSIS,
                DBSchem.TAB_INYECCION, DBSchem.TAB_PLUMA,
                DBSchem.COL_PLUMA_ID, DBSchem.COL_INY_PLUMA,
                DBSchem.COL_PLUMA_USER, DBSchem.COL_INY_FECHA);
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idUser);
            preparedStatement.setInt(2, dias);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble(1);
            }
        } catch (SQLException e){
            throw new DataBaseException("Error de conexión", e);
        }
        //En estos métodos
        return 0.0;
    }


    /*
    Busqueda:
        SELECT MAX(i.dosis) AS pico_max
        FROM inyeccion i
        INNER JOIN pluma_insulina pl
        ON i.id_plumaInsulina = pl.id_plumaInsulina
        WHERE pl.id_usuario = ?
        AND DATE(i.fecha_hora) >= DATE_SUB(CURDATE(), INTERVAL ? DAY);
     */
    public double picoMaxInsulinaPeriodo(int idUsuario, int dias) throws DataBaseException {
        connection = DBConnection.getConnection();
        String query = String.format("""
                SELECT MAX(i.%s) AS pico_max
                        FROM %s i
                        INNER JOIN %s pl
                        ON i.%s = pl.%s
                        WHERE pl.%s = ?
                        AND DATE(i.%s) >= DATE_SUB(CURDATE(), INTERVAL ? DAY);""",
                DBSchem.COL_INY_DOSIS,
                DBSchem.TAB_INYECCION, DBSchem.TAB_PLUMA,
                DBSchem.COL_INY_PLUMA, DBSchem.COL_PLUMA_ID,
                DBSchem.COL_PLUMA_USER,
                DBSchem.COL_INY_FECHA);
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idUsuario);
            preparedStatement.setInt(2, dias);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble(1);
            }
        } catch (SQLException e){
            throw new DataBaseException("Error de conexión", e);
        }

        return 0.0;
    }

    /*
    SELECT COUNT(i.dosis) AS total_inyecciones
    FROM inyeccion i
    INNER JOIN pluma_insulina pl
    ON i.id_plumaInsulina = pl.id_plumaInsulina
    WHERE pl.id_usuario = 1 AND
    DATE(i.fecha_hora) >= DATE_SUB(CURDATE(), INTERVAL 7 DAY);
     */

    public int totalInyeccionesPeriodo(int idUsuario, int dias) throws DataBaseException{
        connection = DBConnection.getConnection();
        String query = String.format("""
                SELECT COUNT(i.%s) AS total_inyecciones
                    FROM %s i
                    INNER JOIN %s pl
                    ON i.%s = pl.%s
                    WHERE pl.%s = ? AND
                    DATE(i.%s) >= DATE_SUB(CURDATE(), INTERVAL ? DAY);""",
                DBSchem.COL_INY_DOSIS,
                DBSchem.TAB_INYECCION, DBSchem.TAB_PLUMA,
                DBSchem.COL_INY_PLUMA, DBSchem.COL_PLUMA_ID,
                DBSchem.COL_PLUMA_USER,
                DBSchem.COL_INY_FECHA);

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,idUsuario);
            preparedStatement.setInt(2, dias);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e){
            throw new DataBaseException("Error de conexión", e);
        }
        return 0;
    }

    /*
    SELECT COUNT(inc.id_incidencia) AS total_incidencias
        FROM incidencia inc
        INNER JOIN inyeccion i ON i.id_inyeccion = inc.id_inyeccion
        INNER JOIN Pluma_Insulina pl
        ON i.id_plumaInsulina = pl.id_plumaInsulina
        WHERE pl.id_usuario = 1 AND
        DATE(i.fecha_hora) >= DATE_SUB(CURDATE(), INTERVAL 7 DAY);
     */

    public int totalIncidenciasPeriodo(int idUsuario, int dias) throws DataBaseException{
        connection = DBConnection.getConnection();
        String query = String.format("""
                SELECT COUNT(inc.%s) AS total_incidencias
                FROM %s inc
                INNER JOIN %s i ON i.%s = inc.%s
                INNER JOIN %s pl
                ON i.%s = pl.%s
                WHERE pl.%s = ? AND
                DATE(i.%s) >= DATE_SUB(CURDATE(), INTERVAL ? DAY);""",
                DBSchem.COL_INC_ID,
                DBSchem.TAB_INCIDENCIA,
                DBSchem.TAB_INYECCION, DBSchem.COL_INY_ID, DBSchem.COL_INC_INY,
                DBSchem.TAB_PLUMA, DBSchem.COL_INY_PLUMA, DBSchem.COL_PLUMA_ID,
                DBSchem.COL_PLUMA_USER,
                DBSchem.COL_INY_FECHA);

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idUsuario);
            preparedStatement.setInt(2, dias);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }catch (SQLException e){
            throw new DataBaseException("Error de conexión",e);
        }

        return 0;
    }

    /*
       SELECT z.zona_cuerpo AS zona_mas_usada
        FROM zona z
    INNER JOIN inyeccion i ON z.id_zona = i.id_zona
    INNER JOIN pluma_insulina pl ON i.id_plumaInsulina = pl.id_plumaInsulina
    WHERE pl.id_usuario = ?
    AND DATE(i.fecha_hora) >= DATE_SUB(CURDATE(), INTERVAL ? DAY)
    GROUP BY z.id_zona, z.zona_cuerpo
    ORDER BY COUNT(i.id_inyeccion) DESC
    LIMIT 1;
     */
    public String zonaMasUsadaPeriodo(int idUsuario, int dias) throws DataBaseException {
        connection = DBConnection.getConnection();
        String query = String.format("""
                SELECT z.%s AS zona_mas_usada
                FROM %s z
                INNER JOIN %s i ON z.%s = i.%s
                INNER JOIN %s pl ON i.%s = pl.%s
                WHERE pl.%s = ?
                AND DATE(i.%s) >= DATE_SUB(CURDATE(), INTERVAL ? DAY)
                GROUP BY z.%s, z.%s
                ORDER BY COUNT(i.%s) DESC
                LIMIT 1;""",
                DBSchem.COL_ZONA_NOMBRE,
                DBSchem.TAB_ZONA,
                DBSchem.TAB_INYECCION, DBSchem.COL_ZONA_ID, DBSchem.COL_INY_ZONA,
                DBSchem.TAB_PLUMA, DBSchem.COL_INY_PLUMA, DBSchem.COL_PLUMA_ID,
                DBSchem.COL_PLUMA_USER,
                DBSchem.COL_INY_FECHA,
                DBSchem.COL_ZONA_ID, DBSchem.COL_ZONA_NOMBRE,
                DBSchem.COL_INY_ID
        );

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idUsuario);
            preparedStatement.setInt(2, dias);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString(1);
            }
        } catch (SQLException e){
            throw new DataBaseException("Error de conexión", e);
        }
        return "No disponible";
    }

    /*
    SELECT z.zona_cuerpo AS zona_mas_problematica
    FROM zona z
    INNER JOIN inyeccion i ON z.id_zona = i.id_zona
    INNER JOIN incidencia inc ON inc.id_inyeccion = i.id_inyeccion
    INNER JOIN pluma_insulina pl ON i.id_plumaInsulina = pl.id_plumaInsulina
    WHERE pl.id_usuario = ?
    AND DATE(i.fecha_hora) >= DATE_SUB(CURDATE(), INTERVAL ? DAY)
    GROUP BY z.id_zona, z.zona_cuerpo
    ORDER BY COUNT(inc.id_incidencia) DESC
    LIMIT 1;
     */
    public String zonaMasIncidencias(int idUsuario, int dias) throws DataBaseException {
        connection = DBConnection.getConnection();
        String query = String.format("""
                SELECT z.%s AS zona_mas_problematica
                FROM %s z
                INNER JOIN %s i ON z.%s = i.%s
                INNER JOIN %s inc ON inc.%s = i.%s
                INNER JOIN %s pl ON i.%s = pl.%s
                WHERE pl.%s = ?
                AND DATE(i.%s) >= DATE_SUB(CURDATE(), INTERVAL ? DAY)
                GROUP BY z.%s, z.%s
                ORDER BY COUNT(inc.%s) DESC
                LIMIT 1;""",
                DBSchem.COL_ZONA_NOMBRE,
                DBSchem.TAB_ZONA,
                DBSchem.TAB_INYECCION, DBSchem.COL_ZONA_ID, DBSchem.COL_INY_ZONA,
                DBSchem.TAB_INCIDENCIA, DBSchem.COL_INC_INY, DBSchem.COL_INY_ID,
                DBSchem.TAB_PLUMA, DBSchem.COL_INY_PLUMA, DBSchem.COL_PLUMA_ID,
                DBSchem.COL_PLUMA_USER,
                DBSchem.COL_INY_FECHA,
                DBSchem.COL_ZONA_ID, DBSchem.COL_ZONA_NOMBRE,
                DBSchem.COL_INC_ID
        );

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idUsuario);
            preparedStatement.setInt(2, dias);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString(1);
            }
        } catch (SQLException e){
            throw new DataBaseException("Error de conexión", e);
        }
        return "No disponible";
    }



    /*
        SELECT z.zona_cuerpo AS zonas_cuerpo,
        COUNT(i.id_inyeccion) AS total_inyecciones
        FROM zona z
        INNER JOIN inyeccion i ON i.id_zona = z.id_zona
        INNER JOIN pluma_insulina pl ON i.id_plumaInsulina = pl.id_plumaInsulina
        WHERE pl.id_usuario = ?
        AND DATE(i.fecha_hora) >= DATE_SUB(CURDATE(), INTERVAL ? DAY)
        GROUP BY z.id_zona, z.zona_cuerpo
    */
    public Map<String, Integer> usoZonasPeriodo(int idUsuario, int dias) throws DataBaseException {
        Map<String, Integer> mapaUsoZonas = new HashMap<>();
        connection = DBConnection.getConnection();
        String query = String.format("""
            SELECT z.%s AS zonas_cuerpo,
            COUNT(i.%s) AS total_inyecciones
            FROM %s z
            INNER JOIN %s i ON i.%s = z.%s
            INNER JOIN %s pl ON i.%s = pl.%s
            WHERE pl.%s = ?
            AND DATE(i.%s) >= DATE_SUB(CURDATE(), INTERVAL ? DAY)
            GROUP BY z.%s, z.%s;""",
                DBSchem.COL_ZONA_NOMBRE,
                DBSchem.COL_INY_ID,
                DBSchem.TAB_ZONA,
                DBSchem.TAB_INYECCION, DBSchem.COL_INY_ZONA, DBSchem.COL_ZONA_ID,
                DBSchem.TAB_PLUMA, DBSchem.COL_INY_PLUMA, DBSchem.COL_PLUMA_ID,
                DBSchem.COL_PLUMA_USER,
                DBSchem.COL_INY_FECHA,
                DBSchem.COL_ZONA_ID, DBSchem.COL_ZONA_NOMBRE);

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idUsuario);
            preparedStatement.setInt(2, dias);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                mapaUsoZonas.put(resultSet.getString("zonas_cuerpo"), resultSet.getInt("total_inyecciones"));
            }
        } catch (SQLException e){
            throw new DataBaseException("Error de conexión", e);
        }

        return mapaUsoZonas;
    }

    /*
          SELECT
            z.zona_cuerpo AS zonas_cuerpo,
            inc.tipo_incidencia,
            COUNT(i.id_inyeccion) AS total
        FROM zona z
        INNER JOIN inyeccion i ON i.id_zona = z.id_zona
        LEFT JOIN incidencia inc ON inc.id_inyeccion = i.id_inyeccion
        INNER JOIN pluma_insulina pl ON i.id_plumaInsulina = pl.id_plumaInsulina
        WHERE pl.id_usuario = 1
          AND DATE(i.fecha_hora) >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)
        GROUP BY z.id_zona, z.zona_cuerpo, inc.tipo_incidencia

        Este map va a consistir en zona_cuerpo (no repetidas key) -> map interno con [nombreIncidencia (sin incidencia si es null -> cantidad]
*/

    public Map<String, Map<String, Integer>> incidenciasPorZona(int idUsuario, int dias) throws DataBaseException {
        Map<String, Map<String, Integer>> mapa = new HashMap<>();
        connection = DBConnection.getConnection();
        String query = String.format("""
            SELECT z.%s AS zonas_cuerpo,
                   inc.%s,
                   COUNT(i.%s) AS total_inyecciones
            FROM %s z
            INNER JOIN %s i ON i.%s = z.%s
            LEFT JOIN %s inc ON inc.%s = i.%s
            INNER JOIN %s pl ON i.%s = pl.%s
            WHERE pl.%s = ?
              AND DATE(i.%s) >= DATE_SUB(CURDATE(), INTERVAL ? DAY)
            GROUP BY z.%s, z.%s, inc.%s;""",
                DBSchem.COL_ZONA_NOMBRE,
                DBSchem.COL_INC_TIPO,
                DBSchem.COL_INY_ID,
                DBSchem.TAB_ZONA,
                DBSchem.TAB_INYECCION, DBSchem.COL_INY_ZONA, DBSchem.COL_ZONA_ID,
                DBSchem.TAB_INCIDENCIA, DBSchem.COL_INC_INY, DBSchem.COL_INY_ID,
                DBSchem.TAB_PLUMA, DBSchem.COL_INY_PLUMA, DBSchem.COL_PLUMA_ID,
                DBSchem.COL_PLUMA_USER,
                DBSchem.COL_INY_FECHA,
                DBSchem.COL_ZONA_ID, DBSchem.COL_ZONA_NOMBRE, DBSchem.COL_INC_TIPO);

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idUsuario);
            preparedStatement.setInt(2, dias);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                String zonaCuerpo = resultSet.getString("zonas_cuerpo");
                String nombreIncidencia = resultSet.getString(DBSchem.COL_INC_TIPO);
                int cantidad = resultSet.getInt("total_inyecciones");

                if (nombreIncidencia == null){
                    nombreIncidencia = "Sin incidencia";
                }
                mapa.putIfAbsent(zonaCuerpo, new HashMap<>());
                mapa.get(zonaCuerpo).put(nombreIncidencia, cantidad);
            }
        } catch (SQLException e){
            throw new DataBaseException("Eror de conexión ", e);
        }
        return mapa;
    }



}
