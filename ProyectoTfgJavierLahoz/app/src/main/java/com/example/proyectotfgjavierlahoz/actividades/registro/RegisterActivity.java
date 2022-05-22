package com.example.proyectotfgjavierlahoz.actividades.registro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.proyectotfgjavierlahoz.R;
import com.example.proyectotfgjavierlahoz.modelos.Empleado;
import com.example.proyectotfgjavierlahoz.sql.DatabaseHelper;
import com.example.proyectotfgjavierlahoz.validadores.ValidacionEntradas;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edtNombre;
    EditText edtApellidos;
    EditText edtEmail;
    EditText edtDni;
    EditText edtContraseña;
    EditText edtConfContraseña;

    Button btnAcceder;
    Button btnRegistro;

    ValidacionEntradas validacionSesion;
    DatabaseHelper dataBaseHelper;
    Empleado empleado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inicializarVistas();
        inicializarObjetos();
        escuchadorBotones();
    }

    private void inicializarVistas(){
        edtNombre = (EditText) findViewById(R.id.edtNombre);
        edtApellidos = (EditText) findViewById(R.id.edtApellido);
        edtEmail = (EditText) findViewById(R.id.edtCorreo);
        edtDni = (EditText) findViewById(R.id.edtDni);
        edtContraseña = (EditText) findViewById(R.id.edtContraseña);
        edtConfContraseña = (EditText) findViewById(R.id.edtConfContraseña);
        btnAcceder = (Button) findViewById(R.id.btnAcceder);
        btnRegistro = (Button) findViewById(R.id.btnRegistro);
    }

    private void inicializarObjetos(){
        validacionSesion = new ValidacionEntradas(this);
        dataBaseHelper = new DatabaseHelper(this);
        empleado = new Empleado();

    }

    private void escuchadorBotones(){
        btnAcceder.setOnClickListener(this);
        btnRegistro.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btnAcceder:
                Intent activityLogin = new Intent(this, LoginActivity.class);
                startActivity(activityLogin);
                break;
            case R.id.btnRegistro:
                verificarDatos();
                break;
        }
    }

    private void verificarDatos(){
        if(!validacionSesion.campoVacio(edtNombre, getString(R.string.errorNombre))){
            return;
        }
        if(!validacionSesion.campoVacio(edtApellidos, getString(R.string.errorApellidos))){
            return;
        }
        if(!validacionSesion.campoVacio(edtEmail, getString(R.string.errorEmail))){
            return;
        }
        if(!validacionSesion.comprobarCorreo(edtEmail, getString(R.string.errorEmail))){
            return;
        }
        if(!validacionSesion.comprobarDni(edtDni, getString(R.string.errorDni))){
            return;
        }
        if(!validacionSesion.campoVacio(edtContraseña, getString(R.string.contraseñaVacia))){
            return;
        }
        if(!validacionSesion.campoVacio(edtConfContraseña, getString(R.string.contraseñaVacia))){
            return;
        }
        if(!validacionSesion.comprobacionContraseña(edtContraseña, edtConfContraseña, getString(R.string.errorContraseña))){
            return;
        }
        if(dataBaseHelper.comprobarDni(edtDni.getText().toString())){
            if(!dataBaseHelper.comprobarUsuario(edtDni.getText().toString())){
                empleado.setDni(edtDni.getText().toString().trim());
                empleado.setNombre(edtNombre.getText().toString().trim());
                empleado.setApellidos(edtApellidos.getText().toString().trim());
                empleado.setCorreo(edtEmail.getText().toString().trim());
                empleado.setContraseña(edtContraseña.getText().toString().trim());

                dataBaseHelper.añadirEmpleado(empleado);

                Toast.makeText(this,getString(R.string.registroCorrecto), Toast.LENGTH_LONG).show();

                Intent login = new Intent(this, LoginActivity.class);
                startActivity(login);
            } else {
                Toast.makeText(this, getString(R.string.errorRegistro), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this,getString(R.string.permisosRegistro), Toast.LENGTH_LONG).show();
        }
    }
}
