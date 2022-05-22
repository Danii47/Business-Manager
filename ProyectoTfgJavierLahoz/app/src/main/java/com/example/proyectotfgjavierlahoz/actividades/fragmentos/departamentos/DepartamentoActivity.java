package com.example.proyectotfgjavierlahoz.actividades.fragmentos.departamentos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.proyectotfgjavierlahoz.R;
import com.example.proyectotfgjavierlahoz.actividades.registro.LoginActivity;
import com.example.proyectotfgjavierlahoz.actividades.usuario.DataDepActivity;
import com.example.proyectotfgjavierlahoz.modelos.Departamento;
import com.example.proyectotfgjavierlahoz.sql.DatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class DepartamentoActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imgEmpleado;
    private TextView txvNombre;

    private Bundle info;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private Fragment depEmpFragment;
    private Fragment depInfoFragment;

    private FloatingActionButton fabEdit;

    private DatabaseHelper bd;
    private Departamento departamento;

    Bundle datos;
    private String codigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_departamento);

        inicializarVistas();
        inicializarObjetos();
        inicializarEscuchadores();

        establecerDatos();
        establecerDatosAdmin();
        establecerTabs();



    }

    private void inicializarVistas(){
        imgEmpleado = (ImageView) findViewById(R.id.imgEmpleado);
        txvNombre = (TextView) findViewById(R.id.txvNombre);
        fabEdit = (FloatingActionButton) findViewById(R.id.fabEditar);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.vpInfos);

    }

    private void inicializarObjetos(){
        bd = new DatabaseHelper(this);
        departamento = new Departamento();

        depEmpFragment = new DepEmpFragment();
        depInfoFragment = new DepInfoFragment();

        datos = getIntent().getExtras();
        info = new Bundle();

    }

    private void inicializarEscuchadores(){
        fabEdit.setOnClickListener(this);
    }

    private void establecerDatos(){
        codigo = datos.getString("codigo");

        info.putString("codigo", codigo);
        depEmpFragment.setArguments(info);
        depInfoFragment.setArguments(info);

        departamento = bd.datosDepartamento(codigo);

        txvNombre.setText(departamento.getNombre());

        if(bd.obtenerImagenDep(codigo) != null){
            imgEmpleado.setImageBitmap(bd.obtenerImagenDep(codigo));
        } else {
            imgEmpleado.setImageResource(R.drawable.ic_outline_home_work_24);
        }

    }

    private void establecerTabs(){

        tabLayout.setupWithViewPager(viewPager);

        DepartamentoActivity.ViewPagerAdapter viewPagerAdapter = new DepartamentoActivity.ViewPagerAdapter(getSupportFragmentManager(), 0);
        viewPagerAdapter.añadirFragmento(depInfoFragment, "Informacion");
        viewPagerAdapter.añadirFragmento(depEmpFragment, "Empleados");
        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    public void onClick(View view) {
        Intent editDep = new Intent(DepartamentoActivity.this, DataDepActivity.class);
        editDep.putExtra("codigo", codigo);
        startActivity(editDep);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragmentos = new ArrayList<>();
        private List<String> titulosFragmentos = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public void añadirFragmento(Fragment fragmento, String titulo){
            fragmentos.add(fragmento);
            titulosFragmentos.add(titulo);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentos.get(position);
        }

        @Override
        public int getCount() {
            return fragmentos.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titulosFragmentos.get(position);
        }
    }

    private void establecerDatosAdmin() {
        if(LoginActivity.administrador == true){
            fabEdit.show();
        }
    }
}
