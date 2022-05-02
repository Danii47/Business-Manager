package com.example.proyectotfgjavierlahoz.actividades.fragmentos.inicio;

import androidx.lifecycle.ViewModel;

public class InicioViewModel extends ViewModel {

    private String dni;

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getDni() {
        return dni;
    }
}