package org.example.myinsuapp.dao;

import org.example.myinsuapp.database.DBConnection;
import org.example.myinsuapp.database.DBSchem;
import org.example.myinsuapp.model.Inyeccion;

import java.sql.*;
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


    //Todo esto debe recibir de JAva 7 o 30 días... Necesito además otro método si el usuario solo quiere ver sus incidencias
    // TODO Composición coyons... necesito que Incidencias sea parte de Inyección en java para poder discriminar y usar el tableview y necesito que el query devuelva los objetos completos.

   /* public List<Inyeccion>getInyeccionesRango(){
        List<Inyeccion> inyecciones = new ArrayList<>();
        connection = DBConnection.getConnection();
        String query = String.format()
    }

    */
}
