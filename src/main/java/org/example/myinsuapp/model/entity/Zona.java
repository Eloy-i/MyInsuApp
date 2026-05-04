package org.example.myinsuapp.model.entity;

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


}
