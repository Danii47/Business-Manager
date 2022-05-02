package com.example.proyectotfgjavierlahoz.sql;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.widget.ListAdapter;

import com.example.proyectotfgjavierlahoz.R;
import com.example.proyectotfgjavierlahoz.modelos.Empleado;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private ByteArrayOutputStream baos;
    private byte[] bytesImagen;

    private static final int BD_VERSION = 1;

    private static final String BD_NOMBRE = "GestionUsuarios.db";

    private static final String TABLA_EMPLEADOS = "empleados";
    private static final String TABLA_DEPARTAMENTOS = "departamentos";

    private static final String COL_EMP_DNI = "empleado_dni";
    private static final String COL_EMP_NOMBRE = "empleado_nombre";
    private static final String COL_EMP_APELLIDOS = "empleado_apellidos";
    private static final String COL_EMP_CONTRASEÑA = "empleado_contraseña";
    private static final String COL_EMP_CORREO = "empleado_correo";
    private static final String COL_EMP_ADMIN = "empleado_administrador";
    private static final String COL_EMP_DIREC = "empleado_direccion";
    private static final String COL_EMP_MOVIL = "empleado_movil";
    private static final String COL_EMP_IMAGEN = "empleado_imagen";
    private static final String COL_EMP_DPTO = "empleado_dpto";
    private static final String COL_EMP_PUESTO = "empleado_puesto";
    private static final String COL_DEP_CODIGO = "departamento_codigo";
    private static final String COL_DEP_NOMBRE = "departamento_nombre";
    private static final String COL_DEP_ENCARGADO = "departamento_encargado";

    private String CREAR_TABLA_DEPARTAMENTOS = "Create table " + TABLA_DEPARTAMENTOS + "(" + COL_DEP_CODIGO  + " TEXT PRIMARY KEY," +
            COL_DEP_NOMBRE + " TEXT NOT NULL, " + COL_DEP_ENCARGADO + " TEXT )";
    private String BORRAR_TABLA_DEPARTAMENTOS = "Drop table if exists " + TABLA_DEPARTAMENTOS;

    private String CREAR_TABLA_EMPLEADOS = "Create table " + TABLA_EMPLEADOS + " (" + COL_EMP_DNI + " TEXT PRIMARY KEY," +
            COL_EMP_NOMBRE + " TEXT NOT NULL, " + COL_EMP_APELLIDOS + " TEXT NOT NULL," +
            COL_EMP_CORREO + " TEXT NOT NULL, " + COL_EMP_DIREC + " TEXT, " + COL_EMP_MOVIL + " TEXT, " +
            COL_EMP_CONTRASEÑA + " TEXT NOT NULL, " + COL_EMP_ADMIN + " INTEGER DEFAULT 0, " + COL_EMP_IMAGEN + " blob," +
            COL_EMP_PUESTO + " TEXT, " +
            COL_EMP_DPTO + " integer, Foreign key (" + COL_EMP_DPTO + ") references " + TABLA_DEPARTAMENTOS + "(" + COL_DEP_CODIGO + "))";

    private String BORRAR_TABLA_EMPLEADOS = "Drop table if exists " + TABLA_EMPLEADOS;

    private Context context;

    public DatabaseHelper(Context context){
        super(context, BD_NOMBRE, null, BD_VERSION);
        this.context = context.getApplicationContext();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREAR_TABLA_DEPARTAMENTOS);
        db.execSQL(CREAR_TABLA_EMPLEADOS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAntigua, int versionNueva) {
        db.execSQL(BORRAR_TABLA_DEPARTAMENTOS);
        db.execSQL(BORRAR_TABLA_EMPLEADOS);
        onCreate(db);
    }

    public void añadirEmpleado(Empleado emp){
        SQLiteDatabase bd = this.getWritableDatabase();
        ContentValues datos = new ContentValues();
        datos.put(COL_EMP_DNI, emp.getDni());
        datos.put(COL_EMP_NOMBRE, emp.getNombre());
        datos.put(COL_EMP_APELLIDOS, emp.getApellidos());
        datos.put(COL_EMP_CORREO, emp.getCorreo());
        datos.put(COL_EMP_DIREC, emp.getDireccion());
        datos.put(COL_EMP_MOVIL, emp.getMovil());
        datos.put(COL_EMP_CONTRASEÑA, emp.getContraseña());
        datos.put(COL_EMP_ADMIN, emp.getAdministrador());

        bd.insert(TABLA_EMPLEADOS, null, datos);
        bd.close();
    }

    public boolean comprobarUsuario(String dni){

        SQLiteDatabase bd = this.getReadableDatabase();
        boolean usuarioEncontrado = false;

        String query = "Select empleado_dni from empleados where empleado_dni = ?";

        String[] columnas = {COL_EMP_DNI};
        String consulta = COL_EMP_DNI  + " = ?";
        String[] argumentos = {dni};

        Cursor cursor = bd.rawQuery(query, argumentos);

        if(cursor.getCount() >= 1){
            usuarioEncontrado = true;
        }

        return usuarioEncontrado;

    }

    public boolean comprobarUsuario(String dni, String contraseña){

        SQLiteDatabase bd = this.getReadableDatabase();
        boolean usuarioEncontrado = false;

        String consulta = "Select empleado_dni from empleados where " + COL_EMP_DNI + "= ? and " + COL_EMP_CONTRASEÑA + " = ?";

        String[] columnas = {COL_EMP_DNI};
        String[] argumentos = {dni, contraseña};

        Cursor cursor = bd.rawQuery(consulta, argumentos);

        if(cursor.getCount() >= 1){
            usuarioEncontrado = true;
        }

        return usuarioEncontrado;
    }

    @SuppressLint("Range")
    public Empleado datosUsuario(String dni){

        SQLiteDatabase bd = this.getReadableDatabase();
        Empleado empleado =  null;

        String consulta = "Select * from " + TABLA_EMPLEADOS  + " where " + COL_EMP_DNI + " = ?";
        String [] argumentos = {dni};
        Log.i("testt", argumentos[0]);
        Cursor cursor = bd.rawQuery(consulta, argumentos);
        if(cursor.moveToFirst()){
            empleado = new Empleado();
            empleado.setNombre(cursor.getString(cursor.getColumnIndex(COL_EMP_NOMBRE)));
            empleado.setApellidos(cursor.getString(cursor.getColumnIndex(COL_EMP_APELLIDOS)));
            empleado.setDni(cursor.getString(cursor.getColumnIndex(COL_EMP_DNI)));
            empleado.setCorreo(cursor.getString(cursor.getColumnIndex(COL_EMP_CORREO)));
            empleado.setMovil(cursor.getString(cursor.getColumnIndex(COL_EMP_MOVIL)));
            empleado.setDireccion(cursor.getString(cursor.getColumnIndex(COL_EMP_DIREC)));
            empleado.setDepartamento(cursor.getString(cursor.getColumnIndex(COL_EMP_DPTO)));
            empleado.setPuesto(cursor.getString(cursor.getColumnIndex(COL_EMP_PUESTO)));
        }
        return empleado;
    }

    public void guardarImagen(Bitmap imagenGuardar, String dni){

        SQLiteDatabase bd = this.getWritableDatabase();

        baos = new ByteArrayOutputStream();
        imagenGuardar.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        bytesImagen = baos.toByteArray();

        ContentValues datos = new ContentValues();
        datos.put(COL_EMP_IMAGEN, bytesImagen);

        int rows = bd.update(TABLA_EMPLEADOS, datos,COL_EMP_DNI + " = '"+ dni + "'", null);

        Log.i("test2", String.valueOf(rows));
        bd.close();
    }

    public Bitmap obtenerImagen(String dni){
        SQLiteDatabase bd = this.getReadableDatabase();
        Bitmap imagen = null;

        String consulta = "Select " + COL_EMP_IMAGEN + " from " + TABLA_EMPLEADOS  + " where " + COL_EMP_DNI + " = ?";
        String [] argumentos = {dni};

        Cursor cursor = bd.rawQuery(consulta, argumentos);

        if(cursor.moveToFirst()){
            byte[] bytesImagen = cursor.getBlob(0);
            if(bytesImagen != null){
                imagen = BitmapFactory.decodeByteArray(bytesImagen, 0, bytesImagen.length);
            }
        }
        return imagen;
    }

    public void cambiarDatosUsuario(String dni, Empleado empleado){
        SQLiteDatabase bd = this.getWritableDatabase();

        ContentValues datos = new ContentValues();

        datos.put(COL_EMP_NOMBRE, empleado.getNombre());
        datos.put(COL_EMP_APELLIDOS, empleado.getApellidos());
        datos.put(COL_EMP_DNI,empleado.getDni());
        datos.put(COL_EMP_CORREO, empleado.getCorreo());
        datos.put(COL_EMP_DIREC, empleado.getDireccion());
        datos.put(COL_EMP_MOVIL, empleado.getMovil());

        bd.update(TABLA_EMPLEADOS, datos, COL_EMP_DNI + " = '" + dni + "'", null);
    }

    @SuppressLint("Range")
    public List<Empleado> obtenerUsuarios(){

        Bitmap imagen = null;

        List<Empleado> listaEmpleados = new ArrayList<>();
        SQLiteDatabase bd = this.getReadableDatabase();

        String[] columnas = {
                COL_EMP_DNI, COL_EMP_NOMBRE, COL_EMP_APELLIDOS, COL_EMP_CORREO, COL_EMP_IMAGEN, COL_EMP_MOVIL
        };

        Cursor cursor = bd.query(TABLA_EMPLEADOS, columnas, null, null, null, null, null);

        if(cursor.moveToFirst()){
            do {
                Empleado empleado = new Empleado();
                empleado.setDni(cursor.getString(cursor.getColumnIndex(COL_EMP_DNI)));
                empleado.setNombre(cursor.getString(cursor.getColumnIndex(COL_EMP_NOMBRE)));
                empleado.setApellidos(cursor.getString(cursor.getColumnIndex(COL_EMP_APELLIDOS)));
                empleado.setCorreo(cursor.getString(cursor.getColumnIndex(COL_EMP_CORREO)));
                empleado.setMovil(cursor.getString(cursor.getColumnIndex(COL_EMP_MOVIL)));

                byte[] bytesImagen = cursor.getBlob(cursor.getColumnIndex(COL_EMP_IMAGEN));

                if(bytesImagen != null){
                    imagen = BitmapFactory.decodeByteArray(bytesImagen, 0, bytesImagen.length);
                } else {
                    imagen = BitmapFactory.decodeResource(this.context.getResources(),R.drawable.user_logo);
                }

                empleado.setImagen(imagen);
                listaEmpleados.add(empleado);
            }while(cursor.moveToNext());
            cursor.close();
            bd.close();
        }
        return listaEmpleados;
    }
}

