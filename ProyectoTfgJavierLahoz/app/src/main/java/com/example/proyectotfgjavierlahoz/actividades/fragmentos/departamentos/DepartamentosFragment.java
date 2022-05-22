package com.example.proyectotfgjavierlahoz.actividades.fragmentos.departamentos;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.proyectotfgjavierlahoz.R;
import com.example.proyectotfgjavierlahoz.actividades.registro.LoginActivity;
import com.example.proyectotfgjavierlahoz.actividades.usuario.DataDepActivity;
import com.example.proyectotfgjavierlahoz.adaptadores.DepListAdapter;
import com.example.proyectotfgjavierlahoz.databinding.FragmentDepartamentosBinding;
import com.example.proyectotfgjavierlahoz.modelos.Departamento;
import com.example.proyectotfgjavierlahoz.modelos.Empleado;
import com.example.proyectotfgjavierlahoz.sql.DatabaseHelper;
import com.example.proyectotfgjavierlahoz.validadores.ValidacionEntradas;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class DepartamentosFragment extends Fragment implements View.OnLongClickListener, View.OnClickListener, SearchView.OnQueryTextListener {

    private FragmentDepartamentosBinding binding;

    private DepListAdapter adapter;
    private List<Departamento> elementos;

    private List<Empleado> empleados;
    private List<String> nombreEmp;

    private DatabaseHelper bd;

    private ValidacionEntradas validacion;

    private Dialog dialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DepartamentosViewModel slideshowViewModel =
                new ViewModelProvider(this).get(DepartamentosViewModel.class);

        binding = FragmentDepartamentosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        inicializarObjetos();
        inicializarEscuchadores();
        establecerDatosAdmin();


        return root;
    }

    private void inicializarObjetos(){
        validacion = new ValidacionEntradas(getContext());

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        bd = new DatabaseHelper(getContext());
        binding.rvListaDepartamentos.setLayoutManager(manager);

        elementos = bd.obtenerDepartamentos();
        adapter = new DepListAdapter(elementos);

    }

    private void inicializarEscuchadores(){
        adapter.setOnClickListener(this);
        adapter.setOnLongClickListener(this);
        binding.fabAAdirDep.setOnClickListener(this);
        binding.rvListaDepartamentos.setAdapter(adapter);
        binding.svDepartamentos.setOnQueryTextListener(this);
    }

    private void establecerDatosAdmin() {
        if(LoginActivity.administrador == true){
            binding.fabAAdirDep.show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public boolean onLongClick(View view) {
        if (LoginActivity.administrador == true){
            String codigo = elementos.get(binding.rvListaDepartamentos.getChildAdapterPosition(view)).getCodigo();

            Intent editDep = new Intent(getContext(), DataDepActivity.class);
            editDep.putExtra("codigo", codigo);
            startActivity(editDep);
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.fabAñadirDep){

            dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.custom_dialog_dep);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            dialog.setCancelable(false);

            EditText edtCodigo = dialog.findViewById(R.id.edtCodigo);
            EditText edtNombre = dialog.findViewById(R.id.edtNombre);
            Spinner spEncargado = dialog.findViewById(R.id.spEncargado);

            empleados = bd.obtenerUsuarios();
            nombreEmp = new ArrayList<>();

            for(Empleado emp : empleados){
                nombreEmp.add(emp.getNombre() + " " + emp.getApellidos());
            }

            spEncargado.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.spinner_list, nombreEmp));


            Button btnAceptar = dialog.findViewById(R.id.btnAceptar);
            Button btnCancelar = dialog.findViewById(R.id.btnCancelar);

            btnAceptar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!validacion.campoVacio(edtCodigo, getString(R.string.dep_codigo_error))){
                        return;
                    }
                    if(!bd.comprobarDepartamento(edtCodigo.getText().toString())){
                        Departamento dep = new Departamento();

                        dep.setNombre(edtNombre.getText().toString());
                        dep.setCodigo(edtCodigo.getText().toString());
                        dep.setEncargado(spEncargado.getSelectedItem().toString());
                        bd.añadirDepartamento(dep);
                        elementos = bd.obtenerDepartamentos();
                        adapter = new DepListAdapter(elementos);
                        binding.rvListaDepartamentos.setAdapter(adapter);
                        Toast.makeText(getContext(),getText(R.string.insertar_dep),Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(),getText(R.string.insertar_dep_error),Toast.LENGTH_LONG).show();
                    }
                }
            });

            btnCancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.show();

        } else {
            String codigo = elementos.get(binding.rvListaDepartamentos.getChildAdapterPosition(view)).getCodigo();

            Intent depInfo = new Intent(getContext(), DepartamentoActivity.class);
            depInfo.putExtra("codigo", codigo);
            startActivity(depInfo);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {

        ArrayList<Departamento> lista = new ArrayList<>();
        elementos = bd.obtenerDepartamentos();

        for(Departamento departamento : elementos){
            if(departamento.getNombre().toLowerCase().contains(s.toLowerCase()) ||
                departamento.getEncargado().toLowerCase().contains(s.toLowerCase())){
                lista.add(departamento);
            }
        }
        elementos = lista;
        adapter.filtrar(lista);
        return false;
    }

}