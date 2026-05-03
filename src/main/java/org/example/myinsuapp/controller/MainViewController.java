package org.example.myinsuapp.controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import org.example.myinsuapp.exceptions.DataBaseException;
import org.example.myinsuapp.service.EstadoService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {

    private Parent inicioView, registroView, historicoView, informeView;

    private InicioController inicioController;

    private boolean baseDatosCaida = false;

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

            FXMLLoader cargaInicioVista = new FXMLLoader(getClass().getResource("/org/example/myinsuapp/inicio-view.fxml"));
            inicioView = cargaInicioVista.load();
            this.inicioController = cargaInicioVista.getController();

            registroView = FXMLLoader.load(MainViewController.class.getResource("/org/example/myinsuapp/registro-view.fxml"));
            historicoView = FXMLLoader.load(MainViewController.class.getResource("/org/example/myinsuapp/historico-view.fxml"));
            informeView = FXMLLoader.load(MainViewController.class.getResource("/org/example/myinsuapp/informe-view.fxml"));


        } catch (IOException e) {
            System.err.println(e.getMessage());
        }catch (DataBaseException e) {
            baseDatosCaida = true;
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de conexión");
            alert.setContentText(e.getMessage());
            alert.showAndWait();

        }

    }


    private void initGUI() {

        if (baseDatosCaida){
            try {
                mainBorderPane.setCenter(FXMLLoader.load(getClass().getResource("/org/example/myinsuapp/en-local-funcionaba-view.fxml")));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            mainBorderPane.setCenter(inicioView);
        }
    }



    private void actions() {
        if (baseDatosCaida){
            btnInicio.setDisable(true);
            btnRegistro.setDisable(true);
            btnHistorico.setDisable(true);
            btnInforme.setDisable(true);
        }else {
            btnInicio.setOnAction(event -> {
                mainBorderPane.setCenter(inicioView);
                inicioController.initGUI();
            });
            btnRegistro.setOnAction(event -> mainBorderPane.setCenter(registroView));
            btnHistorico.setOnAction(event -> mainBorderPane.setCenter(historicoView));
            btnInforme.setOnAction(event -> mainBorderPane.setCenter(informeView));
        }

    }
}
