package org.example.myinsuapp.controller;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
public class HistoricoController {
    @FXML
    private Button btnEliminar;

    @FXML
    private TableColumn<?, ?> fechaColumn;

    @FXML
    private TableColumn<?, ?> horaColumn;

    @FXML
    private RadioButton incidenciaButton;

    @FXML
    private TableColumn<?, ?> incidenciaColumn;

    @FXML
    private TableView<?> tableView;

    @FXML
    private TableColumn<?, ?> unidadesColumn;

    @FXML
    private TableColumn<?, ?> zonaColumn;
}
