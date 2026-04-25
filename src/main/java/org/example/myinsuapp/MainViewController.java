package org.example.myinsuapp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import org.example.myinsuapp.service.EstadoService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {

    Parent inicioView, registroView, historicoView, informeView;

    @FXML
    private Button btnHistorico;

    @FXML
    private Button btnInforme;

    @FXML
    private Button btnInicio;

    @FXML
    private Button btnRegistro;

    @FXML
    private BorderPane mainBorderPane;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instances();
        initGUI();
        actions();
    }


    private void instances() {
        try {
            EstadoService.getInstance().cargaInicial();
            inicioView = FXMLLoader.load(MainViewController.class.getResource("inicio-view.fxml"));
            registroView = FXMLLoader.load(MainViewController.class.getResource("registro-view.fxml"));
            historicoView = FXMLLoader.load(MainViewController.class.getResource("historico-view.fxml"));
            informeView = FXMLLoader.load(MainViewController.class.getResource("informe-view.fxml"));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    private void initGUI() {
        mainBorderPane.setCenter(inicioView);
    }



    private void actions() {
        btnInicio.setOnAction(event -> mainBorderPane.setCenter(inicioView));
        btnRegistro.setOnAction(event -> mainBorderPane.setCenter(registroView));
        btnHistorico.setOnAction(event-> mainBorderPane.setCenter(historicoView));
        btnInforme.setOnAction( event -> mainBorderPane.setCenter(informeView));

    }
}
