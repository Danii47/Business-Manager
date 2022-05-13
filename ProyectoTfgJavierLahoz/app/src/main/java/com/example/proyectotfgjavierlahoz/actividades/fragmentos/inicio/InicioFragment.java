package com.example.proyectotfgjavierlahoz.actividades.fragmentos.inicio;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProvider;

import com.example.proyectotfgjavierlahoz.R;
import com.example.proyectotfgjavierlahoz.actividades.usuario.DataActivity;
import com.example.proyectotfgjavierlahoz.actividades.registro.LoginActivity;
import com.example.proyectotfgjavierlahoz.databinding.FragmentInicioBinding;
import com.example.proyectotfgjavierlahoz.modelos.Empleado;
import com.example.proyectotfgjavierlahoz.sql.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;


public class InicioFragment extends Fragment implements View.OnClickListener {

    private FragmentInicioBinding binding;

    private DatabaseHelper databaseHelper;
    private Empleado empleado;

    private String dni;

    private LaboralFragment laboralFragment;
    private PersonalFragment personalFragment;


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

        laboralFragment = new LaboralFragment();
        personalFragment = new PersonalFragment();

    }

    private void establecerDatosUsuario(){
        dni = LoginActivity.dni;

        empleado = databaseHelper.datosUsuario(dni);

        binding.txvNombre.setText(empleado.getNombre() + " " + empleado.getApellidos());

        Bitmap imagen = databaseHelper.obtenerImagen(dni);

        if(imagen != null){
            binding.imgEmpleado.setImageBitmap(imagen);
        } else {
            binding.imgEmpleado.setImageResource(R.drawable.user_logo);
        }

        establecerTabs();

    }

    private void establecerTabs(){

        binding.tabLayout.setupWithViewPager(binding.vpInfos);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), 0);
        viewPagerAdapter.añadirFragmento(personalFragment, "Info. Personal");
        viewPagerAdapter.añadirFragmento(laboralFragment, "Info. Laboral");
        binding.vpInfos.setAdapter(viewPagerAdapter);
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
}