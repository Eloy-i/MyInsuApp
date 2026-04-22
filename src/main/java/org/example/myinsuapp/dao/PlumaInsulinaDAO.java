package org.example.myinsuapp.dao;

import org.example.myinsuapp.database.DBConnection;
import org.example.myinsuapp.database.DBSchem;
import org.example.myinsuapp.model.PlumaInsulina;
import org.example.myinsuapp.model.Usuario;

import java.sql.*;
import java.time.LocalDate;

public class PlumaInsulinaDAO {
    private Connection connection;
    private ResultSet resultSet;
    private PreparedStatement preparedStatement;

    public PlumaInsulina getPlumaActiva(Usuario usuario) throws SQLException {
        PlumaInsulina plumaInsulina = null;
        connection = DBConnection.getConnection();
        String query = String.format("SELECT * FROM %s WHERE %s = true AND %s = ?",
                DBSchem.TAB_PLUMA, DBSchem.COL_PLUMA_ACTIVO, DBSchem.COL_PLUMA_USER);

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

        return plumaInsulina;
    }

    public void desactivarPluma(int idPluma) throws SQLException {
        connection = DBConnection.getConnection();
        String query = String.format("UPDATE %s SET %s = false WHERE %s = ?",
                DBSchem.TAB_PLUMA, DBSchem.COL_PLUMA_ACTIVO, DBSchem.COL_PLUMA_ID);

        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, idPluma);
        preparedStatement.executeUpdate();
    }

    public void insertPluma(PlumaInsulina plumaInsulina) throws SQLException {
        connection = DBConnection.getConnection();
        String query = String.format("INSERT INTO %s (%s, %s, %s, %s) " +
                "VALUES (?, ?, ?, ?);",
                DBSchem.TAB_PLUMA, DBSchem.COL_PLUMA_USER, DBSchem.COL_PLUMA_DEPOSITO,
                DBSchem.COL_PLUMA_ACTIVO, DBSchem.COL_PLUMA_FECHA);

        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, plumaInsulina.getUsuario().getIdUsuario());
        preparedStatement.setInt(2, plumaInsulina.getDepositoInicial());
        preparedStatement.setBoolean(3, plumaInsulina.isActivo());
        preparedStatement.setDate(4, Date.valueOf(plumaInsulina.getFechaApertura()));
        preparedStatement.executeUpdate();

    }
}
