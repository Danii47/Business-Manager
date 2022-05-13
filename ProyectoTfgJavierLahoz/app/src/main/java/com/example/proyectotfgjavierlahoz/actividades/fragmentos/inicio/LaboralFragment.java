package com.example.proyectotfgjavierlahoz.actividades.fragmentos.inicio;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyectotfgjavierlahoz.R;
import com.example.proyectotfgjavierlahoz.actividades.registro.LoginActivity;
import com.example.proyectotfgjavierlahoz.databinding.FragmentLaboralBinding;
import com.example.proyectotfgjavierlahoz.databinding.FragmentPersonalBinding;


public class LaboralFragment extends Fragment {

    private FragmentLaboralBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentLaboralBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        establecerDatos();

        return root;
    }

    private void establecerDatos(){

        if(LoginActivity.administrador == true){
            binding.swcAdministrador2.setChecked(true);
        } else {
            binding.swcAdministrador2.setChecked(false);
        }

    }
}