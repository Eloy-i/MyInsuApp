package org.example.myinsuapp.dao;

import org.example.myinsuapp.database.DBConnection;
import org.example.myinsuapp.database.DBSchem;
import org.example.myinsuapp.model.Zona;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZonaDAO {
    private Connection connection;
    private ResultSet resultSet;
    private PreparedStatement preparedStatement;

    public Map<Integer, Zona> getZonas(){
        Map<Integer, Zona> mapaZonas = new HashMap<>();
        connection = DBConnection.getConnection();
        String query = "SELECT * FROM "+ DBSchem.TAB_ZONA;

        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                int id = resultSet.getInt(DBSchem.COL_ZONA_ID);
                String nombreZona = resultSet.getString(DBSchem.COL_ZONA_NOMBRE);
                mapaZonas.put(id, new Zona(id, nombreZona));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return mapaZonas;
    }
}
