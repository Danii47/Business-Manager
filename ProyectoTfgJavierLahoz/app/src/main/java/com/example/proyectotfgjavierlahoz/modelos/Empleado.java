package com.example.proyectotfgjavierlahoz.modelos;

import android.graphics.Bitmap;

public class Empleado {
    private String dni;
    private String nombre;
    private String apellidos;
    private String contraseña;
    private String correo;
    private String administrador;
    private String direccion;
    private String movil;
    private String puesto;
    private String departamento;

    private Bitmap imagen;

    public String getDni(){
        return dni;
    }

    public void setDni(String dni){
        this.dni = dni;
    }

    public String getNombre(){
        return nombre;
    }

    public void setNombre(String nombre){
        this.nombre = nombre;
    }

    public String getApellidos(){
        return apellidos;
    }

    public void setApellidos(String apellidos){
        this.apellidos = apellidos;
    }

    public String getContraseña(){
        return contraseña;
    }

    public void setContraseña(String contraseña){
        this.contraseña = contraseña;
    }

    public String getCorreo(){
        return correo;
    }

    public void setCorreo(String correo){
        this.correo = correo;
    }

    public String getAdministrador(){
        return administrador;
    }

    public void setAdministrador(String administrador){
        this.administrador = administrador;
    }

    public String getDireccion(){
        return direccion;
    }

    public void setDireccion(String direccion){
        this.direccion = direccion;
    }

    public String getMovil(){
        return movil;
    }

    public void setMovil(String movil){
        this.movil = movil;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getDepartamento() {
        return departamento;
    }

    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

    public Bitmap getImagen(){
        return imagen;
    }

    public void setImagen(Bitmap imagen){
        this.imagen = imagen;
    }
}
