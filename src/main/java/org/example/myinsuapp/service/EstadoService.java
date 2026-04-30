package org.example.myinsuapp.service;

import org.example.myinsuapp.dao.PlumaInsulinaDAO;
import org.example.myinsuapp.dao.UsuarioDAO;
import org.example.myinsuapp.dao.ZonaDAO;
import org.example.myinsuapp.exceptions.DataBaseException;
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


    // El metodo de iniciar pluma llama a DAO que lanza una transacción que se encarga del UPDATE de la anterior e insertar la nueva en la bd.
    public void iniciarPluma() throws DataBaseException {
        PlumaInsulinaDAO plumaInsulinaDAO = new PlumaInsulinaDAO();
        PlumaInsulina nuevaPluma = new PlumaInsulina(PlumaInsulina.DEPOSITO_ESTANDAR, this.usuario, true, LocalDate.now());
        plumaInsulinaDAO.registrarPlumaNueva(nuevaPluma);

        //No sé si tiene mucho sentido pero me parecía peligroso machacar la plumaActiva antes de que el DAO termine su función, por eso instancio una nueva e igualo.
        plumaActiva = nuevaPluma;

    }

    public Zona getZonaByID(int id){
        if (mapaZonas.isEmpty()){
            throw new RuntimeException(); //todo pensar si personalizar
        }
        return mapaZonas.get(id);
    }

    public Map<Integer, Zona> getMapaZonas() {
        return mapaZonas;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public PlumaInsulina getPlumaActiva() {
        return plumaActiva;
    }
}
