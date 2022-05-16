package com.example.proyectotfgjavierlahoz.actividades.usuario;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.proyectotfgjavierlahoz.R;
import com.example.proyectotfgjavierlahoz.actividades.fragmentos.inicio.InicioFragment;
import com.example.proyectotfgjavierlahoz.actividades.fragmentos.inicio.LaboralFragment;
import com.example.proyectotfgjavierlahoz.actividades.fragmentos.inicio.PersonalFragment;
import com.example.proyectotfgjavierlahoz.actividades.registro.LoginActivity;
import com.example.proyectotfgjavierlahoz.modelos.Empleado;
import com.example.proyectotfgjavierlahoz.sql.DatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imgEmpleado;
    private TextView txvNombre;
    private TextView txvDni;
    private TextView txvEmail;
    private TextView txvNumero;
    private TextView txvDireccion;
    private FloatingActionButton btnCorreo;
    private Switch swcAdministrador;
    private FloatingActionButton btnLlamar;
    private Bundle info;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private Fragment laboralFragment;
    private Fragment personalFragment;

    private DatabaseHelper bd;
    private Empleado empleado;

    Bundle datos;
    private String dni;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        inicializarVistas();
        inicializarObjetos();
        inicializarEscuchadores();

        establecerDatos();
        establecerTabs();



    }

    private void inicializarVistas(){
        imgEmpleado = (ImageView) findViewById(R.id.imgEmpleado);
        txvNombre = (TextView) findViewById(R.id.txvNombre);
        btnLlamar = (FloatingActionButton) findViewById(R.id.llamar);
        btnCorreo = (FloatingActionButton) findViewById(R.id.correo);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.vpInfos);

    }

    private void inicializarObjetos(){
        bd = new DatabaseHelper(this);
        empleado = new Empleado();

        laboralFragment = new LaboralFragment();
        personalFragment = new PersonalFragment();

        datos = getIntent().getExtras();
        info = new Bundle();

    }

    private void inicializarEscuchadores(){
        btnLlamar.setOnClickListener(this);
        btnCorreo.setOnClickListener(this);
    }

    private void establecerDatos(){
        dni = datos.getString("dni");

        info.putString("dni", dni);
        laboralFragment.setArguments(info);
        personalFragment.setArguments(info);

        empleado = bd.datosUsuario(dni);

        txvNombre.setText(empleado.getNombre() + " " + empleado.getApellidos());

        if(bd.obtenerImagen(dni) != null){
            imgEmpleado.setImageBitmap(bd.obtenerImagen(dni));
        } else {
            imgEmpleado.setImageResource(R.drawable.user_logo);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.correo:
                Intent email = new Intent(Intent.ACTION_SENDTO);
                email.setData(Uri.parse("mailto:" + empleado.getCorreo()));
                // email.putExtra(Intent.EXTRA_EMAIL, empleado.getCorreo());
                if(email.resolveActivity(getPackageManager()) != null){
                    startActivity(email);
                }
                break;
            case R.id.llamar:
                int checkPermisos = ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
                if(checkPermisos != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CALL_PHONE}, 255);
                } else {
                    String telefono = "tel:" + empleado.getMovil();
                    Intent llamar = new Intent(Intent.ACTION_CALL, Uri.parse(telefono));
                    startActivity(llamar);
                }
                break;
        }
    }

    private void establecerTabs(){

        tabLayout.setupWithViewPager(viewPager);

        UserActivity.ViewPagerAdapter viewPagerAdapter = new UserActivity.ViewPagerAdapter(getSupportFragmentManager(), 0);
        viewPagerAdapter.añadirFragmento(personalFragment, "Info. Personal");
        viewPagerAdapter.añadirFragmento(laboralFragment, "Info. Laboral");
        viewPager.setAdapter(viewPagerAdapter);
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
