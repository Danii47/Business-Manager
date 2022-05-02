package com.example.proyectotfgjavierlahoz.validadores;

import android.content.Context;
import android.util.Log;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;

public class ValidacionSesion {

    private Context context;

    public ValidacionSesion(Context context){
        this.context = context;
    }

    public boolean campoVacio (EditText editText, String mensaje){
        String valor = editText.getText().toString().trim();
        if(valor.isEmpty()){
            Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean comprobarCorreo(EditText editText, String mensaje){
        String valor = editText.getText().toString().trim();
        if(!Patterns.EMAIL_ADDRESS.matcher(valor).matches()){
            Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean comprobacionContraseña(EditText edtContraseña, EditText edtConfContraseña, String mensaje){
        String contraseña = edtContraseña.getText().toString().trim();
        String confContraseña = edtConfContraseña.getText().toString().trim();
        if(!contraseña.contentEquals(confContraseña)){
            Toast.makeText(context,mensaje, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean comprobarDni(EditText editText, String mensaje){
        String dni = editText.getText().toString().trim();
        String letraMayuscula;

        if(dni.length()!= 9 || Character.isLetter(dni.charAt(8)) == false){
            Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
            return false;
        }

        letraMayuscula = dni.substring(8).toUpperCase();

        if(comprobarNumerosDni(dni) == true && comprobarLetraDni(dni).equals(letraMayuscula)){
            return true;
        } else {
            Log.i("test2", "entra");
            Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean comprobarNumerosDni(String dni){
        String[] numeros = {"0","1","2","3","4","5","6","7","8","9"};
        String numero;
        String comprobacion = "";

        for(int i = 0; i < dni.length(); i++){
            numero = dni.substring(i, i+1);
            for (int j = 0; j < numeros.length - 1; j++){
                if(numero.equals(numeros[j])){
                    comprobacion += numeros[j];
                }
            }
        }

        if(comprobacion.length() != 8){
            return false;
        } else {
            return true;
        }
    }

    private String comprobarLetraDni(String dni){
        int resto;
        String[] letras = {"T","R","W","A","G","M","Y","F","P","D","X","B","N","J","Z","S","Q","V","H","L","C","K","E"};
        String letra;

        resto = Integer.parseInt(dni.substring(0,8)) % 23;
        letra = letras[resto];

        return letra;
    }
}
