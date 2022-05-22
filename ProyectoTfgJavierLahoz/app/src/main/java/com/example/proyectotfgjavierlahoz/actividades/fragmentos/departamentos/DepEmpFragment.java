package com.example.proyectotfgjavierlahoz.actividades.fragmentos.departamentos;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyectotfgjavierlahoz.R;
import com.example.proyectotfgjavierlahoz.actividades.fragmentos.empleados.EmpleadosViewModel;
import com.example.proyectotfgjavierlahoz.adaptadores.UserListAdapter;
import com.example.proyectotfgjavierlahoz.databinding.FragmentDepEmpBinding;
import com.example.proyectotfgjavierlahoz.databinding.FragmentEmpleadosBinding;
import com.example.proyectotfgjavierlahoz.modelos.Empleado;
import com.example.proyectotfgjavierlahoz.sql.DatabaseHelper;
import com.example.proyectotfgjavierlahoz.validadores.ValidacionEntradas;

import java.util.List;


public class DepEmpFragment extends Fragment {

    private FragmentDepEmpBinding binding;

    private UserListAdapter adapter;
    private List<Empleado> elementos;

    private DatabaseHelper bd;

    private String codigo;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDepEmpBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        inicializarObjetos();
        establecerDatos();
        inicializarEscuchadores();

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    private void inicializarObjetos(){

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        bd = new DatabaseHelper(getContext());

        binding.rvListausuarios.setLayoutManager(manager);


    }

    private void establecerDatos(){
        codigo = getArguments().getString("codigo");
        elementos = bd.obtenerUsuarios(codigo);
        adapter = new UserListAdapter(elementos);
    }

    private void inicializarEscuchadores(){
        binding.rvListausuarios.setAdapter(adapter);
    }

}