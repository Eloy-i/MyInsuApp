package org.example.myinsuapp.model;

public class Zona {
    private int idZona;
    private String zonaCuerpo;

    public Zona(int idZona, String zonaCuerpo){
        this.idZona = idZona;
        this.zonaCuerpo = zonaCuerpo;
    }

    public int getIdZona() {
        return idZona;
    }

    public String getZonaCuerpo() {
        return zonaCuerpo;
    }

    public void setIdZona(int idZona) {
        this.idZona = idZona;
    }

    public void setZonaCuerpo(String zonaCuerpo) {
        this.zonaCuerpo = zonaCuerpo;
    }
}
