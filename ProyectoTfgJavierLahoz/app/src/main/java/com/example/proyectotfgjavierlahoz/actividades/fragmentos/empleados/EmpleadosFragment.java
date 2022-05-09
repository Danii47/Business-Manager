package com.example.proyectotfgjavierlahoz.actividades.fragmentos.empleados;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.proyectotfgjavierlahoz.R;
import com.example.proyectotfgjavierlahoz.actividades.registro.LoginActivity;
import com.example.proyectotfgjavierlahoz.actividades.usuario.DataActivity;
import com.example.proyectotfgjavierlahoz.actividades.usuario.UserActivity;
import com.example.proyectotfgjavierlahoz.adaptadores.UserListAdapter;
import com.example.proyectotfgjavierlahoz.databinding.FragmentEmpleadosBinding;
import com.example.proyectotfgjavierlahoz.modelos.Empleado;
import com.example.proyectotfgjavierlahoz.sql.DatabaseHelper;
import com.example.proyectotfgjavierlahoz.validadores.ValidacionSesion;

import java.util.ArrayList;
import java.util.List;


public class EmpleadosFragment extends Fragment implements View.OnClickListener, SearchView.OnQueryTextListener, View.OnLongClickListener {

    private FragmentEmpleadosBinding binding;

    private UserListAdapter adapter;
    private List<Empleado> elementos;

    private DatabaseHelper bd;
    private ValidacionSesion validacion;

    private String dni;

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
        validacion = new ValidacionSesion(getActivity());

        binding.rvListausuarios.setLayoutManager(manager);

        elementos = bd.obtenerUsuarios();
        adapter = new UserListAdapter(elementos);
        adapter.setOnClickListener(this);
        adapter.setOnLongClickListener(this);
        binding.fabAAdirDNI.setOnClickListener(this);
        binding.rvListausuarios.setAdapter(adapter);
        binding.svEmpleado.setOnQueryTextListener(this);

        datosAdministrador();
    }

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.fabAñadirDNI){
            AlertDialog.Builder dialogoDNI = new AlertDialog.Builder(getActivity());
            dialogoDNI.setTitle("Añadir DNI");

            final EditText inputDni = new EditText(getActivity());
            inputDni.setInputType(InputType.TYPE_CLASS_TEXT);
            dialogoDNI.setView(inputDni);

            dialogoDNI.setCancelable(false);
            dialogoDNI.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if(!validacion.comprobarDni(inputDni, getString(R.string.errorDni))){
                        return;
                    }
                    if(!bd.comprobarDni(inputDni.getText().toString())){
                        bd.añadirDNI(inputDni.getText().toString());
                        Toast.makeText(getActivity(), getString(R.string.insertar_dni), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.errorRegistro), Toast.LENGTH_LONG).show();
                    }
                }
            });

            dialogoDNI.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            }).create().show();
        } else {
            dni = elementos.get(binding.rvListausuarios.getChildAdapterPosition(view)).getDni();

            Intent datosUsuario = new Intent(getContext(), UserActivity.class);
            datosUsuario.putExtra("dni", dni);
            startActivity(datosUsuario);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String s) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {

        ArrayList<Empleado> lista = new ArrayList<>();
        elementos = bd.obtenerUsuarios();

        for(Empleado empleado : elementos){
            if(empleado.getNombre().toLowerCase().contains(s.toLowerCase()) ||
               empleado.getApellidos().toLowerCase().contains(s.toLowerCase()) ||
               empleado.getCorreo().toLowerCase().contains(s.toLowerCase())){
                lista.add(empleado);
            }
        }
        elementos = lista;
        adapter.filtrar(lista);
        return false;
    }

    private void datosAdministrador() {
        if(LoginActivity.administrador == true){
            binding.fabAAdirDNI.show();
        }
    }

    @Override
    public boolean onLongClick(View view) {
        if(LoginActivity.administrador == true){
            dni = elementos.get(binding.rvListausuarios.getChildAdapterPosition(view)).getDni();
            Intent editUserAdmin = new Intent(getActivity(), DataActivity.class);
            editUserAdmin.putExtra("dni", dni);
            startActivity(editUserAdmin);
        }
        return false;
    }
}