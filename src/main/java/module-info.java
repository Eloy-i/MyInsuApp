module org.example.myinsuapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.example.myinsuapp to javafx.fxml;
    exports org.example.myinsuapp;
    opens org.example.myinsuapp.controller to javafx.fxml;
    exports org.example.myinsuapp.controller;
}