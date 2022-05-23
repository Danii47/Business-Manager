package com.example.proyectotfgjavierlahoz.sql;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.proyectotfgjavierlahoz.R;
import com.example.proyectotfgjavierlahoz.modelos.Departamento;
import com.example.proyectotfgjavierlahoz.modelos.Empleado;
import com.example.proyectotfgjavierlahoz.modelos.Fichaje;

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
    private static final String TABLA_DNI = "dni";
    private static final String TABLA_FICHAJES = "fichajes";

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
    private static final String COL_DEP_IMAGEN = "departamento_imagen";

    private static final String COL_FIC_COD = "fichajes_cod";
    private static final String COL_FIC_EMP = "fichajes_emp";
    private static final String COL_FIC_DIA = "fichaje_dia";
    private static final String COL_FIC_INICIO = "fichaje_inicio";
    private static final String COL_FIC_FIN = "fichaje_fin";
    private static final String COL_FIC_DIA_FIN = "fichaje_dia_fin";
    private static final String COL_FIC_ACTIVO = "fichaje_activo";

    private static final String COL_DNI_DNI = "dni_empleado";

    private String CREAR_TABLA_DEPARTAMENTOS = "Create table " + TABLA_DEPARTAMENTOS + "(" + COL_DEP_CODIGO  + " TEXT PRIMARY KEY, " +
            COL_DEP_NOMBRE + " TEXT NOT NULL, " + COL_DEP_ENCARGADO + " TEXT, " + COL_DEP_IMAGEN + " blob)";

    private String CREAR_TABLA_EMPLEADOS = "Create table " + TABLA_EMPLEADOS + " (" + COL_EMP_DNI + " TEXT PRIMARY KEY," +
            COL_EMP_NOMBRE + " TEXT NOT NULL, " + COL_EMP_APELLIDOS + " TEXT NOT NULL," +
            COL_EMP_CORREO + " TEXT NOT NULL, " + COL_EMP_DIREC + " TEXT, " + COL_EMP_MOVIL + " TEXT, " +
            COL_EMP_CONTRASEÑA + " TEXT NOT NULL, " + COL_EMP_ADMIN + " INTEGER DEFAULT 0, " + COL_EMP_IMAGEN + " blob," +
            COL_EMP_PUESTO + " TEXT, " +
            COL_EMP_DPTO + " integer, Foreign key (" + COL_EMP_DPTO + ") references " + TABLA_DEPARTAMENTOS + "(" + COL_DEP_CODIGO + "))";

    private String CREAR_TABLA_DNI = "Create table " + TABLA_DNI  + "(" + COL_DNI_DNI + " TEXT PRIMARY KEY)";

    private String CREAR_TABLA_FICHAJES = "Create table " + TABLA_FICHAJES + "(" + COL_FIC_COD + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_FIC_EMP + " TEXT NOT NULL, " + COL_FIC_DIA + " TEXT NOT NULL, " + COL_FIC_INICIO + " TEXT, " + COL_FIC_FIN + " TEXT, " + COL_FIC_DIA_FIN + " TEXT, " + COL_FIC_ACTIVO + " INTEGER)";


    private String BORRAR_TABLA_EMPLEADOS = "Drop table if exists " + TABLA_EMPLEADOS;
    private String BORRAR_TABLA_DEPARTAMENTOS = "Drop table if exists " + TABLA_DEPARTAMENTOS;
    private String BORRAR_TABLA_DNI = "Drop table if exists " + TABLA_DNI;
    private String BORRAR_TABLA_FICHAJES = "Drop table if exists " + TABLA_FICHAJES;


    private Context context;

    public DatabaseHelper(Context context){
        super(context, BD_NOMBRE, null, BD_VERSION);
        this.context = context.getApplicationContext();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREAR_TABLA_DEPARTAMENTOS);
        db.execSQL(CREAR_TABLA_EMPLEADOS);
        db.execSQL(CREAR_TABLA_DNI);
        db.execSQL(CREAR_TABLA_FICHAJES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAntigua, int versionNueva) {
        db.execSQL(BORRAR_TABLA_DEPARTAMENTOS);
        db.execSQL(BORRAR_TABLA_EMPLEADOS);
        db.execSQL(BORRAR_TABLA_DNI);
        db.execSQL(BORRAR_TABLA_FICHAJES);
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
            empleado.setContraseña(cursor.getString(cursor.getColumnIndex(COL_EMP_CONTRASEÑA)));
            empleado.setMovil(cursor.getString(cursor.getColumnIndex(COL_EMP_MOVIL)));
            empleado.setDireccion(cursor.getString(cursor.getColumnIndex(COL_EMP_DIREC)));
            empleado.setDepartamento(cursor.getString(cursor.getColumnIndex(COL_EMP_DPTO)));
            empleado.setPuesto(cursor.getString(cursor.getColumnIndex(COL_EMP_PUESTO)));
            empleado.setAdministrador(cursor.getInt(cursor.getColumnIndex(COL_EMP_ADMIN)));
            empleado.setDepartamento(cursor.getString(cursor.getColumnIndex(COL_EMP_DPTO)));
        }
        return empleado;
    }

    public void guargarImagenEmpleado(Bitmap imagenGuardar, String dni){

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

    public Bitmap obtenerImagenEmpleado(String dni){
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
        datos.put(COL_EMP_ADMIN, empleado.getAdministrador());
        datos.put(COL_EMP_PUESTO, empleado.getPuesto());

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

    @SuppressLint("Range")
    public List<Empleado> obtenerUsuarios(String codigo){

        Bitmap imagen = null;
        Log.i("testbdemp", codigo);
        List<Empleado> listaEmpleados = new ArrayList<>();
        SQLiteDatabase bd = this.getReadableDatabase();

        String[] columnas = {
                COL_EMP_DNI, COL_EMP_NOMBRE, COL_EMP_APELLIDOS, COL_EMP_CORREO, COL_EMP_IMAGEN, COL_EMP_MOVIL
        };

        Cursor cursor = bd.query(TABLA_EMPLEADOS, columnas, COL_EMP_DPTO + " = " + "'" + codigo + "'", null, null, null, null);
        Log.i("testbdemp",String.valueOf(cursor.getCount()));
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

    public void añadirDNI(String dni){
        SQLiteDatabase bd = this.getWritableDatabase();

        ContentValues datos = new ContentValues();
        datos.put(COL_DNI_DNI, dni);
        bd.insert(TABLA_DNI, null, datos);
        bd.close();
    }

    public boolean comprobarDni(String dni){

        SQLiteDatabase bd = this.getReadableDatabase();
        boolean usuarioEncontrado = false;

        String query = "Select dni_empleado from dni where dni_empleado = ?";

        String[] columnas = {COL_DNI_DNI};
        String consulta = COL_DNI_DNI  + " = ?";
        String[] argumentos = {dni};

        Cursor cursor = bd.rawQuery(query, argumentos);

        if(cursor.getCount() >= 1){
            usuarioEncontrado = true;
        }
        return usuarioEncontrado;
    }

    public void eliminarUsuario(String dni){
        SQLiteDatabase bd = this.getWritableDatabase();

        bd.delete(TABLA_EMPLEADOS, COL_EMP_DNI + " = " + "'" + dni + "'", null);
        bd.delete(TABLA_DNI, COL_DNI_DNI + " = " + "'" + dni + "'", null);
    }

    @SuppressLint("Range")
    public List<Departamento> obtenerDepartamentos(){

        Bitmap imagen = null;

        List<Departamento> listaDepartamentos = new ArrayList<>();
        SQLiteDatabase bd = this.getReadableDatabase();

        String[] columnas = {
                COL_DEP_CODIGO, COL_DEP_NOMBRE, COL_DEP_ENCARGADO, COL_DEP_IMAGEN
        };

        Cursor cursor = bd.query(TABLA_DEPARTAMENTOS, columnas, null, null, null, null, null);

        if(cursor.moveToFirst()){
            do {
                Departamento departamento = new Departamento();
                departamento.setCodigo(cursor.getString(cursor.getColumnIndex(COL_DEP_CODIGO)));
                departamento.setNombre(cursor.getString(cursor.getColumnIndex(COL_DEP_NOMBRE)));
                departamento.setEncargado(cursor.getString(cursor.getColumnIndex(COL_DEP_ENCARGADO)));
                byte[] bytesImagen = cursor.getBlob(cursor.getColumnIndex(COL_DEP_IMAGEN));

                if(bytesImagen != null){
                    imagen = BitmapFactory.decodeByteArray(bytesImagen, 0, bytesImagen.length);
                } else {
                    imagen = BitmapFactory.decodeResource(this.context.getResources(),R.drawable.ic_outline_home_work_24);
                }

                departamento.setImagen(imagen);
                listaDepartamentos.add(departamento);
            }while(cursor.moveToNext());
            cursor.close();
            bd.close();
        }
        return listaDepartamentos;
    }

    public void añadirDepartamento(Departamento dep){
        SQLiteDatabase bd = this.getWritableDatabase();
        ContentValues datos = new ContentValues();
        datos.put(COL_DEP_CODIGO, dep.getCodigo());
        datos.put(COL_DEP_NOMBRE, dep.getNombre());
        datos.put(COL_DEP_ENCARGADO, dep.getEncargado());

        bd.insert(TABLA_DEPARTAMENTOS, null, datos);
        bd.close();
    }

    public boolean comprobarDepartamento(String codigo){

        SQLiteDatabase bd = this.getReadableDatabase();
        boolean depEncontrado = false;

        String query = "Select * from " + TABLA_DEPARTAMENTOS + " where " + COL_DEP_CODIGO + " = ?";

        String[] columnas = {COL_EMP_DNI};
        String consulta = COL_EMP_DNI  + " = ?";
        String[] argumentos = {codigo};

        Cursor cursor = bd.rawQuery(query, argumentos);

        if(cursor.getCount() >= 1){
            depEncontrado = true;
        }
        return depEncontrado;
    }

    @SuppressLint("Range")
    public Departamento datosDepartamento(String codigo){

        SQLiteDatabase bd = this.getReadableDatabase();
        Departamento departamento =  null;

        String consulta = "Select * from " + TABLA_DEPARTAMENTOS  + " where " + COL_DEP_CODIGO + " = ?";
        String [] argumentos = {codigo};
        Log.i("testt", argumentos[0]);
        Cursor cursor = bd.rawQuery(consulta, argumentos);
        if(cursor.moveToFirst()){
            departamento = new Departamento();
            departamento.setCodigo(cursor.getString(cursor.getColumnIndex(COL_DEP_CODIGO)));
            departamento.setNombre(cursor.getString(cursor.getColumnIndex(COL_DEP_NOMBRE)));
            departamento.setEncargado(cursor.getString(cursor.getColumnIndex(COL_DEP_ENCARGADO)));
        }
        return departamento;
    }

    public void cambiarDatosDepartamento(String codigo, Departamento departamento){
        SQLiteDatabase bd = this.getWritableDatabase();

        ContentValues datos = new ContentValues();

        datos.put(COL_DEP_NOMBRE, departamento.getNombre());
        datos.put(COL_DEP_ENCARGADO, departamento.getEncargado());

        bd.update(TABLA_DEPARTAMENTOS, datos, COL_DEP_CODIGO + " = '" + codigo + "'", null);
    }

    public void eliminarDepartamento(String codigo){
        SQLiteDatabase bd = this.getWritableDatabase();

        bd.delete(TABLA_DEPARTAMENTOS, COL_DEP_CODIGO + " = " + "'" + codigo + "'", null);

    }

    public void guardarImagenDep(Bitmap imagenGuardar, String codigo){

        SQLiteDatabase bd = this.getWritableDatabase();

        baos = new ByteArrayOutputStream();
        imagenGuardar.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        bytesImagen = baos.toByteArray();

        ContentValues datos = new ContentValues();
        datos.put(COL_DEP_IMAGEN, bytesImagen);

        int rows = bd.update(TABLA_DEPARTAMENTOS, datos,COL_DEP_CODIGO + " = '"+ codigo + "'", null);

        Log.i("test2", String.valueOf(rows));
        bd.close();
    }

    public Bitmap obtenerImagenDep(String codigo){
        SQLiteDatabase bd = this.getReadableDatabase();
        Bitmap imagen = null;

        String consulta = "Select " + COL_DEP_IMAGEN + " from " + TABLA_DEPARTAMENTOS  + " where " + COL_DEP_CODIGO + " = ?";
        String [] argumentos = {codigo};

        Cursor cursor = bd.rawQuery(consulta, argumentos);

        if(cursor.moveToFirst()){
            byte[] bytesImagen = cursor.getBlob(0);
            if(bytesImagen != null){
                imagen = BitmapFactory.decodeByteArray(bytesImagen, 0, bytesImagen.length);
            }
        }
        return imagen;
    }

    public void establecerEmpleadoDep(String codDep, String dni){
        SQLiteDatabase bd = this.getWritableDatabase();

        ContentValues datos = new ContentValues();

        datos.put(COL_EMP_DPTO, codDep);

        bd.update(TABLA_EMPLEADOS, datos, COL_EMP_DNI + " = '" + dni + "'", null);
    }

    public String obtenerDepCod(String codigo){
        SQLiteDatabase bd = this.getReadableDatabase();

        String nombreDep = "";

        String consulta = "Select " + COL_DEP_NOMBRE + " from " + TABLA_DEPARTAMENTOS  + " inner join " + TABLA_EMPLEADOS + " on " + TABLA_EMPLEADOS + "." + COL_EMP_DPTO + " = " +
                TABLA_DEPARTAMENTOS + "." + COL_DEP_CODIGO  + " where " + COL_DEP_CODIGO + " = ?";
        String [] argumentos = {codigo};

        Cursor cursor = bd.rawQuery(consulta, argumentos);

        if(cursor.moveToFirst()){
            nombreDep = cursor.getString(0);
        }
        return nombreDep;
    }

    public void cambiarContraseña(String dni, String contraseña){
        SQLiteDatabase bd = this.getWritableDatabase();

        ContentValues datos = new ContentValues();

        datos.put(COL_EMP_CONTRASEÑA, contraseña);

        bd.update(TABLA_EMPLEADOS, datos, COL_EMP_DNI + " = '" + dni  + "'", null);
    }

    public void insertarFichaje(Fichaje fich){
        SQLiteDatabase bd = this.getWritableDatabase();

        ContentValues datos = new ContentValues();

        datos.put(COL_FIC_EMP, fich.getFichEmp());
        datos.put(COL_FIC_DIA, fich.getFichDia());
        datos.put(COL_FIC_INICIO, fich.getFichInicio());
        datos.put(COL_FIC_ACTIVO, fich.getFichActivo());

        bd.insert(TABLA_FICHAJES, null, datos);
        bd.close();
    }

    public void actualizarFichaje(Fichaje fich , String dni){
        SQLiteDatabase bd = this.getWritableDatabase();

        ContentValues datos = new ContentValues();

        datos.put(COL_FIC_FIN, fich.getFichFin());
        datos.put(COL_FIC_DIA_FIN, fich.getFichFinDia());
        datos.put(COL_FIC_ACTIVO, fich.getFichActivo());

        int rows = bd.update(TABLA_FICHAJES, datos, COL_FIC_EMP + " = '" + dni + "'" + " and " + COL_FIC_FIN  + " is NULL ", null);
        Log.i("testrows", String.valueOf(rows));
    }

    @SuppressLint("Range")
    public List<Fichaje> obtenerFichajes(String dni){

        List<Fichaje> listaFichajes = new ArrayList<>();

        SQLiteDatabase bd = this.getReadableDatabase();

        String[] columnas = {
                COL_FIC_COD, COL_FIC_EMP, COL_FIC_DIA, COL_FIC_INICIO, COL_FIC_FIN, COL_FIC_DIA_FIN, COL_FIC_ACTIVO
        };

        Cursor cursor = bd.query(TABLA_FICHAJES, columnas, COL_FIC_EMP + " = '" + dni + "'" , null, null, null, null);

        if(cursor.moveToFirst()){
            do {
                Fichaje fichaje = new Fichaje();
                fichaje.setFichCod(cursor.getInt(cursor.getColumnIndex(COL_FIC_COD)));
                fichaje.setFichEmp(cursor.getString(cursor.getColumnIndex(COL_FIC_EMP)));
                fichaje.setFichDia(cursor.getString(cursor.getColumnIndex(COL_FIC_DIA)));
                fichaje.setFichInicio(cursor.getString(cursor.getColumnIndex(COL_FIC_INICIO)));
                fichaje.setFichFin(cursor.getString(cursor.getColumnIndex(COL_FIC_FIN)));
                fichaje.setFichFinDia(cursor.getString(cursor.getColumnIndex(COL_FIC_DIA_FIN)));
                fichaje.setFichActivo(cursor.getInt(cursor.getColumnIndex(COL_FIC_ACTIVO)));

                listaFichajes.add(fichaje);
            }while(cursor.moveToNext());
            cursor.close();
            bd.close();
        }
        return listaFichajes;
    }
}

