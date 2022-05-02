package com.example.proyectotfgjavierlahoz.actividades.fragmentos.departamentos;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DepartamentosViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public DepartamentosViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}