package org.example.myinsuapp.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static Connection connection;

    //TODO lanzar excepcion o capturar -> Borja comenta que si no se puede resolver por quien sa la app mejor capturar aquí.


    public static Connection getConnection() {
        if (connection == null){
            newConnection();
        }
        return connection;
    }


    private static void newConnection() {
        String user = "root";
        String pass = "";
        String url = "127.0.0.1";
        String port = "3306";
        String dbName = "myinsuapp_prueba";
        String urlJDBC = String.format("jdbc:mysql://%s:%s/%s",url,port,dbName);

        try {
            connection = DriverManager.getConnection(urlJDBC, user,pass);
        } catch (SQLException e){
             throw new RuntimeException(); //todo preparar excepciones para un ALERT en javafx
        }


    }


}
