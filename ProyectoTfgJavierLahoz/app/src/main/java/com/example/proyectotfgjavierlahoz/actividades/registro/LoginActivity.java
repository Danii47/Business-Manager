package com.example.proyectotfgjavierlahoz.actividades.registro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.proyectotfgjavierlahoz.R;
import com.example.proyectotfgjavierlahoz.actividades.MainActivity;
import com.example.proyectotfgjavierlahoz.actividades.registro.RegisterActivity;
import com.example.proyectotfgjavierlahoz.sql.DatabaseHelper;
import com.example.proyectotfgjavierlahoz.validadores.ValidacionSesion;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public static String dni;

    EditText edtDni;
    EditText edtContraseña;

    Button btnAcceder;
    Button btnRegistro;

    ValidacionSesion validacionSesion;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inicializarVistas();
        inicializarObjetos();
        escuchadorBotones();
    }

    private void inicializarVistas(){
        edtDni = (EditText) findViewById(R.id.edtDni);
        edtContraseña = (EditText) findViewById(R.id.edtContraseña);
        btnAcceder = (Button) findViewById(R.id.btnAcceder);
        btnRegistro = (Button) findViewById(R.id.btnRegistro);
    }

    private void inicializarObjetos(){
        validacionSesion = new ValidacionSesion(this);
        databaseHelper = new DatabaseHelper(this);
    }

    private void escuchadorBotones() {
        btnAcceder.setOnClickListener(this);
        btnRegistro.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnAcceder:
                verificarDatos();
                break;
            case R.id.btnRegistro:
                Intent activityRegistro = new Intent(this, RegisterActivity.class);
                startActivity(activityRegistro);
                break;
        }
    }

    private void verificarDatos(){
        if(!validacionSesion.campoVacio(edtDni, getString(R.string.errorDni))){
            return;
        }
        if(!validacionSesion.comprobarDni(edtDni, getString(R.string.errorDni))){
            return;
        }
        if(!validacionSesion.campoVacio(edtContraseña, getString(R.string.contraseñaVacia))){
            return;
        }
        dni = edtDni.getText().toString().trim();
        String contraseña = edtContraseña.getText().toString().trim();

        if(databaseHelper.comprobarUsuario(dni, contraseña)){
            Intent login = new Intent(this , MainActivity.class);
            login.putExtra("dni", dni);
            startActivity(login);
            Log.i("testt", "intent aplicacion");
        } else {
            Toast.makeText(this, getString(R.string.errorLogin), Toast.LENGTH_LONG).show();
        }
    }
}