package org.example.myinsuapp.controller;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.myinsuapp.exceptions.DataBaseException;
import org.example.myinsuapp.exceptions.ReglaInyeccionException;
import org.example.myinsuapp.model.Incidencia;
import org.example.myinsuapp.model.Inyeccion;
import org.example.myinsuapp.model.Usuario;
import org.example.myinsuapp.service.EstadoService;
import org.example.myinsuapp.service.InyeccionService;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.ResourceBundle;

public class HistoricoController implements Initializable {

    private ObservableList<Inyeccion> listaIyecciones;

    private InyeccionService inyeccionService;

    @FXML
    private Button btnVerHistorial;

    @FXML
    private RadioButton incidenciaCheck;

    @FXML
    private Button btnEliminar;

    @FXML
    private ComboBox<Integer> comboRango;

    @FXML
    private TableView<Inyeccion> tableView;

    @FXML
    private TableColumn<Inyeccion, LocalDate> fechaColumn;

    @FXML
    private TableColumn<Inyeccion, LocalTime> horaColumn;

    @FXML
    private TableColumn<Inyeccion, String> incidenciaColumn;

    @FXML
    private TableColumn<Inyeccion, Double> unidadesColumn;

    @FXML
    private TableColumn<Inyeccion, String> zonaColumn;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instances();
        initGUI();
        actions();
    }

    private void instances() {
        inyeccionService = new InyeccionService();
        listaIyecciones = FXCollections.observableArrayList();

    }

    private void initGUI() {
        comboRango.setItems(FXCollections.observableArrayList(1, 7, 15, 30));
        comboRango.getSelectionModel().select(0);

        tableView.setItems(listaIyecciones);
        fechaColumn.setCellValueFactory(data ->{
            LocalDate fecha = data.getValue().getHoraInyeccion().toLocalDate();
            return new SimpleObjectProperty<>(fecha);
        });
        horaColumn.setCellValueFactory(data-> {
            LocalTime hora = data.getValue().getHoraInyeccion().toLocalTime();
            return new SimpleObjectProperty<>(hora);
        });
        incidenciaColumn.setCellValueFactory(data->{
            Incidencia incidencia = data.getValue().getIncidencia();
            if (incidencia == null){
                return new SimpleStringProperty("");
            }
            String nombreIncidencia = incidencia.getTipoIncidencia().toString();
            return new SimpleObjectProperty<>(nombreIncidencia);
        });
        unidadesColumn.setCellValueFactory(new PropertyValueFactory<>("dosis"));
        zonaColumn.setCellValueFactory(data ->{
            String nombreZona = data.getValue().getZona().getZonaCuerpo();
            return new SimpleObjectProperty<>(nombreZona);
        });

    }

    private void actions() {
        Usuario usuario = EstadoService.getInstance().getUsuario();

        btnVerHistorial.setOnAction(event ->{
            listaIyecciones.clear();
            int dias = comboRango.getValue();
            try {
                if (incidenciaCheck.isSelected()){
                    listaIyecciones.addAll(inyeccionService.listaInyeccionesConIncidencia(dias, usuario.getIdUsuario()));
                }else {
                    listaIyecciones.addAll(inyeccionService.listaInyecciones(dias, usuario.getIdUsuario()));
                }
            } catch (DataBaseException e){
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Error de conexión");
                error.setContentText(e.getMessage());
                error.showAndWait();
            }

        });

        btnEliminar.setOnAction(event -> {
            Inyeccion inyeccion = tableView.getSelectionModel().getSelectedItem();
            if (inyeccion != null){
                Alert confirmar = new Alert(Alert.AlertType.CONFIRMATION);
                confirmar.setTitle("Confirmación de borrado");
                confirmar.setContentText("¿Deseas borrar la Inyección seleccionada?");
                Optional<ButtonType> respuesta = confirmar.showAndWait();
                if (respuesta.isPresent() && respuesta.get() == ButtonType.OK){
                    try {
                        inyeccionService.eliminarInyeccion(inyeccion);
                        listaIyecciones.remove(inyeccion);
                    } catch (ReglaInyeccionException e) {
                        Alert informacion = new Alert(Alert.AlertType.INFORMATION);
                        informacion.setTitle("Borrado no permitido");
                        informacion.setContentText(e.getMessage());
                        informacion.showAndWait();
                    } catch (DataBaseException e){
                        Alert error = new Alert(Alert.AlertType.ERROR);
                        error.setTitle("Error de conexión");
                        error.setContentText(e.getMessage());
                        error.showAndWait();
                    }
                }
            }
        });
    }
}
