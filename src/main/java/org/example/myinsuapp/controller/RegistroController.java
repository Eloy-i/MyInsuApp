package org.example.myinsuapp.controller;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.example.myinsuapp.exceptions.DataBaseException;
import org.example.myinsuapp.exceptions.ReglaInyeccionException;
import org.example.myinsuapp.model.entity.PlumaInsulina;
import org.example.myinsuapp.model.entity.TipoIncidencia;
import org.example.myinsuapp.model.entity.Zona;
import org.example.myinsuapp.service.EstadoService;
import org.example.myinsuapp.service.InyeccionService;

import java.net.URL;
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
                return;
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
                resetearFormulario();
            } catch (DataBaseException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error de conexión");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            } catch (ReglaInyeccionException ex){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Registro no disponible");
                alert.setContentText(ex.getMessage());
                alert.showAndWait();
            }
        });

        grupoZonas.selectedToggleProperty().addListener((observableValue,
                                                         toggle, t1) -> {
            if (t1 != null){
                Zona zonaSeleccionada = (Zona) t1.getUserData();
                zonaSeleccionadaLabel.setText(zonaSeleccionada.getZonaCuerpo().toUpperCase());
            } else {
                zonaSeleccionadaLabel.setText("Selecciona una zona...");
            }
        } );

    }

    private void resetearFormulario(){
        grupoZonas.selectToggle(null);
        incidenciaComboBox.getSelectionModel().clearSelection();
        unidadesSpinner.getValueFactory().setValue(0.0);
    }

    /*
    Dejo esto de recuerdo para intentarlo en otro momento pero hoy (02/05/2026) después de más de una hora no he
    conseguido aplicar el sombreado como lo hicimos en clase. Antes de eso me he estado peleando donde puse
    grupoZonas.selectedToggleProperty().addListener... Aprovechando la capacidad del togglegroup de descartarse mutuamente
    pensé en dejar la sombra unicamente para el botón seleccionado pero me ha sido imposible.

    Ahora lo he intentado como hicimos en clase, dejando el sombreado para setOnMouseEntered y Exited, pero el problema
    es que los botones toggle parecen tener algún tipo de transparecia de serie y una vez pasas encima de ellos, oscurecen.
    Ni desactivando el efecto vuelven a su punto anterior. Como el tiempo apremía me conformo con meter un label que recoja
    el nombre del botón seleccionado para dar feedback visual.

    class ManejoRaton implements EventHandler<MouseEvent>{

        @Override
        public void handle(MouseEvent mouseEvent) {
            if (mouseEvent.getEventType() == MouseEvent.MOUSE_ENTERED){
                ((ToggleButton) mouseEvent.getSource()).setEffect(dropShadow);
            } else if (mouseEvent.getEventType() == MouseEvent.MOUSE_EXITED){
                ((ToggleButton) mouseEvent.getSource()).setEffect(null);
            }
        }
        */
    }


