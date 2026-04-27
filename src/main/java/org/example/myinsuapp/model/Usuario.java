package org.example.myinsuapp.model;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

import java.time.LocalDate;

public class Usuario {
    private int idUsuario;
    private String nombre;
    private String apellidos;
    private TipoDiabetes tipoDiabetes ;
    private LocalDate fechaNacimiento;

    public Usuario(){

    }

    public Usuario(int idUsuario, String nombre, String apellidos, TipoDiabetes tipoDiabetes, LocalDate fechaNacimiento) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.tipoDiabetes = tipoDiabetes;
        this.fechaNacimiento = fechaNacimiento;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public TipoDiabetes getTipoDiabetes() {
        return tipoDiabetes;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public void setTipoDiabetes(TipoDiabetes tipoDiabetes) {
        this.tipoDiabetes = tipoDiabetes;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
}
