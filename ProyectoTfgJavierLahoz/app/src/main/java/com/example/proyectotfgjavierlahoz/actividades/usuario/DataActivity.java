package com.example.proyectotfgjavierlahoz.actividades.usuario;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectotfgjavierlahoz.R;
import com.example.proyectotfgjavierlahoz.actividades.MainActivity;
import com.example.proyectotfgjavierlahoz.modelos.Empleado;
import com.example.proyectotfgjavierlahoz.sql.DatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;

public class DataActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseHelper databaseHelper;
    private Empleado empleado;

    private ImageView imagenUsuario;
    private ImageView editImagen;
    private EditText edtNombre;
    private TextView txvDni;
    private EditText edtCorreo;
    private EditText edtMovil;
    private EditText edtDireccion;
    private EditText edtApellido;
    private FloatingActionButton btnCancelar;
    private FloatingActionButton btnGuardar;

    private Bundle datos;
    private String dni;

    Uri rutaImagen;
    Bitmap imagenBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        inicializarVistas();
        inicializarObjetos();
        escuchadorBotones();
        establecerDatos();
    }

    private void escuchadorBotones(){
        btnCancelar.setOnClickListener(this);
        btnGuardar.setOnClickListener(this);
        editImagen.setOnClickListener(this);
    }

    private void inicializarVistas(){
        imagenUsuario = (ImageView) findViewById(R.id.imgEmpleado);
        edtNombre = (EditText) findViewById(R.id.edtNombre);
        edtApellido = (EditText) findViewById(R.id.edtApellido);
        edtCorreo = (EditText) findViewById(R.id.edtEmail);
        edtMovil = (EditText) findViewById(R.id.edtMovil);
        edtDireccion = (EditText) findViewById(R.id.edtDireccion);
        txvDni = (TextView) findViewById(R.id.txvDni2);
        btnCancelar = (FloatingActionButton) findViewById(R.id.btnCancelar);
        btnGuardar = (FloatingActionButton) findViewById(R.id.btnGuardar);
        editImagen = (ImageView) findViewById(R.id.imgEdit);
    }

    private void inicializarObjetos(){
        datos = getIntent().getExtras();
        databaseHelper = new DatabaseHelper(this);
        empleado = new Empleado();
    }

    private void establecerDatos(){
        dni = datos.getString("dni");

        empleado = databaseHelper.datosUsuario(dni);
        edtNombre.setText(empleado.getNombre());
        edtApellido.setText(empleado.getApellidos());
        edtCorreo.setText(empleado.getCorreo());
        edtMovil.setText(empleado.getMovil());
        edtDireccion.setText(empleado.getDireccion());
        txvDni.setText(empleado.getDni());

        Bitmap imagen = databaseHelper.obtenerImagen(dni);

        if(imagen != null){
            imagenUsuario.setImageBitmap(imagen);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnCancelar:
                Intent back = new Intent(this, MainActivity.class);
                back.putExtra("dni",dni);
                startActivity(back);
                break;
            case R.id.btnGuardar:

                empleado.setNombre(edtNombre.getText().toString());
                empleado.setApellidos(edtApellido.getText().toString());
                empleado.setCorreo(edtCorreo.getText().toString());
                empleado.setDireccion(edtDireccion.getText().toString());
                empleado.setMovil(edtMovil.getText().toString());

                if(imagenBitmap != null){
                    databaseHelper.guardarImagen(imagenBitmap, dni);
                }

                databaseHelper.cambiarDatosUsuario(dni, empleado);

                dni = empleado.getDni();
                Intent main = new Intent(this, MainActivity.class);
                main.putExtra("dni", dni);
                startActivity(main);
                break;
            case R.id.imgEdit:
                escogerImagen();
                break;
        }
    }

    private void escogerImagen(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
        } else {
            Intent escogerFoto = new Intent();
            escogerFoto.setType("image/*");
            escogerFoto.setAction(Intent.ACTION_GET_CONTENT);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                startActivityForResult(Intent.createChooser(escogerFoto,"Escoger imagen"), 1);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null){
            rutaImagen = data.getData();
            try {
                imagenBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), rutaImagen);
                imagenUsuario.setImageBitmap(imagenBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}