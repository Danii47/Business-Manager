package com.example.proyectotfgjavierlahoz.actividades.fragmentos.inicio;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.proyectotfgjavierlahoz.actividades.registro.LoginActivity;
import com.example.proyectotfgjavierlahoz.databinding.FragmentPersonalBinding;
import com.example.proyectotfgjavierlahoz.modelos.Empleado;
import com.example.proyectotfgjavierlahoz.sql.DatabaseHelper;


public class PersonalFragment extends Fragment {

        private FragmentPersonalBinding binding;

        private TextView txvDni;
        private TextView txvEmail;
        private TextView txvMovil;
        private TextView txvDireccion;

        private Empleado empleado;
        private DatabaseHelper databaseHelper;

        private String dni;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPersonalBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        inicializarObjetos();
        establecerDatos();

        return root;

    }

    private void inicializarObjetos(){
        empleado = new Empleado();
        databaseHelper = new DatabaseHelper(getContext());
    }

    private void establecerDatos(){

        dni = LoginActivity.dni;
        Log.i("testbd", dni);
        empleado = databaseHelper.datosUsuario(dni);

        binding.txvDni2.setText(empleado.getDni());
        binding.txvEmail2.setText(empleado.getCorreo());
        binding.txvMovil2.setText(empleado.getMovil());
        binding.txvDireccion2.setText(empleado.getDireccion());
    }
}