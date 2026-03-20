module org.example.myinsuapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.myinsuapp to javafx.fxml;
    exports org.example.myinsuapp;
}