package org.example.myinsuapp.service;

import org.example.myinsuapp.dao.PlumaInsulinaDAO;
import org.example.myinsuapp.dao.UsuarioDAO;
import org.example.myinsuapp.dao.ZonaDAO;
import org.example.myinsuapp.model.PlumaInsulina;
import org.example.myinsuapp.model.Usuario;
import org.example.myinsuapp.model.Zona;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Map;

public class EstadoService {
    private Map<Integer, Zona> mapaZonas;
    private Usuario usuario;
    private PlumaInsulina plumaActiva;

    private static EstadoService instancia;

    public static EstadoService getInstance(){
        if (instancia == null){
            instancia = new EstadoService();
        }
        return instancia;
    }


    public void cargaInicial() throws SQLException {

        ZonaDAO zonaDAO = new ZonaDAO();
        this.mapaZonas = zonaDAO.getZonas();

        UsuarioDAO usuarioDAO = new UsuarioDAO();
        this.usuario = usuarioDAO.getUsuario();

        PlumaInsulinaDAO plumaInsulinaDAO = new PlumaInsulinaDAO();
        this.plumaActiva = plumaInsulinaDAO.getPlumaActiva(this.usuario);

    }

    public void iniciarPluma() throws SQLException {
        PlumaInsulinaDAO plumaInsulinaDAO = new PlumaInsulinaDAO();
        if (plumaActiva != null){
            plumaInsulinaDAO.desactivarPluma(plumaActiva.getIdPluma());
        }

        //TODO revisar esto que me parece redundante crear nueva y machacarla con get- seguro que hay algo mejor
        plumaActiva = new PlumaInsulina(300, this.usuario, true, LocalDate.now());
        plumaInsulinaDAO.insertPluma(plumaActiva);
        plumaActiva = plumaInsulinaDAO.getPlumaActiva(this.usuario);

    }

    public Zona getZonaByID(int id){
        if (mapaZonas.isEmpty()){
            throw new RuntimeException(); //todo pensar si personalizar
        }
        return mapaZonas.get(id);
    }



}
