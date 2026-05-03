package org.example.myinsuapp.database;

import org.example.myinsuapp.exceptions.DataBaseException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static Connection connection;

    public static Connection getConnection() {
        if (connection == null){
            newConnection();
        }
        return connection;
    }


    private static void newConnection() {
        String user = "root";
        String pass = "root";
        String url = "127.0.0.1";
        String port = "3306";
        String dbName = "myinsuapp";
        String urlJDBC = String.format("jdbc:mysql://%s:%s/%s",url,port,dbName);

        try {
            connection = DriverManager.getConnection(urlJDBC, user,pass);
        } catch (SQLException e){
             throw new DataBaseException("Error de conexión. En local funcionaba, lo juro.", e );
        }


    }


}
