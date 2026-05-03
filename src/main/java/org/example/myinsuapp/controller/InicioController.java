package org.example.myinsuapp.controller;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.example.myinsuapp.exceptions.DataBaseException;
import org.example.myinsuapp.model.entity.Inyeccion;
import org.example.myinsuapp.model.entity.PlumaInsulina;
import org.example.myinsuapp.service.EstadoService;
import org.example.myinsuapp.service.InyeccionService;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

public class InicioController implements Initializable {

    private InyeccionService inyeccionService;
    @FXML
    private Button btnIniciarPluma;

    @FXML
    private Label udRegistradasLabel;

    @FXML
    private Label fechaLabel;

    @FXML
    private AnchorPane inicio_anchor;

    @FXML
    private Label nombreLabel;

    @FXML
    private Label tipoDTLabel;

    @FXML
    private Label diasRestantesLabel;

    @FXML
    private ProgressBar diasRestantesBar;

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

    public void initGUI() {

        nombreLabel.setText(EstadoService.getInstance().getUsuario().getNombre());
        tipoDTLabel.setText(EstadoService.getInstance().getUsuario().getTipoDiabetes().toString());
        ultDosisLabel.setText(ultimaInyeccionTexto());
        //saco este apartado a un privado porque además de cargarlo al inicio de la ventana quiero que esta parte cargue tras darle al botón
        actualizarEstadoPluma();

    }

    private void actions() {
        btnIniciarPluma.setOnAction(event ->{
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Iniciar nueva Pluma");
            confirmacion.setContentText("Al iniciar nueva pluma la anterior será dada de baja. ¿Estás de acuerdo?");
            Optional<ButtonType> respuesta = confirmacion.showAndWait();

            if (respuesta.isPresent() && respuesta.get() == ButtonType.OK) {

                try {
                    EstadoService.getInstance().iniciarPluma();
                    actualizarEstadoPluma();
                } catch (DataBaseException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                }
            }
        });

    }

    private void actualizarEstadoPluma(){
        PlumaInsulina plumaActiva = EstadoService.getInstance().getPlumaActiva();

        if (plumaActiva != null) {
            fechaLabel.setText(EstadoService.getInstance().getPlumaActiva().getFechaApertura().toString());

            double udUsadas = inyeccionService.getUnidadesUsadasPluma(plumaActiva);
            udRegistradasLabel.setText(udUsadas + " unidades");

            long diasRestantes = plumaActiva.diasRestantes();
            diasRestantesLabel.setText(diasRestantes + " días");

            comportamientoProgressBar(diasRestantes);

        }
    }

    private void comportamientoProgressBar(long dias){

        double procentajeRestante = dias / 30.0;
        diasRestantesBar.setProgress(procentajeRestante);
    }

    private String ultimaInyeccionTexto() {

        Inyeccion inyeccion = inyeccionService.ultimaInyeccion(EstadoService.getInstance().getUsuario().getIdUsuario());


        if (inyeccion == null) {
            return "Aún no hay dosis registradas";
        }

        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("dd MMM 'a las' HH:mm");
        return String.format("%.1f unidades en %s el %s",
                inyeccion.getDosis(),
                inyeccion.getZona().getZonaCuerpo(),
                inyeccion.getHoraInyeccion().format(formatoHora));
    }

}
