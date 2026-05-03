package org.example.myinsuapp.dao;

import org.example.myinsuapp.database.DBConnection;
import org.example.myinsuapp.database.DBSchem;
import org.example.myinsuapp.exceptions.DataBaseException;
import org.example.myinsuapp.model.entity.TipoDiabetes;
import org.example.myinsuapp.model.entity.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class UsuarioDAO {
    private Connection connection;
    private ResultSet resultSet;
    private PreparedStatement preparedStatement;

    /*
    La busqueda del usuario va a traer a mi usuario precargado para la demo. Como las voy a enmascarar con la interfaz
    DBSchem te las iré dejando en comentarío superior a cada mét-odo para no volverte loco a ti y a mi mientras formate,
    que luego hay unas querys que van a ser "graciosas" de formatear.

        SELECT id_usuario, nombre, apellidos, fecha_nacimiento, tipo_diabetes
        FROM usuario WHERE id_usuario = 1
     */

    public Usuario getUsuario(int idUsuario){
        Usuario user = null;
        connection = DBConnection.getConnection();
        String query = String.format("""
                SELECT %s, %s, %s, %s, %s
                FROM %s WHERE %s = ?""", DBSchem.COL_USER_ID, DBSchem.COL_USER_NOMBRE, DBSchem.COL_USER_APELL,
                DBSchem.COL_USER_NACIMIENTO, DBSchem.COL_USER_DT,
                DBSchem.TAB_USUARIO, DBSchem.COL_USER_ID);

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idUsuario);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                int id = resultSet.getInt(DBSchem.COL_USER_ID);
                String nombre = resultSet.getString(DBSchem.COL_USER_NOMBRE);
                String apellido = resultSet.getString(DBSchem.COL_USER_APELL);
                String tDiabetesString = resultSet.getString(DBSchem.COL_USER_DT);
                TipoDiabetes tDiabetes = TipoDiabetes.desdeBD(tDiabetesString);
                LocalDate fechaNacimiento = resultSet.getDate(DBSchem.COL_USER_NACIMIENTO).toLocalDate();
                user = new Usuario(id, nombre, apellido, tDiabetes, fechaNacimiento);
            }


        } catch (SQLException e) {
             throw new DataBaseException("Error conexión a acceder al usuario", e);
        }
        return user;

    }

    // todo Si tengo tiempo a añadir una pantalla de identificación sencilla... lanzar quiery que compruebe nombre e id, por ejemplo.
}
