package org.example.myinsuapp.controller;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class InicioController implements Initializable {
    @FXML
    private Button btnIniciarPluma;

    @FXML
    private Label diasRestantesLabel;

    @FXML
    private Label fechaLabel;

    @FXML
    private AnchorPane inicio_anchor;

    @FXML
    private Label nombreLabel;

    @FXML
    private Label tipoDTLabel;

    @FXML
    private Label udRestantesLabel;

    @FXML
    private ProgressBar udRestantesProgBar;

    @FXML
    private Label ultDosisLabel;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instances();
        initGUI();
        actions();
    }

    private void instances() {

    }

    private void initGUI() {
    }

    private void actions() {
    }
}
