package com.example.proyectotfgjavierlahoz.modelos;

public class Fichaje {
    private int fichCod;
    private String fichEmp;
    private String fichDia;
    private String fichInicio;
    private String fichFin;
    private String fichFinDia;
    private int fichActivo;

    public int getFichCod() {
        return fichCod;
    }

    public String getFichDia() {
        return fichDia;
    }

    public String getFichEmp() {
        return fichEmp;
    }

    public String getFichFin() {
        return fichFin;
    }

    public String getFichInicio() {
        return fichInicio;
    }

    public void setFichCod(int fichCod) {
        this.fichCod = fichCod;
    }

    public void setFichDia(String fichDia) {
        this.fichDia = fichDia;
    }

    public void setFichEmp(String fichEmp) {
        this.fichEmp = fichEmp;
    }

    public void setFichFin(String fichFin) {
        this.fichFin = fichFin;
    }

    public void setFichInicio(String fichInicio) {
        this.fichInicio = fichInicio;
    }

    public int getFichActivo() {
        return fichActivo;
    }

    public String getFichFinDia() {
        return fichFinDia;
    }

    public void setFichActivo(int fichActivo) {
        this.fichActivo = fichActivo;
    }

    public void setFichFinDia(String fichFinDia) {
        this.fichFinDia = fichFinDia;
    }
}
