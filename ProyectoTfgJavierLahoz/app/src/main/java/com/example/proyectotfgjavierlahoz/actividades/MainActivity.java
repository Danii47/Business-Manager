package com.example.proyectotfgjavierlahoz.actividades;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.proyectotfgjavierlahoz.R;
import com.example.proyectotfgjavierlahoz.actividades.registro.LoginActivity;
import com.example.proyectotfgjavierlahoz.databinding.ActivityMainBinding;
import com.example.proyectotfgjavierlahoz.modelos.Empleado;
import com.example.proyectotfgjavierlahoz.sql.DatabaseHelper;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    private String dni;

    private Dialog dialog;

    private TextView txvNombre;
    private TextView txvCorreo;
    private Button btnCerrarSesion;
    private Button btnBorrarCuenta;
    private ImageView imagenUsuario;

    private Empleado empleado;
    private DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarMain.toolbar);

        binding.appBarMain.toolbar.setBackgroundColor(getResources().getColor(R.color.gris));


        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        View view = navigationView.getHeaderView(0);




        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_inicio, R.id.nav_empleados, R.id.nav_departamentos, R.id.nav_fichaje)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        inicializarVistas(view);
        inicializarObjetos();

        establecerDatosUsuario();
        escuchadorBotones();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void inicializarVistas(View view) {
        txvNombre = (TextView) view.findViewById(R.id.txvNombre);
        txvCorreo = (TextView) view.findViewById(R.id.txvCorreo);
        btnCerrarSesion = (Button) findViewById(R.id.btnCerrarSesion);
        imagenUsuario = (ImageView) view.findViewById(R.id.imagenUsuario);
        btnBorrarCuenta = (Button) findViewById(R.id.btnBorrarCuenta);
    }

    private void inicializarObjetos(){
        databaseHelper = new DatabaseHelper(this);
        empleado = new Empleado();
    }

    private void establecerDatosUsuario(){
        dni = LoginActivity.dni;

        Log.i("testdni",dni);
        empleado = databaseHelper.datosUsuario(dni);

        txvNombre.setText(empleado.getNombre() + " " + empleado.getApellidos());
        txvCorreo.setText(empleado.getCorreo());

        Bitmap imagen = databaseHelper.obtenerImagenEmpleado(dni);
        if(imagen != null){
            imagenUsuario.setImageBitmap(imagen);
        } else {
            imagenUsuario.setImageResource(R.drawable.user_logo);
        }
    }

    private void escuchadorBotones(){
        btnCerrarSesion.setOnClickListener(this);
        imagenUsuario.setOnClickListener(this);
        btnBorrarCuenta.setOnClickListener(this);
    }

    private void dialogoCerrarSesion(){

        dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.setCancelable(false);

        TextView txvTitulo = dialog.findViewById(R.id.txvDialog);
        TextView txvTexto = dialog.findViewById(R.id.txvDialog2);
        txvTitulo.setText("Cerrar sesion");
        txvTexto.setText("¿Seguro que desea cerrar sesion?");


        Button btnAceptar = dialog.findViewById(R.id.btnAceptar);
        Button btnCancelar = dialog.findViewById(R.id.btnCancelar);

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginActivity = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(loginActivity);
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void dialogoBorrarCuenta(){

        dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.setCancelable(false);

        TextView txvTitulo = dialog.findViewById(R.id.txvDialog);
        TextView txvTexto = dialog.findViewById(R.id.txvDialog2);
        txvTitulo.setText("Borrar cuenta");
        txvTexto.setText("¿Seguro que desea borrar la cuenta?");

        ImageView imgdialog = dialog.findViewById(R.id.imgDialog);
        imgdialog.setImageResource(R.drawable.ic_baseline_delete_24);

        Button btnAceptar = dialog.findViewById(R.id.btnAceptar);
        Button btnCancelar = dialog.findViewById(R.id.btnCancelar);

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseHelper.eliminarUsuario(dni);
                Toast.makeText(MainActivity.this,getString(R.string.menu_cuenta_borrada), Toast.LENGTH_LONG).show();
                Intent inicioSesion = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(inicioSesion);
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnCerrarSesion:
                dialogoCerrarSesion();
                break;
            case R.id.btnBorrarCuenta:
                dialogoBorrarCuenta();
                break;
        }
    }

}