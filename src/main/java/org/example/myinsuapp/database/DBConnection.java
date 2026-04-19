package org.example.myinsuapp.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static Connection connection;

    //TODO lanzar excepcion o capturar


    public static Connection getConnection() throws SQLException {
        if (connection == null){
            newConnection();
        }
        return connection;
    }

    private static void newConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/myinsuapp_prueba";
        connection = DriverManager.getConnection(url, "root","");
    }


}
