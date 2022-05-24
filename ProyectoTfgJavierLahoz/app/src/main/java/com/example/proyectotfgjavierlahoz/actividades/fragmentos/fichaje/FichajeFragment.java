package com.example.proyectotfgjavierlahoz.actividades.fragmentos.fichaje;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.proyectotfgjavierlahoz.R;
import com.example.proyectotfgjavierlahoz.actividades.fragmentos.inicio.InicioViewModel;
import com.example.proyectotfgjavierlahoz.actividades.fragmentos.inicio.LaboralFragment;
import com.example.proyectotfgjavierlahoz.actividades.fragmentos.inicio.PersonalFragment;
import com.example.proyectotfgjavierlahoz.actividades.usuario.DataActivity;
import com.example.proyectotfgjavierlahoz.actividades.registro.LoginActivity;
import com.example.proyectotfgjavierlahoz.adaptadores.FichListAdapter;
import com.example.proyectotfgjavierlahoz.databinding.FragmentFichajeBinding;
import com.example.proyectotfgjavierlahoz.databinding.FragmentInicioBinding;
import com.example.proyectotfgjavierlahoz.modelos.Empleado;
import com.example.proyectotfgjavierlahoz.modelos.Fichaje;
import com.example.proyectotfgjavierlahoz.sql.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;


public class FichajeFragment extends Fragment implements View.OnClickListener, View.OnLongClickListener {

    private FragmentFichajeBinding binding;
    private DatabaseHelper bd;

    private boolean activo = false;

    private Fichaje fichaje;
    private List<Fichaje> elementos;

    private FichListAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentFichajeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        inicializarObjetos();
        escuchadoresBotones();
        establecerBotones();

        return root;
    }

    private void establecerBotones(){
        List<Fichaje> fichajes = new ArrayList<>();

        fichajes = bd.obtenerFichajes(LoginActivity.dni);

        for(int i = 0; i<fichajes.size(); i++){
            Fichaje fichaje = fichajes.get(i);
            if(fichaje.getFichActivo() == 1){
                binding.fabIniciar.setVisibility(View.INVISIBLE);
                binding.fabParar.setVisibility(View.VISIBLE);
            }
        }
    }

    private void inicializarObjetos(){
        fichaje = new Fichaje();
        bd = new DatabaseHelper(getContext());

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        binding.rvFichajes.setLayoutManager(manager);

        elementos = bd.obtenerFichajes(LoginActivity.dni);
        adapter = new FichListAdapter(elementos);

    }

    private void escuchadoresBotones(){
        binding.fabIniciar.setOnClickListener(this);
        binding.fabParar.setOnClickListener(this);
        binding.rvFichajes.setAdapter(adapter);
        adapter.setOnLongClickListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fabIniciar:
                binding.fabIniciar.setVisibility(View.INVISIBLE);
                binding.fabParar.setVisibility(View.VISIBLE);

                fichaje.setFichEmp(LoginActivity.dni);
                fichaje.setFichDia(Fechas.obtenerFechaActual("GMT+2"));
                fichaje.setFichInicio(Fechas.obtenerHoraActual("GMT+2"));
                fichaje.setFichActivo(1);

                bd.insertarFichaje(fichaje);

                elementos = bd.obtenerFichajes(LoginActivity.dni);
                adapter = new FichListAdapter(elementos);
                binding.rvFichajes.setAdapter(adapter);

                break;
            case R.id.fabParar:
                binding.fabIniciar.setVisibility(View.VISIBLE);
                binding.fabParar.setVisibility(View.INVISIBLE);

                fichaje.setFichFin(Fechas.obtenerHoraActual("GMT+2"));
                fichaje.setFichFinDia(Fechas.obtenerFechaActual("GMT+2"));
                fichaje.setFichActivo(0);

                bd.actualizarFichaje(fichaje, LoginActivity.dni);

                elementos = bd.obtenerFichajes(LoginActivity.dni);
                adapter = new FichListAdapter(elementos);
                binding.rvFichajes.setAdapter(adapter);

                break;
        }
    }

    @Override
    public boolean onLongClick(View view) {

        int position = binding.rvFichajes.getChildAdapterPosition(view);
        int codigo = elementos.get(position).getFichCod();

        bd.eliminarFichaje(codigo);

        elementos.remove(position);
        adapter.notifyItemRemoved(position);

        return false;
    }
}