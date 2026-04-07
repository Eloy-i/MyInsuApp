module org.example.myinsuapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.example.myinsuapp to javafx.fxml;
    exports org.example.myinsuapp;
}