package org.example.myinsuapp.controller;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.example.myinsuapp.model.PlumaInsulina;
import org.example.myinsuapp.model.TipoIncidencia;
import org.example.myinsuapp.model.Zona;
import org.example.myinsuapp.service.EstadoService;
import org.example.myinsuapp.service.InyeccionService;

import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.ResourceBundle;

public class RegistroController implements Initializable {
    private InyeccionService inyeccionService;


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
    private ComboBox<TipoIncidencia> incidenciaComboBox;

    @FXML
    private Spinner<Double> unidadesSpinner;

    @FXML
    private Label zonaSeleccionadaLabel;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instances();
        initGUI();
        actions();
    }

    private void instances() {
        inyeccionService = new InyeccionService();
        grupoZonas = new ToggleGroup();

    }

    /*
     btnAbdomenD.setToggleGroup(grupoZonas);
        btnAbdomenIz.setToggleGroup(grupoZonas);
        btnMusloD.setToggleGroup(grupoZonas);
        btnMusloIz.setToggleGroup(grupoZonas);
        btnTicepsD.setToggleGroup(grupoZonas);
        btnTricepsIz.setToggleGroup(grupoZonas);
        btnGluteoD.setToggleGroup(grupoZonas);
        btnGluteoIz.setToggleGroup(grupoZonas);

        btnAbdomenD.setUserData(EstadoService.getInstance().getZonaByID(1));
        btnAbdomenIz.setUserData(EstadoService.getInstance().getZonaByID(2));
        btnMusloD.setUserData(EstadoService.getInstance().getZonaByID(3));
        btnMusloIz.setUserData(EstadoService.getInstance().getZonaByID(4));
        btnTicepsD.setUserData(EstadoService.getInstance().getZonaByID(5));
        btnTricepsIz.setUserData(EstadoService.getInstance().getZonaByID(6));
        btnGluteoD.setUserData(EstadoService.getInstance().getZonaByID(7));
        btnGluteoIz.setUserData(EstadoService.getInstance().getZonaByID(8));

        Quizá esto es una chorrada pero viendo esto que he escrito he pensado que un array era la solución limpia en
        cuanto a código.
     */
    private void initGUI() {
        ToggleButton[] botonesZonas = {btnAbdomenD, btnAbdomenIz, btnMusloD, btnMusloIz, btnTicepsD, btnTricepsIz,
                btnGluteoD, btnGluteoIz};
        for (int i = 0; i < botonesZonas.length; i++) {
            botonesZonas[i].setToggleGroup(grupoZonas);
            botonesZonas[i].setUserData(EstadoService.getInstance().getZonaByID(i+1));
        }
        incidenciaComboBox.setItems(FXCollections.observableArrayList(TipoIncidencia.values()));

        unidadesSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 30, 0.0, 0.5));




    }

    private void actions() {
        btnRegistrar.setOnAction(event ->{
            PlumaInsulina plumaActiva = EstadoService.getInstance().getPlumaActiva();

            Toggle btnSelecionado = grupoZonas.getSelectedToggle();

            if (btnSelecionado == null){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Seleccion de Zonas erronea");
                alert.setContentText("Para poder proceder al registro hay que seleccionar una zona.");
                alert.showAndWait();
            }
            Zona zonaSeleccionada = (Zona)btnSelecionado.getUserData();

            double dosis = unidadesSpinner.getValue();
            TipoIncidencia tipoIncidencia = incidenciaComboBox.getValue();
            try {
                inyeccionService.registrarInyeccion(plumaActiva, zonaSeleccionada,dosis, tipoIncidencia);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Registro correcto");
                alert.setContentText("El registro se ha realizado correctamente");
                alert.showAndWait();
            } catch (SQLException e) {
                System.out.println("Pensar como gestionar errores con personalizadas");
                System.out.println(e.getMessage());
            } catch (Exception e){
                System.out.println(e.getMessage());
            }


        });

        grupoZonas.selectedToggleProperty().addListener((observableValue,
                                                         toggle, t1) -> {
            if (t1 != null){
                Zona zonaSeleccionada = (Zona) t1.getUserData();
                zonaSeleccionadaLabel.setText(zonaSeleccionada.getZonaCuerpo());
            } else {
                zonaSeleccionadaLabel.setText("Selecciona una zona...");
            }
        } );
    }
}
