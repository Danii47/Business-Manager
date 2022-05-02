package com.example.proyectotfgjavierlahoz.actividades.fragmentos.inicio;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.proyectotfgjavierlahoz.R;
import com.example.proyectotfgjavierlahoz.actividades.usuario.DataActivity;
import com.example.proyectotfgjavierlahoz.actividades.registro.LoginActivity;
import com.example.proyectotfgjavierlahoz.databinding.FragmentInicioBinding;
import com.example.proyectotfgjavierlahoz.modelos.Empleado;
import com.example.proyectotfgjavierlahoz.sql.DatabaseHelper;


public class InicioFragment extends Fragment implements View.OnClickListener {

    private FragmentInicioBinding binding;

    private DatabaseHelper databaseHelper;
    private Empleado empleado;

    private String dni;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        InicioViewModel homeViewModel =
                new ViewModelProvider(this).get(InicioViewModel.class);

        binding = FragmentInicioBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        inicializarObjetos();
        establecerDatosUsuario();
        escuchadoresBotones();


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void escuchadoresBotones(){
        binding.fab.setOnClickListener(this);
    }

    private void inicializarObjetos(){
        databaseHelper = new DatabaseHelper(getContext());
        empleado = new Empleado();
    }

    private void establecerDatosUsuario(){
        dni = LoginActivity.dni;

        empleado = databaseHelper.datosUsuario(dni);

        binding.txvNombre.setText(empleado.getNombre() + " " + empleado.getApellidos());
        binding.txvDni2.setText(empleado.getDni());
        binding.txvEmail2.setText(empleado.getCorreo());
        binding.txvMovil2.setText(empleado.getMovil());
        binding.txvDireccion2.setText(empleado.getDireccion());

        Bitmap imagen = databaseHelper.obtenerImagen(dni);

        if(imagen != null){
            binding.imgEmpleado.setImageBitmap(imagen);
        }

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.fab:
                Intent i1 = new Intent(getContext(), DataActivity.class);
                i1.putExtra("dni", dni);
                startActivity(i1);
                break;
        }
    }
}