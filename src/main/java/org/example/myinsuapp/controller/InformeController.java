package org.example.myinsuapp.controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import org.example.myinsuapp.model.dto.IncidenciaDTO;
import org.example.myinsuapp.model.dto.InformeMedicoDTO;
import org.example.myinsuapp.model.dto.ResumenInyeccionesDTO;
import org.example.myinsuapp.model.dto.ZonaUsoDTO;
import org.example.myinsuapp.service.InformeService;
import org.example.myinsuapp.util.XmlExportUtil;

import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class InformeController implements Initializable {

    private InformeService informeService;

    private XmlExportUtil xmlExportUtil;

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
        xmlExportUtil = new XmlExportUtil();
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
                InformeMedicoDTO informeMedicoDTO = informeService.generarInforme(dias);

                ResumenInyeccionesDTO resumen = informeMedicoDTO.getResumenInyeccionesDTO();

                lblDosisTotal.setText(resumen.getDosisTotal()+" uds");
                lblPromedioUdDiario.setText(resumen.getPromedioInsulinaDia()+ "uds/día");
                lblPicoMaximo.setText(resumen.getDosisMaxInsulina()+ " uds");
                lblTotalInyecciones.setText(resumen.getTotalInyecciones()+" inyecciones");
                lblTotalIncidencias.setText(resumen.getTotalIncidencias()+" incidencias");
                lblInyeccionesDia.setText(resumen.getInyeccionesPorDia()+" inyecciones/día");
                lblZonaMasUsada.setText(resumen.getZonaMasUsada());
                lblZonaMasIncidencias.setText(resumen.getZonaMasProblematica());
                lblTasaIncidencias.setText(resumen.getPorcentajeIncidencia()+" %");

                setGraficoQuesitosDos(informeMedicoDTO);
                setGraficoBarrasDos(informeMedicoDTO);



            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        btnExportarXML.setOnAction(event->{
            int dias = rangoDiasCombo.getValue();

            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar exportación");
            confirmacion.setHeaderText("Generación de informe XML");
            confirmacion.setContentText("¿Deseas exportar los datos de los últimos " + dias + " días?");

            Optional<ButtonType> respuesta = confirmacion.showAndWait();
            if (respuesta.isPresent() && respuesta.get() == ButtonType.OK){
                try {
                    InformeMedicoDTO informeMedicoDTO = informeService.generarInforme(dias);
                    xmlExportUtil.exportarXmlInforme(informeMedicoDTO);

                    // Alerta de éxito con el enlace que montamos antes
                    Alert alertExito = new Alert(Alert.AlertType.INFORMATION);
                    alertExito.setTitle("Informe exportado");
                    alertExito.setHeaderText("Informe exportado con éxito");
                    alertExito.showAndWait();


                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

            }

        });
    }

    private void setGraficoQuesitosDos(InformeMedicoDTO informeMedicoDTO){
        graficoZonas.getData().clear();

        List<ZonaUsoDTO> zonaUsoDTOList = informeMedicoDTO.getListaUsoZonas();
        zonaUsoDTOList.stream().filter(zona -> zona.getUsoTotal()>0)
                .forEach(zona ->{
                    listaQuesitos.add(new PieChart.Data(zona.getNombreZona(), zona.getUsoTotal()));
                });
        graficoZonas.setData(listaQuesitos);

    }

    /*
    Me he querido forzar al uso de lamdas y esta web para me ha salvado para entender el flatMap
    https://www.arquitecturajava.com/java-8-flatmap/
     */
    public void setGraficoBarrasDos(InformeMedicoDTO informeMedicoDTO){
        barraIncidencias.getData().clear();
        barraIncidencias.setAnimated(false);
        List<ZonaUsoDTO> zonaUsoDTOList = informeMedicoDTO.getListaUsoZonas();

        Set<String> incidencias = zonaUsoDTOList.stream()
                .map(zona -> zona.getListaIncidenciasZona())
                .flatMap(listaIncidencias -> listaIncidencias.stream())
                .map(IncidenciaDTO::getTipoIncidencia).collect(Collectors.toSet());

        incidencias.forEach(incidencia -> {
            XYChart.Series series = new XYChart.Series();
            series.setName(incidencia);

            zonaUsoDTOList.forEach(zona ->{
                int cantidad = zona.getListaIncidenciasZona().stream().filter(nombreIncidencia ->
                        nombreIncidencia.getTipoIncidencia().equalsIgnoreCase(series.getName()))
                        .map(IncidenciaDTO::getCantidadTotal)
                        .findFirst().orElse(0);
                series.getData().add(new XYChart.Data<>(zona.getNombreZona(), cantidad));

            });
            barraIncidencias.getData().add(series);
        });
    }

     /*

    private void setGraficoBarrasNULL (InformeMedico informeMedico){
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

   */
}
