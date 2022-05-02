package com.example.proyectotfgjavierlahoz.actividades.usuario;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.proyectotfgjavierlahoz.R;
import com.example.proyectotfgjavierlahoz.modelos.Empleado;
import com.example.proyectotfgjavierlahoz.sql.DatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

public class UserActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imgEmpleado;
    private TextView txvNombre;
    private TextView txvDni;
    private TextView txvEmail;
    private TextView txvNumero;
    private TextView txvDireccion;
    private FloatingActionButton btnCorreo;
    private FloatingActionButton btnLlamar;

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

    }

    private void inicializarVistas(){
        imgEmpleado = (ImageView) findViewById(R.id.imgEmpleado);
        txvNombre = (TextView) findViewById(R.id.txvNombre);
        txvDni = (TextView) findViewById(R.id.txvDni2);
        txvEmail = (TextView) findViewById(R.id.txvEmail2);
        txvNumero =(TextView) findViewById(R.id.txvMovil2);
        txvDireccion = (TextView) findViewById(R.id.txvDireccion2);
        btnLlamar = (FloatingActionButton) findViewById(R.id.llamar);
        btnCorreo = (FloatingActionButton) findViewById(R.id.correo);

    }

    private void inicializarObjetos(){
        bd = new DatabaseHelper(this);
        empleado = new Empleado();
        datos = getIntent().getExtras();
    }

    private void inicializarEscuchadores(){
        btnLlamar.setOnClickListener(this);
        btnCorreo.setOnClickListener(this);
    }

    private void establecerDatos(){
        dni = datos.getString("dni");
        empleado = bd.datosUsuario(dni);

        if(bd.obtenerImagen(dni) != null){
            imgEmpleado.setImageBitmap(bd.obtenerImagen(dni));
        } else {
            imgEmpleado.setImageResource(R.drawable.user_logo);
        }
        txvNombre.setText(empleado.getNombre() + " " + empleado.getApellidos());
        txvDni.setText(empleado.getDni());
        txvEmail.setText(empleado.getCorreo());
        txvNumero.setText(empleado.getMovil());
        txvDireccion.setText(empleado.getDireccion());

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
}
