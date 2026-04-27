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
import java.util.*;

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

    /*
    3 horas mirando videos sobre como llenar un StackedBarChart y recorrer un map anidado para esto... Recordatorio, abandonar programación e ir a criar ovejas.

    Funciona a medias... Los datos y la gráfica se pintan bien pero el String Zona sale apiñado en la izquierda en vez de salir bajo la barra que le corresponde
     */

    //Todo repasar porqué el String zona sale mezclado a la izquierda y hacer que se pinte en su lugar correspondiente.
    // Todo 2 -> Si no se logra pensar si entregar así con el bug visual o eliminar del todo.
    // TODO 3 -> Lo deje o no, recordar anotar comentarios de flujo de trabajo porque en dos semanas no voy a recordar como c he hecho esto.

    private void setGraficoBarras (InformeMedico informeMedico){
        barraIncidencias.getData().clear();
        barraIncidencias.setAnimated(false);
        Map<String, Map<String, Integer>> incidenciasTotalesPorZona = informeMedico.getHistorialIncidenciasPorZona();
        Set<String> incidencias = new HashSet<>();


        for (Map<String, Integer> value : incidenciasTotalesPorZona.values()) {
            for (String s : value.keySet()) {
                incidencias.add(s);
            }
        }

        for (String incidencia : incidencias) {
            XYChart.Series series = new XYChart.Series<>();
            series.setName(incidencia);
            for (Map.Entry<String, Map<String, Integer>> stringMapEntry : incidenciasTotalesPorZona.entrySet()) {
                String zona = stringMapEntry.getKey();
                int cantidad = stringMapEntry.getValue().getOrDefault(incidencia, 0);
                series.getData().add(new XYChart.Data<>(zona, cantidad));
            }
            barraIncidencias.getData().add(series);

        }
    }
}
