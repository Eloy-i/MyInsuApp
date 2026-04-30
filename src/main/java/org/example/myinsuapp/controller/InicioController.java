package org.example.myinsuapp.controller;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import org.example.myinsuapp.exceptions.DataBaseException;
import org.example.myinsuapp.model.PlumaInsulina;
import org.example.myinsuapp.service.EstadoService;
import org.example.myinsuapp.service.InyeccionService;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class InicioController implements Initializable {

    InyeccionService inyeccionService;
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

        inyeccionService = new InyeccionService();
    }

    private void initGUI() {
        PlumaInsulina plumaActiva = EstadoService.getInstance().getPlumaActiva();
        nombreLabel.setText(EstadoService.getInstance().getUsuario().getNombre());
        tipoDTLabel.setText(EstadoService.getInstance().getUsuario().getTipoDiabetes().toString());
        if (plumaActiva != null) {
            fechaLabel.setText(EstadoService.getInstance().getPlumaActiva().getFechaApertura().toString());

            double porcentajeRestante = inyeccionService.getPorcentajeRestantePluma(plumaActiva);
            udRestantesProgBar.setProgress(porcentajeRestante);

            double udUsadas = inyeccionService.getUnidadesUsadasPluma(plumaActiva);
            udRestantesLabel.setText(udUsadas + " unidades");

            try {
                ultDosisLabel.setText(inyeccionService.ultimaInyeccionTexto(plumaActiva));


            } catch (RuntimeException e) {
                ultDosisLabel.setText("RECURDAR ARREGLAR LA QUERY PARA QUE NO SE VINCULE A LA PLUMA, QUE SOY IDIOTA");
            }
        }



    }

    private void actions() {
        btnIniciarPluma.setOnAction(event ->{
            try {
                EstadoService.getInstance().iniciarPluma();
                initGUI();
            } catch (DataBaseException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        });

    }
}
