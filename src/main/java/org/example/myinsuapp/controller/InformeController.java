package org.example.myinsuapp.controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import org.example.myinsuapp.exceptions.DataBaseException;
import org.example.myinsuapp.exceptions.DatosInsuficientesException;
import org.example.myinsuapp.exceptions.ExportException;
import org.example.myinsuapp.model.Usuario;
import org.example.myinsuapp.model.dto.IncidenciaDTO;
import org.example.myinsuapp.model.dto.InformeMedicoDTO;
import org.example.myinsuapp.model.dto.ResumenInyeccionesDTO;
import org.example.myinsuapp.model.dto.ZonaUsoDTO;
import org.example.myinsuapp.service.EstadoService;
import org.example.myinsuapp.service.InformeService;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class InformeController implements Initializable {

    private InformeService informeService;

    private InformeMedicoDTO informeMedicoDTO;

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
        Usuario usuario = EstadoService.getInstance().getUsuario();
        btnCargarDatos.setOnAction(event->{

            try {
                int dias = rangoDiasCombo.getValue();

                informeMedicoDTO = informeService.generarInforme(usuario, dias);

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

            } catch (DatosInsuficientesException e){
                ventanaInformeNoDisponible(e.getMessage());

            } catch (DataBaseException e){
                ventanaError(e.getMessage());
            }
        });

        btnExportarXML.setOnAction(event->{
           //Para avitar dos llamadas redundantes a la bd, este botón manda el informe solo si antes se ha pedido arriba.
            if (informeMedicoDTO != null) {

                Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
                confirmacion.setTitle("Confirmar exportación");
                confirmacion.setHeaderText("Generación de informe XML");
                confirmacion.setContentText("¿Deseas exportar los datos de los últimos " + rangoDiasCombo.getValue() + " días?");

                Optional<ButtonType> respuesta = confirmacion.showAndWait();
                if (respuesta.isPresent() && respuesta.get() == ButtonType.OK) {
                    try {

                        informeService.exportarInforme(informeMedicoDTO);
                        Alert alertExito = new Alert(Alert.AlertType.INFORMATION);
                        alertExito.setTitle("Informe exportado");
                        alertExito.setHeaderText("Informe exportado con éxito");
                        alertExito.showAndWait();



                    } catch (ExportException e) {
                        ventanaError(e.getMessage());
                    }

                }
            } else {
                ventanaInformeNoDisponible("Primero debes cargar los datos del informe pulsando el botón superior.");
            }

        });
    }

    /*
    Viendo que he repetido exactamente las mismas líneas me los saco a un metod.o privado
     */
    private void ventanaError(String mensaje){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void ventanaInformeNoDisponible(String mensaje){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informe no disponible");
        alert.setContentText(mensaje);

        alert.showAndWait();
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
