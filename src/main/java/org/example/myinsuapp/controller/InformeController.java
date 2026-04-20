package org.example.myinsuapp.controller;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

public class InformeController {
    @FXML
    private StackedBarChart<?, ?> barraIncidencias;

    @FXML
    private Button btnCargarDatos;

    @FXML
    private Button btnExportarXML;

    @FXML
    private DatePicker fechaFinPicker;

    @FXML
    private DatePicker fechaInicioPicker;

    @FXML
    private PieChart graficoZonas;

    @FXML
    private Label lblDosisTotal;

    @FXML
    private Label lblInyeccionesDia;

    @FXML
    private Label lblPicoMaximo;

    @FXML
    private Label lblPromedioUdDiario;

    @FXML
    private Label lblTasaIncidencias;

    @FXML
    private Label lblZonaMasIncidencias;

    @FXML
    private Label lblZonaMasUsada;
}
