package org.example.myinsuapp.dao;

import org.example.myinsuapp.database.DBConnection;
import org.example.myinsuapp.database.DBSchem;
import org.example.myinsuapp.model.TipoDiabetes;
import org.example.myinsuapp.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class UsuarioDAO {
    private Connection connection;
    private ResultSet resultSet;
    private PreparedStatement preparedStatement;

    public Usuario getUsuario(){
        Usuario user = null;
        connection = DBConnection.getConnection();
        String query = "SELECT * FROM "+ DBSchem.TAB_USUARIO;

        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                int id = resultSet.getInt(DBSchem.COL_USER_ID);
                String nombre = resultSet.getString(DBSchem.COL_USER_NOMBRE);
                String apellido = resultSet.getString(DBSchem.COL_USER_APELL);
                String tDiabetesString = resultSet.getString(DBSchem.COL_USER_DT);
                TipoDiabetes tDiabetes = TipoDiabetes.valueOf(tDiabetesString); //Todo ajustar nombre de enums en base de datos para hacerlo coincidir
                LocalDate fechaNacimiento = resultSet.getDate(DBSchem.COL_USER_NACIMIENTO).toLocalDate();
                user = new Usuario(id, nombre, apellido, tDiabetes, fechaNacimiento);
            }


        } catch (SQLException e) {
            //TODO pensar si crear excepciones personalizadas para que las recoja la vista
            throw new RuntimeException(e);
        }

        return user;

    }
}
