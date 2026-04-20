package org.example.myinsuapp.controller;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import org.example.myinsuapp.model.Incidencia;
import org.example.myinsuapp.model.Zona;

import java.net.URL;
import java.util.ResourceBundle;

public class RegistroController implements Initializable {


    @FXML
    private ToggleGroup grupoZonas;

    @FXML
    private ToggleButton btnAbdomenD;

    @FXML
    private ToggleButton btnAbdomenIz;

    @FXML
    private ToggleButton btnGluteoD;

    @FXML
    private ToggleButton btnGluteoIz;

    @FXML
    private ToggleButton btnMusloD;

    @FXML
    private ToggleButton btnMusloIz;

    @FXML
    private ToggleButton btnTicepsD;

    @FXML
    private ToggleButton btnTricepsIz;

    @FXML
    private Button btnRegistrar;

    @FXML
    private ComboBox<Incidencia> incidenciaComboBox;

    @FXML
    private Spinner<?> unidadesSpinner;

    @FXML
    private Label zonaSeleccionadaLabel;



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
