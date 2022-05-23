package com.example.proyectotfgjavierlahoz.actividades.usuario;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectotfgjavierlahoz.R;
import com.example.proyectotfgjavierlahoz.actividades.MainActivity;
import com.example.proyectotfgjavierlahoz.actividades.registro.LoginActivity;
import com.example.proyectotfgjavierlahoz.modelos.Departamento;
import com.example.proyectotfgjavierlahoz.modelos.Empleado;
import com.example.proyectotfgjavierlahoz.sql.DatabaseHelper;
import com.example.proyectotfgjavierlahoz.validadores.ValidacionEntradas;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseHelper databaseHelper;
    private Empleado empleado;
    private ValidacionEntradas validacion;

    private ImageView imagenUsuario;
    private ImageView editImagen;
    private EditText edtNombre;
    private TextView txvDni;
    private EditText edtCorreo;
    private EditText edtMovil;
    private EditText edtDireccion;
    private EditText edtApellido;
    private EditText edtPuesto;
    private FloatingActionButton btnCancelar;
    private FloatingActionButton btnGuardar;
    private FloatingActionButton btnBorrar;
    private FloatingActionButton btnExpandir;
    private FloatingActionButton btnContraseña;
    private Switch swcAdministrador;
    private Spinner spDepartamento;

    private boolean pulsado = false;

    private Animation rotacionAbrir;
    private Animation rotacionCerrar;
    private Animation  ascender;
    private Animation  descender;
    private String dni;

    private List<String> nombreDeps;
    private List<Departamento> departamentos;

    private Dialog dialog;
    private Bundle datos;

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
        btnBorrar.setOnClickListener(this);
        editImagen.setOnClickListener(this);
        btnExpandir.setOnClickListener(this);
        btnContraseña.setOnClickListener(this);
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
        btnBorrar = (FloatingActionButton) findViewById(R.id.btnBorrar);
        btnContraseña = (FloatingActionButton) findViewById(R.id.btnCOntraseña);
        editImagen = (ImageView) findViewById(R.id.imgEdit);
        swcAdministrador = (Switch) findViewById(R.id.swcAdministrador);
        spDepartamento = (Spinner) findViewById(R.id.spDep);
        edtPuesto = (EditText) findViewById(R.id.edtPuesto);
        btnExpandir = (FloatingActionButton) findViewById(R.id.fabExpandir);

        rotacionAbrir = AnimationUtils.loadAnimation(DataActivity.this, R.anim.rotate_open_anim);
        rotacionCerrar = AnimationUtils.loadAnimation(DataActivity.this, R.anim.rotate_close_anim);
        ascender= AnimationUtils.loadAnimation(DataActivity.this, R.anim.from_bottom_anim);
        descender= AnimationUtils.loadAnimation(DataActivity.this, R.anim.to_bottom_anim);
    }

    private void inicializarObjetos(){
        datos = getIntent().getExtras();
        databaseHelper = new DatabaseHelper(this);
        empleado = new Empleado();
        validacion = new ValidacionEntradas(DataActivity.this);
    }

    private void establecerDatos(){
        dni = datos.getString("dni");

        datosAdministrador();
        establecerTextos();
        funcionesAdministrador();
        establecerImagen();
        establecerSpinner();

    }

    private void establecerTextos() {
        empleado = databaseHelper.datosUsuario(dni);
        edtNombre.setText(empleado.getNombre());
        edtApellido.setText(empleado.getApellidos());
        edtCorreo.setText(empleado.getCorreo());
        edtMovil.setText(empleado.getMovil());
        edtDireccion.setText(empleado.getDireccion());
        edtPuesto.setText(empleado.getPuesto());
        txvDni.setText(empleado.getDni());
    }

    private void establecerImagen() {
        Bitmap imagen = databaseHelper.obtenerImagenEmpleado(dni);

        if(imagen != null){
            imagenUsuario.setImageBitmap(imagen);
        } else{
            imagenUsuario.setImageResource(R.drawable.user_logo);
        }
    }

    private void funcionesAdministrador() {
        if(empleado.getAdministrador() == 1){
            swcAdministrador.setChecked(true);
        } else {
            swcAdministrador.setChecked(false);
        }
    }

    private void establecerSpinner() {

        departamentos = databaseHelper.obtenerDepartamentos();

        nombreDeps = new ArrayList<>();
        for(Departamento dep : departamentos){
            nombreDeps.add(dep.getNombre());
        }

        spDepartamento.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_list, nombreDeps));

        String codEmpDep = empleado.getDepartamento();

        for (int i = 0;i < departamentos.size(); i++){
            Departamento dep = departamentos.get(i);
            if (dep.getCodigo().equals(codEmpDep)) {
                spDepartamento.setSelection(i);
            }
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
            case R.id.btnBorrar:
                comprobarContraseña();
                break;
            case R.id.btnGuardar:
                guardarInformacion();
                break;
            case R.id.imgEdit:
                escogerImagen();
                break;
            case R.id.fabExpandir:
                establecerBotones();
                break;
            case R.id.btnCOntraseña:
                cambiarContraseña();
                break;
        }
    }

    private void comprobarContraseña(){
        dialog = new Dialog(DataActivity.this);
        dialog.setContentView(R.layout.custom_dialog_con);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.setCancelable(false);

        EditText edtPass = dialog.findViewById(R.id.edtPass);

        Button btnAceptar = dialog.findViewById(R.id.btnAceptarContraseña);
        Button btnCancelar = dialog.findViewById(R.id.btnCancelar);

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String contraseña = empleado.getContraseña();
                if(!validacion.campoVacio(edtPass, getString(R.string.contraseñaVacia))){
                    return;
                }
                if(!contraseña.equals(edtPass.getText().toString())){
                    Toast.makeText(DataActivity.this, getString(R.string.errorContraseña), Toast.LENGTH_LONG).show();
                    return;
                }
                dialog.hide();
                borrarCuenta();
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

    private void cambiarContraseña(){

        dialog = new Dialog(DataActivity.this);
        dialog.setContentView(R.layout.custom_dialog_cam_con);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.setCancelable(false);

        EditText edtAntiguaPass = dialog.findViewById(R.id.edtAntiguaPass);
        EditText edtNuevaPass = dialog.findViewById(R.id.edtNuevaPass);
        EditText edtNuevaPasConf = dialog.findViewById(R.id.edtNuevaPass2);

        Button btnAceptar = dialog.findViewById(R.id.btnAceptarContraseña);
        Button btnCancelar = dialog.findViewById(R.id.btnCancelar);

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String contraseña = empleado.getContraseña();
                if(!validacion.campoVacio(edtAntiguaPass, getString(R.string.contraseñaVacia))){
                    return;
                }
                if(!validacion.campoVacio(edtNuevaPasConf, getString(R.string.contraseñaVacia))){
                    return;
                }
                if(!validacion.campoVacio(edtNuevaPass, getString(R.string.contraseñaVacia))){
                    return;
                }
                if(!contraseña.equals(edtAntiguaPass.getText().toString())){
                    Toast.makeText(DataActivity.this, getString(R.string.errorContraseña), Toast.LENGTH_LONG).show();
                    return;
                }
                if(!validacion.comprobacionContraseña(edtNuevaPass, edtNuevaPasConf, getString(R.string.errorContraseña))){
                    return;
                }
                String nuevaContraseña = edtNuevaPass.getText().toString();

                databaseHelper.cambiarContraseña(LoginActivity.dni, nuevaContraseña);
                Toast.makeText(DataActivity.this, getString(R.string.cambiarContraseña), Toast.LENGTH_LONG).show();
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

    private void establecerBotones(){
        establecerVisibilidad();
        establecerAnimaciones();
        if(!pulsado == true){
            pulsado = true;
        } else {
            pulsado = false;
        }
    }

    private void establecerAnimaciones() {
        if(!pulsado){
            btnBorrar.startAnimation(ascender);
            btnCancelar.startAnimation(ascender);
            btnGuardar.startAnimation(ascender);
            btnContraseña.startAnimation(ascender);
            btnExpandir.startAnimation(rotacionAbrir);
        } else {
            btnBorrar.startAnimation(descender);
            btnCancelar.startAnimation(descender);
            btnGuardar.startAnimation(descender);
            btnContraseña.startAnimation(descender);
            btnExpandir.startAnimation(rotacionCerrar);
        }
    }

    private void establecerVisibilidad(){
        if(!pulsado){
            Empleado admin = databaseHelper.datosUsuario(LoginActivity.dni);

            btnBorrar.setVisibility(View.VISIBLE);
            btnCancelar.setVisibility(View.VISIBLE);
            btnGuardar.setVisibility(View.VISIBLE);
        } else {
            btnBorrar.setVisibility(View.INVISIBLE);
            btnCancelar.setVisibility(View.INVISIBLE);
            btnGuardar.setVisibility(View.INVISIBLE);
        }
    }

    private void guardarInformacion() {
        empleado.setNombre(edtNombre.getText().toString());
        empleado.setApellidos(edtApellido.getText().toString());
        empleado.setCorreo(edtCorreo.getText().toString());
        empleado.setDireccion(edtDireccion.getText().toString());
        empleado.setMovil(edtMovil.getText().toString());
        empleado.setPuesto(edtPuesto.getText().toString());

        int i = spDepartamento.getSelectedItemPosition();
        String codigoDep = departamentos.get(i).getCodigo();

        databaseHelper.establecerEmpleadoDep(codigoDep, dni);

        if(swcAdministrador.isChecked()){
            empleado.setAdministrador(1);
        } else {
            empleado.setAdministrador(0);
        }

        if(imagenBitmap != null){
            databaseHelper.guargarImagenEmpleado(imagenBitmap, dni);
        }

        databaseHelper.cambiarDatosUsuario(dni, empleado);

        dni = empleado.getDni();
        Intent main = new Intent(this, MainActivity.class);
        startActivity(main);
    }

    private void borrarCuenta(){
        dialog = new Dialog(DataActivity.this);
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
                Toast.makeText(DataActivity.this,getString(R.string.menu_cuenta_borrada), Toast.LENGTH_LONG).show();
                Intent inicioSesion = new Intent(DataActivity.this,LoginActivity.class);
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
                imagenUsuario.setImageBitmap(imagenBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void datosAdministrador(){
        if(LoginActivity.administrador == true){
            swcAdministrador.setClickable(true);
        }
    }
}