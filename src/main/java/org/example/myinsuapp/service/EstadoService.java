package org.example.myinsuapp.service;

import org.example.myinsuapp.dao.PlumaInsulinaDAO;
import org.example.myinsuapp.dao.UsuarioDAO;
import org.example.myinsuapp.dao.ZonaDAO;
import org.example.myinsuapp.exceptions.DataBaseException;
import org.example.myinsuapp.exceptions.MapaZonasEmptyException;
import org.example.myinsuapp.model.PlumaInsulina;
import org.example.myinsuapp.model.Usuario;
import org.example.myinsuapp.model.Zona;

import java.time.LocalDate;
import java.util.Map;
/*
EstadoService la he planteado como una clase que gestiona el estado global de la app con un patrón Singleton, viendo como
se instaciaba el DBConnection me parecía lógico hacerlo así con los elementos que he metido aquí.
En memoria hay una única instancia para centralizar el acceso a datos como Usuario activo, el catálogo de Zonas y la Pluma actual.
 */

public class EstadoService {

    private Map<Integer, Zona> mapaZonas;
    private Usuario usuario;
    private PlumaInsulina plumaActiva;

    private static EstadoService instancia;

    // Buscando como aplicar patrones Singleton he leído que es recomendable marcar un constructor vacio en privado para evitar un new en otra parte del código
    private EstadoService(){}

    public static EstadoService getInstance(){
        if (instancia == null){
            instancia = new EstadoService();
        }
        return instancia;
    }

    //Este méto-do será cargado al inicio y se trae directamente desde la BD tanto a Usuario, como las zonas del cuerpo y la pluma que tenga el usuario activa
    public void cargaInicial() throws DataBaseException {

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
        if (mapaZonas == null || mapaZonas.isEmpty()){
            throw new MapaZonasEmptyException("El mapa de zonas no está inicializado o está vacío.");
        }
        return mapaZonas.get(id);
    }

    // getMapaZonas dudo que lo use teniendo la herramienta tan potente de poder usar la key del map, realmente me interesa coger una concreta para el registro
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
