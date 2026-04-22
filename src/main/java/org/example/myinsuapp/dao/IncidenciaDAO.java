package org.example.myinsuapp.dao;

import org.example.myinsuapp.database.DBConnection;
import org.example.myinsuapp.database.DBSchem;
import org.example.myinsuapp.model.Incidencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IncidenciaDAO {
    private Connection connection;
    private ResultSet resultSet;
    private PreparedStatement preparedStatement;


    public void insertIncidencia(Incidencia incidencia) throws SQLException {
        connection = DBConnection.getConnection();
        String query = String.format("INSERT INTO %s (%s, %s) VALUES (?, ?);",
                DBSchem.TAB_INCIDENCIA, DBSchem.COL_INC_INY, DBSchem.COL_INC_TIPO);
        preparedStatement = connection.prepareStatement(query);

        preparedStatement.setInt(1, incidencia.getInyeccionIncidencia().getIdInyeccion());
        preparedStatement.setString(2, String.valueOf(incidencia.getTipoIncidencia()));
        preparedStatement.executeUpdate();
    }

}
