package org.example.myinsuapp.dao;
import org.example.myinsuapp.database.DBConnection;
import org.example.myinsuapp.database.DBSchem;
import org.example.myinsuapp.exceptions.DataBaseException;
import org.example.myinsuapp.model.entity.Incidencia;
import org.example.myinsuapp.model.entity.Inyeccion;
import org.example.myinsuapp.model.entity.TipoIncidencia;
import org.example.myinsuapp.model.entity.Zona;
import org.example.myinsuapp.service.EstadoService;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class InyeccionDAO {
    private Connection connection;
    private ResultSet resultSet;
    private PreparedStatement preparedStatement;
    //Todo -> pendiente para futura optimización: try-with-resources era muy útil con los flujos de archivos y APIs y quiero aplicarlo aquí.
    /*
    Originalmente tenía una clase IncidenciaDAO (eliminada ya) pero después de ver en BD las transacciones la operación de
    insertar incidencia se hacía muy dependiente de la Inyeccion y me parece perfecta para una transacción.
    el metodo insertInyección es el original que manda a la bd inyecciones sin incidenciasal que le he suprimido
    el obtener los id puesto que ya no lo necesito.

    He agregado un segundo metod.o en el que mediante transacción se guardan las inyecciones con incidencia.
    Aclaro que podría haberlo controlado en un sólo métod.o discriminando if (incidencia != null) pero me parecía darle
    responsabilidades al DAO que no le corresponden. Desde Service hago la distinción y decido a cual de los dos llamar.
    Aunque repita levemente código, me ha parecido la mejor opción.

     */
    public void insertInyeccion(Inyeccion inyeccion) throws DataBaseException {
        connection = DBConnection.getConnection();
        String query = String.format("INSERT INTO %s (%s, %s, %s, %s) VALUES (?, ?, ?, ?);",
                DBSchem.TAB_INYECCION, DBSchem.COL_INY_PLUMA, DBSchem.COL_ZONA_ID,
                DBSchem.COL_INY_DOSIS, DBSchem.COL_INY_FECHA);

        try {
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, inyeccion.getPlumaInsulina().getIdPluma());
            preparedStatement.setInt(2, inyeccion.getZona().getIdZona());
            preparedStatement.setDouble(3, inyeccion.getDosis());
            preparedStatement.setObject(4, inyeccion.getHoraInyeccion());
            preparedStatement.executeUpdate();
        } catch (SQLException e){
            throw new DataBaseException("Error de conexión al registrar la inyección", e);
        }
    }

    public void insertInyeccionIncidencia(Inyeccion inyeccion) throws DataBaseException{
        connection = DBConnection.getConnection();
        String insertIny = String.format("INSERT INTO %s (%s, %s, %s, %s) VALUES (?, ?, ?, ?);",
                DBSchem.TAB_INYECCION, DBSchem.COL_INY_PLUMA, DBSchem.COL_ZONA_ID,
                DBSchem.COL_INY_DOSIS, DBSchem.COL_INY_FECHA);

        String insertIncidencia = String.format("INSERT INTO %s (%s, %s) VALUES (?, ?);",
                DBSchem.TAB_INCIDENCIA, DBSchem.COL_INC_INY, DBSchem.COL_INC_TIPO);

        try {
            connection.setAutoCommit(false);
            // En primer lugar la inyección recuperando el id que necesito para inserta la incidencia, de haberla
            preparedStatement = connection.prepareStatement(insertIny, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1,inyeccion.getPlumaInsulina().getIdPluma());
            preparedStatement.setInt(2, inyeccion.getZona().getIdZona());
            preparedStatement.setDouble(3,inyeccion.getDosis());
            preparedStatement.setObject(4, inyeccion.getHoraInyeccion());
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()){
                int idGenerada = resultSet.getInt(1);
                inyeccion.setIdInyeccion(idGenerada);
                //De momento la necesidad de tener el objeto completo es nula, pero prefiero hacerlo de cara a futuras funciones
                inyeccion.getIncidencia().setIdInyeccion(idGenerada);
            }
            preparedStatement.close();

            //Procedemos a insertar incidencia
            preparedStatement = connection.prepareStatement(insertIncidencia);
            preparedStatement.setInt(1, inyeccion.getIdInyeccion());
            preparedStatement.setString(2, String.valueOf(inyeccion.getIncidencia().getTipoIncidencia()));
            preparedStatement.executeUpdate();

            connection.commit();

        } catch (SQLException e) {
            if (connection != null){
                try {
                    connection.rollback();
                } catch (SQLException ex){
                    System.err.println(ex.getMessage());
                }
            }
            throw new DataBaseException("Error de conexión al registrar la inyección", e);
        } finally {
            if (connection != null){
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException e) {
                    System.err.println(e.getMessage());
                }
            }
        }

    }

    // DELETE de inyección
    public void borrarInyeccion(int idInyeccion) {

        connection = DBConnection.getConnection();
        String query = String.format("""
                DELETE FROM %s
                WHERE %s = ?
                AND %s >= NOW() - INTERVAL 2 HOUR;""",
                DBSchem.TAB_INYECCION,
                DBSchem.COL_INY_ID,
                DBSchem.COL_INY_FECHA
        );

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idInyeccion);
            preparedStatement.executeUpdate();
        } catch (SQLException e){
            throw new DataBaseException("Error de conexión con DB");
        }

    }



    /*
    Querie sin formateo (la he modificado un poco respecto al .sql ya que aquí no necesito tantos datos:

        SELECT
            i.id_inyeccion, i.fecha_hora, i.dosis, i.id_zona,
            inc.id_incidencia, inc.tipo_incidencia
        FROM pluma_insulina pl
        INNER JOIN inyeccion i ON i.id_plumaInsulina = pl.id_plumaInsulina
        LEFT JOIN incidencia inc ON i.id_inyeccion = inc.id_inyeccion
        WHERE pl.id_usuario = 1
        AND i.fecha_hora BETWEEN DATE_SUB(NOW(), INTERVAL 7 DAY) AND NOW()
        ORDER BY i.fecha_hora DESC;
     */

    public List<Inyeccion>getInyeccionesRango(int dias, int idUser) throws DataBaseException {
        List<Inyeccion> inyecciones = new ArrayList<>();
        connection = DBConnection.getConnection();
        String query = String.format("""
                SELECT i.%s, i.%s, i.%s, i.%s,
                       inc.%s, inc.%s
                       FROM %s i
                INNER JOIN %s pl ON i.%s = pl.%s
                LEFT JOIN %s inc ON i.%s = inc.%s
                WHERE pl.%s = ?
                AND i.%s BETWEEN DATE_SUB(NOW(), INTERVAL ? DAY) AND NOW()
                ORDER BY i.%s DESC;""", DBSchem.COL_INY_ID, DBSchem.COL_INY_FECHA, DBSchem.COL_INY_DOSIS,
                DBSchem.COL_INY_ZONA, DBSchem.COL_INC_ID, DBSchem.COL_INC_TIPO,
                DBSchem.TAB_INYECCION,
                DBSchem.TAB_PLUMA, DBSchem.COL_INY_PLUMA, DBSchem.COL_PLUMA_ID,
                DBSchem.TAB_INCIDENCIA, DBSchem.COL_INY_ID, DBSchem.COL_INC_INY,
                DBSchem.COL_PLUMA_USER,
                DBSchem.COL_INY_FECHA, DBSchem.COL_INY_FECHA
        );

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idUser);
            preparedStatement.setInt(2, dias);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                int idInyeccion = resultSet.getInt(DBSchem.COL_INY_ID);
                LocalDateTime fechaHora = resultSet.getObject(DBSchem.COL_INY_FECHA, LocalDateTime.class);
                double dosis = resultSet.getDouble(DBSchem.COL_INY_DOSIS);

                /*
                Explico esta parte... Como la busqueda era con left y quiero obtener inyecciones con o sin incidencias,
                declaro Incidencia en null, pido al resultset que me entregue el resultado y dentro del if, si el resultado
                no es nulo, pido el tipo de incidencia para crear el nuevo objeto. Si el resultado es nulo la dejo como tal
                y lo controlo en la siguiente capa.
                 */
                Incidencia incidencia = null;
                int idIncidencia = resultSet.getInt(DBSchem.COL_INC_ID);
                if (!resultSet.wasNull()){
                    TipoIncidencia tipoIncidencia = TipoIncidencia.desdeBD(resultSet.getString(DBSchem.COL_INC_TIPO));
                    incidencia = new Incidencia(idIncidencia, tipoIncidencia);
                }

                int idZona = resultSet.getInt(DBSchem.COL_INY_ZONA);
                Zona zona = EstadoService.getInstance().getZonaByID(idZona);
                Inyeccion inyeccion = new Inyeccion(idInyeccion, dosis, fechaHora, zona, incidencia);
                inyecciones.add(inyeccion);
            }

        } catch (SQLException e){
            throw new DataBaseException("Error al acceder a la consulta en base de datos.", e);
        }

        return inyecciones;
    }


    /*
    Querie sin formateo:
        SELECT
            i.id_inyeccion, i.fecha_hora, i.dosis, i.id_zona,
            inc.id_incidencia, inc.tipo_incidencia
        FROM pluma_insulina pl
        INNER JOIN inyeccion i ON i.id_plumaInsulina = pl.id_plumaInsulina
        INNER JOIN incidencia inc ON i.id_inyeccion = inc.id_inyeccion
        WHERE pl.id_usuario = 1
        AND i.fecha_hora BETWEEN DATE_SUB(NOW(), INTERVAL 7 DAY) AND NOW()
        ORDER BY i.fecha_hora DESC;
     */
    public List<Inyeccion> getInyeccionesIncidenciaRango (int dias, int idUsuario) throws DataBaseException {
        List<Inyeccion> inyeccionesIncidencias = new ArrayList<>();
        connection = DBConnection.getConnection();
        String query = String.format("""
                SELECT i.%s, i.%s, i.%s, i.%s,
                       inc.%s, inc.%s
                       FROM %s i
                INNER JOIN %s pl ON i.%s = pl.%s
                INNER JOIN %s inc ON i.%s = inc.%s
                WHERE pl.%s = ?
                AND i.%s BETWEEN DATE_SUB(NOW(), INTERVAL ? DAY) AND NOW()
                ORDER BY i.%s DESC;""", DBSchem.COL_INY_ID, DBSchem.COL_INY_FECHA, DBSchem.COL_INY_DOSIS,
                DBSchem.COL_INY_ZONA, DBSchem.COL_INC_ID, DBSchem.COL_INC_TIPO,
                DBSchem.TAB_INYECCION,
                DBSchem.TAB_PLUMA, DBSchem.COL_INY_PLUMA, DBSchem.COL_PLUMA_ID,
                DBSchem.TAB_INCIDENCIA, DBSchem.COL_INY_ID, DBSchem.COL_INC_INY,
                DBSchem.COL_PLUMA_USER,
                DBSchem.COL_INY_FECHA, DBSchem.COL_INY_FECHA
        );

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idUsuario);
            preparedStatement.setInt(2, dias);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                int idInyeccion = resultSet.getInt(DBSchem.COL_INY_ID);
                LocalDateTime fechaHora = resultSet.getObject(DBSchem.COL_INY_FECHA, LocalDateTime.class);
                double dosis = resultSet.getDouble(DBSchem.COL_INY_DOSIS);

                //Datos para crear el objeto Incidencia dentro de Inyección
                int idIncidencia = resultSet.getInt(DBSchem.COL_INC_ID);
                TipoIncidencia tipoIncidencia = TipoIncidencia.desdeBD(resultSet.getString(DBSchem.COL_INC_TIPO));
                Incidencia incidencia = new Incidencia(idIncidencia, tipoIncidencia);
                //A la zona accedo mediante id
                int idZona = resultSet.getInt(DBSchem.COL_INY_ZONA);
                Zona zona = EstadoService.getInstance().getZonaByID(idZona);

                Inyeccion inyeccion = new Inyeccion(idInyeccion, dosis, fechaHora, zona, incidencia);
                inyeccionesIncidencias.add(inyeccion);
            }
        } catch (SQLException e){
            throw new DataBaseException("Error al acceder a la consulta en base de datos.", e);
        }

        return inyeccionesIncidencias;
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

    /*
    Esta query es para obtener la última inyección registrada... le he añadido el control por fecha ya que habrás datos
    cargados a "futuro".

         SELECT i.dosis, i.fecha_hora, z.id_zona
                FROM pluma_insulina pl
                INNER JOIN inyeccion i
                ON i.id_plumaInsulina = pl.id_plumaInsulina
                INNER JOIN zona z ON z.id_zona = i.id_zona
                WHERE pl.id_usuario = 1 and i.fecha_hora <= NOW()
                ORDER BY i.fecha_hora DESC
                LIMIT 1
     */

    public Inyeccion getUltimaInyeccion(int idUser) {
        Inyeccion inyeccion = null;
        connection = DBConnection.getConnection();
        String query = String.format("""
                SELECT i.%s, i.%s, z.%s
                        FROM %s pl
                        INNER JOIN %s i ON i.%s = pl.%s
                        INNER JOIN %s z ON z.%s = i.%s
                        WHERE pl.%s = ? AND i.%s <= NOW()
                        ORDER BY i.%s DESC
                        LIMIT 1;""", DBSchem.COL_INY_DOSIS, DBSchem.COL_INY_FECHA, DBSchem.COL_ZONA_ID,
                DBSchem.TAB_PLUMA,
                DBSchem.TAB_INYECCION, DBSchem.COL_INY_PLUMA, DBSchem.COL_PLUMA_ID,
                DBSchem.TAB_ZONA, DBSchem.COL_ZONA_ID, DBSchem.COL_INY_ZONA,
                DBSchem.COL_PLUMA_USER, DBSchem.COL_INY_FECHA,
                DBSchem.COL_INY_FECHA);

        try {
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, idUser);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                double dosis = resultSet.getDouble(DBSchem.COL_INY_DOSIS);
                LocalDateTime localDateTime = (LocalDateTime) resultSet.getObject(DBSchem.COL_INY_FECHA);
                int idZona = resultSet.getInt(DBSchem.COL_ZONA_ID);
                Zona zona = EstadoService.getInstance().getZonaByID(idZona);
                inyeccion = new Inyeccion(dosis, localDateTime, zona);

            }
        } catch (SQLException e) {
            throw new DataBaseException("Error de conexión", e);
        }

        return inyeccion;

    }

}

