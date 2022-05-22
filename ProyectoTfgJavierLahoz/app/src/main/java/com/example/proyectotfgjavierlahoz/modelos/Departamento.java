package com.example.proyectotfgjavierlahoz.modelos;

import android.graphics.Bitmap;

public class Departamento {

    private String codigo;
    private String nombre;
    private String encargado;

    private Bitmap imagen;

    public String getCodigo() {
        return codigo;
    }

    public String getEncargado() {
        return encargado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setEncargado(String encargado) {
        this.encargado = encargado;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }
}
