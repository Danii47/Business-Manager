package com.example.proyectotfgjavierlahoz.actividades.fragmentos.empleados;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.proyectotfgjavierlahoz.actividades.usuario.UserActivity;
import com.example.proyectotfgjavierlahoz.adaptadores.UserListAdapter;
import com.example.proyectotfgjavierlahoz.databinding.FragmentEmpleadosBinding;
import com.example.proyectotfgjavierlahoz.modelos.Empleado;
import com.example.proyectotfgjavierlahoz.sql.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;


public class EmpleadosFragment extends Fragment implements View.OnClickListener, SearchView.OnQueryTextListener {

    private FragmentEmpleadosBinding binding;

    private UserListAdapter adapter;
    private List<Empleado> elementos;
    private DatabaseHelper bd;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        EmpleadosViewModel galleryViewModel =
                new ViewModelProvider(this).get(EmpleadosViewModel.class);

        binding = FragmentEmpleadosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        inicializarObjetos();

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

        elementos = bd.obtenerUsuarios();
        adapter = new UserListAdapter(elementos);
        adapter.setOnClickListener(this);
        binding.rvListausuarios.setAdapter(adapter);
        binding.svEmpleado.setOnQueryTextListener(this);

    }

    @Override
    public void onClick(View view) {
        String dni = elementos.get(binding.rvListausuarios.getChildAdapterPosition(view)).getDni();
        Intent datosUsuario = new Intent(getContext(), UserActivity.class);
        datosUsuario.putExtra("dni", dni);
        startActivity(datosUsuario);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        ArrayList<Empleado> lista = new ArrayList<>();

        for(Empleado empleado : elementos){
            if(empleado.getNombre().toLowerCase().contains(s.toLowerCase()) ||
               empleado.getApellidos().toLowerCase().contains(s.toLowerCase()) ||
               empleado.getCorreo().toLowerCase().contains(s.toLowerCase())){
                lista.add(empleado);
            }
        }
        adapter.filtrar(lista);
        return false;
    }
}