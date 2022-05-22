package com.example.proyectotfgjavierlahoz.actividades.fragmentos.departamentos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.proyectotfgjavierlahoz.R;
import com.example.proyectotfgjavierlahoz.actividades.registro.LoginActivity;
import com.example.proyectotfgjavierlahoz.databinding.FragmentDepEmpBinding;
import com.example.proyectotfgjavierlahoz.databinding.FragmentDepInfoBinding;
import com.example.proyectotfgjavierlahoz.databinding.FragmentLaboralBinding;
import com.example.proyectotfgjavierlahoz.modelos.Departamento;
import com.example.proyectotfgjavierlahoz.modelos.Empleado;
import com.example.proyectotfgjavierlahoz.sql.DatabaseHelper;

import org.w3c.dom.Text;

public class DepInfoFragment extends Fragment {

    private FragmentDepInfoBinding binding;

    private String codigo;

    private Departamento departamento;
    private DatabaseHelper bd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentDepInfoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        inicializarObjetos();
        establecerDatos();

        return root;
    }

    private void inicializarObjetos(){
        departamento = new Departamento();
        bd = new DatabaseHelper(getContext());

    }

    private void establecerDatos(){

        codigo = getArguments().getString("codigo");

        departamento = bd.datosDepartamento(codigo);

        binding.txvCodigo2.setText(departamento.getCodigo());
        binding.txvEncargado2.setText(departamento.getEncargado());

    }
}