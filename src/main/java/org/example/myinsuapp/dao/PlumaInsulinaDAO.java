package org.example.myinsuapp.dao;

import org.example.myinsuapp.database.DBConnection;
import org.example.myinsuapp.database.DBSchem;
import org.example.myinsuapp.exceptions.DataBaseException;
import org.example.myinsuapp.model.PlumaInsulina;
import org.example.myinsuapp.model.Usuario;

import java.sql.*;
import java.time.LocalDate;

public class PlumaInsulinaDAO {
    private Connection connection;
    private ResultSet resultSet;
    private PreparedStatement preparedStatement;


    /*
    Este primer meto.do devuelve la pluma activa del user que haya en la bd... Si aún no hubiera se guarda null y se
    controla en EstadoService
    La busqueda:
        SELECT pl.id_plumaInsulina, pl.deposito_inicial, pl.activo, pl.fecha_apertura
        FROM pluma_insulina pl WHERE pl.activo = true AND pl.id_usuario = 1
     */
    public PlumaInsulina getPlumaActiva(Usuario usuario) {
        PlumaInsulina plumaInsulina = null;
        connection = DBConnection.getConnection();
        String query = String.format("""
                SELECT %s, %s, %s, %s
                FROM %s WHERE %s = true AND %s = ?""", DBSchem.COL_PLUMA_ID, DBSchem.COL_PLUMA_DEPOSITO,
                DBSchem.COL_PLUMA_ACTIVO, DBSchem.COL_PLUMA_FECHA,
                DBSchem.TAB_PLUMA, DBSchem.COL_PLUMA_ACTIVO, DBSchem.COL_USER_ID);

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, usuario.getIdUsuario());
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                int id = resultSet.getInt(DBSchem.COL_PLUMA_ID);
                int deposito = resultSet.getInt(DBSchem.COL_PLUMA_DEPOSITO);
                boolean activo = resultSet.getBoolean(DBSchem.COL_PLUMA_ACTIVO);
                LocalDate fechaApertura = resultSet.getDate(DBSchem.COL_PLUMA_FECHA).toLocalDate();
                plumaInsulina = new PlumaInsulina(id, deposito, usuario, activo, fechaApertura);
            }

        }catch (SQLException e){
            throw new DataBaseException("Error a cargar la Pluma de insulina activa", e);
        }
        return plumaInsulina;

    }

    /*
    El méto.do de registro de una pluma nueva va precedido de un UPDATE que da de baja la anterior, modificando el campo
    "activo" a false. Para prevenir el fallo de que el usuario tenga dos plumas activas a la vez lo he planteado como una
    transacción en la que primero se procede a la baja de la atenterior pluma y posteriormente el alta de la nueva.

    Añado sentencias
        Update:
            UPDATE Pluma_Insulina
            SET activo = 0
            WHERE id_usuario = 1 AND activo = 1;

        Insert:
        INSERT INTO Pluma_Insulina (id_usuario, deposito_inicial, activo, fecha_apertura)
        VALUES (?, ?, ?, ?);

     */


    public void registrarPlumaNueva(PlumaInsulina plumaInsulina){
        connection = DBConnection.getConnection();

        String update = String.format("""
                UPDATE %s SET %s = 0
                WHERE %s = ? AND %s = 1;""", DBSchem.TAB_PLUMA, DBSchem.COL_PLUMA_ACTIVO,
                DBSchem.COL_PLUMA_USER, DBSchem.COL_PLUMA_ACTIVO);

        String insert = String.format("INSERT INTO %s (%s, %s, %s, %s) " +
                "VALUES (?, ?, ?, ?);",DBSchem.TAB_PLUMA, DBSchem.COL_PLUMA_USER, DBSchem.COL_PLUMA_DEPOSITO,
                DBSchem.COL_PLUMA_ACTIVO, DBSchem.COL_PLUMA_FECHA);

        try {
            connection.setAutoCommit(false);
            // primero desconecto las plumas anteriores, funciona igualmente si no hay plumas y es primer inicio.
            preparedStatement = connection.prepareStatement(update);
            preparedStatement.setInt(1, plumaInsulina.getUsuario().getIdUsuario());
            preparedStatement.executeUpdate();
            preparedStatement.close();

            // Después inserto la pluma nueva solicitada en la vista y gestionada en service y le inyecto el id generado
            preparedStatement = connection.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, plumaInsulina.getUsuario().getIdUsuario());
            preparedStatement.setInt(2, plumaInsulina.getDepositoInicial());
            preparedStatement.setBoolean(3, plumaInsulina.isActivo());
            preparedStatement.setDate(4, Date.valueOf(plumaInsulina.getFechaApertura()));
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()){
                plumaInsulina.setIdPluma(resultSet.getInt(1));
            }
            connection.commit();


        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    System.err.println(ex.getMessage());
                }
            }
            throw new DataBaseException("Mensaje a poner",e);
        } finally {

            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException e) {
                    System.err.println(e.getMessage());
                }
            }
        }
    }

}
