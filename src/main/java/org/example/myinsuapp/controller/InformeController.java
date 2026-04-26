package org.example.myinsuapp.controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import org.example.myinsuapp.model.InformeMedico;
import org.example.myinsuapp.service.InformeService;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class InformeController implements Initializable {

    private InformeService informeService;

    ObservableList<PieChart.Data> listaQuesitos;

    @FXML
    private StackedBarChart<String, Number> barraIncidencias;

    @FXML
    private Button btnCargarDatos;

    @FXML
    private Button btnExportarXML;

    @FXML
    private ComboBox<Integer> rangoDiasCombo;

    @FXML
    private PieChart graficoZonas;

    @FXML
    private Label lblDosisTotal;

    @FXML
    private Label lblInyeccionesDia;

    @FXML
    private Label lblTotalInyecciones;

    @FXML
    private Label lblTotalIncidencias;

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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        instances();
        initGUI();
        actions();
    }

    private void instances() {
        informeService = new InformeService();
        listaQuesitos = FXCollections.observableArrayList();
    }

    private void initGUI() {
        rangoDiasCombo.setItems(FXCollections.observableArrayList(1, 7, 15, 30));
        rangoDiasCombo.getSelectionModel().select(0);

    }

    private void actions() {
        btnCargarDatos.setOnAction(event->{

            int dias = rangoDiasCombo.getValue();
            try {
                InformeMedico informeMedico = informeService.informeCompleto(dias);
                lblDosisTotal.setText(informeMedico.getDosisTotal()+" uds");
                lblPromedioUdDiario.setText(String.format("%.1f uds/día", informeMedico.getPromedioInsulinaDia()));
                lblPicoMaximo.setText(informeMedico.getPicoMaxInsulina()+ " uds");
                lblTotalInyecciones.setText(informeMedico.getTotalInyecciones()+" inyecciones");
                lblTotalIncidencias.setText(informeMedico.getTotalIncidencias()+" incidencias");
                lblInyeccionesDia.setText(String.format("%.1f inyecciones/día", informeMedico.getInyeccionesPorDia()));
                lblZonaMasUsada.setText(informeMedico.getZonaMasUsada());
                lblZonaMasIncidencias.setText(informeMedico.getZonaMasProblematica());
                lblTasaIncidencias.setText(String.format("%.1f%%", informeMedico.getPorcentajeIncidencia()));
                setGraficoQuesitos(informeMedico);
                setGraficoBarras(informeMedico);



            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        });
    }

    private void setGraficoQuesitos (InformeMedico informeMedico){
        graficoZonas.getData().clear();

        Map<String, Integer> usoZonas = informeMedico.getUsoZonas();

        for (Map.Entry<String, Integer> entry : usoZonas.entrySet()) {
            if (entry.getValue() > 0){
                listaQuesitos.add(new PieChart.Data(entry.getKey(), entry.getValue()));
            }
        }
        graficoZonas.setData(listaQuesitos);
    }

    private void setGraficoBarras (InformeMedico informeMedico){
        //TODO he sido derrotado por el PierChart, dos horas y no he sido capaz de pintarlo como quería... Borrar esto ya qu elo tengo en la tableview o hacer otro grafico de quesitos
    }
}
