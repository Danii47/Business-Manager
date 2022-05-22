package com.example.proyectotfgjavierlahoz.actividades.usuario;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.proyectotfgjavierlahoz.R;
import com.example.proyectotfgjavierlahoz.actividades.MainActivity;
import com.example.proyectotfgjavierlahoz.actividades.registro.LoginActivity;
import com.example.proyectotfgjavierlahoz.modelos.Departamento;
import com.example.proyectotfgjavierlahoz.modelos.Empleado;
import com.example.proyectotfgjavierlahoz.sql.DatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;

public class DataDepActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseHelper databaseHelper;
    private Departamento departamento;

    private ImageView imgDepartamento;
    private ImageView imgEdit;
    private EditText edtNombre;
    private EditText edtEncargado;

    private FloatingActionButton btnGuardar;
    private FloatingActionButton btnCancelar;
    private FloatingActionButton btnBorrar;

    private Dialog dialog;

    private Bundle datos;
    private String codigo;

    Uri rutaImagen;
    Bitmap imagenBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_dep);

        inicializarVistas();
        inicializarObjetos();
        escuchadorBotones();
        establecerDatos();
    }

    private void escuchadorBotones(){
        btnCancelar.setOnClickListener(this);
        btnGuardar.setOnClickListener(this);
        btnBorrar.setOnClickListener(this);
        imgEdit.setOnClickListener(this);
    }

    private void inicializarVistas(){
        imgDepartamento = (ImageView) findViewById(R.id.imgDepartamento);
        edtNombre = (EditText) findViewById(R.id.edtNombre);
        edtEncargado = (EditText) findViewById(R.id.edtEncargado);
        imgEdit = (ImageView) findViewById(R.id.imgEdit);
        btnGuardar = (FloatingActionButton) findViewById(R.id.btnGuardar);
        btnBorrar = (FloatingActionButton) findViewById(R.id.btnBorrar);
        btnCancelar = (FloatingActionButton) findViewById(R.id.btnCancelar);
    }

    private void inicializarObjetos(){
        datos = getIntent().getExtras();
        databaseHelper = new DatabaseHelper(this);
        departamento = new Departamento();
    }

    private void establecerDatos(){
        codigo = datos.getString("codigo");
        departamento = databaseHelper.datosDepartamento(codigo);

        edtNombre.setText(departamento.getNombre());
        edtEncargado.setText(departamento.getEncargado());


        Bitmap imagen = databaseHelper.obtenerImagenDep(codigo);

        if(imagen != null){
            imgDepartamento.setImageBitmap(imagen);
        } else{
            imgDepartamento.setImageResource(R.drawable.ic_outline_home_work_24);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnCancelar:
                Intent back = new Intent(this, MainActivity.class);
                back.putExtra("dni", LoginActivity.dni);
                startActivity(back);
                break;
            case R.id.btnBorrar:
                borrarCuenta();
                break;
            case R.id.btnGuardar:
                guardarInformacion();
                break;
            case R.id.imgEdit:
                escogerImagen();
                break;
        }
    }

    private void guardarInformacion() {
        departamento.setNombre(edtNombre.getText().toString());
        departamento.setEncargado(edtEncargado.getText().toString());

        // CAMBIAR
        if(imagenBitmap != null){
            databaseHelper.guardarImagenDep(imagenBitmap, codigo);
        }

        databaseHelper.cambiarDatosDepartamento(codigo, departamento);

        Intent main = new Intent(this, MainActivity.class);
        startActivity(main);
    }

    private void borrarCuenta(){
        dialog = new Dialog(DataDepActivity.this);
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
                databaseHelper.eliminarDepartamento(codigo);
                Toast.makeText(DataDepActivity.this,getString(R.string.menu_cuenta_borrada), Toast.LENGTH_LONG).show();
                Intent inicioSesion = new Intent(DataDepActivity.this, MainActivity.class);
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
                imgDepartamento.setImageBitmap(imagenBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}