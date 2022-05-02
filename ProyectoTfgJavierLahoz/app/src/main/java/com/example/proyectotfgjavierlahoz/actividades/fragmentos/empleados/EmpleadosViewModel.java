package com.example.proyectotfgjavierlahoz.actividades.fragmentos.empleados;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EmpleadosViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public EmpleadosViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}