package org.example.myinsuapp.dao;

import org.example.myinsuapp.database.DBConnection;
import org.example.myinsuapp.database.DBSchem;
import org.example.myinsuapp.exceptions.DataBaseException;
import org.example.myinsuapp.exceptions.MapaZonasEmptyException;
import org.example.myinsuapp.model.Zona;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ZonaDAO {
    private Connection connection;
    private ResultSet resultSet;
    private PreparedStatement preparedStatement;

    /*
    Zona la he modelado como una entidad catalogo fija.
    Busqueda:
        SELECT id_zona, zona_cuerpo FROM zona
     */
    public Map<Integer, Zona> getZonas(){
        Map<Integer, Zona> mapaZonas = new HashMap<>();
        connection = DBConnection.getConnection();
        String query = String.format("SELECT %s, %s FROM %s;",
                DBSchem.COL_ZONA_ID, DBSchem.COL_ZONA_NOMBRE, DBSchem.TAB_ZONA);

        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                int id = resultSet.getInt(DBSchem.COL_ZONA_ID);
                String nombreZona = resultSet.getString(DBSchem.COL_ZONA_NOMBRE);
                mapaZonas.put(id, new Zona(id, nombreZona));
            }
        } catch (SQLException e) {
            throw new MapaZonasEmptyException("Error al cargar Zonas desde la base de datos", e);
        }
        return mapaZonas;
    }
}
