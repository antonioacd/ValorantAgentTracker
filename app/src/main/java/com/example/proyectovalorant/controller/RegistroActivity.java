package com.example.proyectovalorant.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

import com.example.proyectovalorant.R;

import io.github.muddz.styleabletoast.StyleableToast;

public class RegistroActivity extends AppCompatActivity {

    TextView txtNombre;
    TextView txtTelefono;
    TextView txtUsuarioR;
    TextView txtContraseniaR;
    Button btnRegistrar;
    DBAccess mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        btnRegistrar = (Button) findViewById(R.id.btnRegistrar);
        txtNombre = (TextView) findViewById(R.id.txtNombre);
        txtTelefono = (TextView) findViewById(R.id.txtTelefono);
        txtUsuarioR = (TextView) findViewById(R.id.txtUsuarioRegistro);
        txtContraseniaR = (TextView) findViewById(R.id.txtContraseniaRegistro);

        //Inicializamos el objeto mDB para tener acceso a los metodos de la clase DBAcces
        mDB = new DBAccess(this);

        //Obtenemos la versión
        mDB.getVersionDB();

        //Listener del boton Registrar
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = txtNombre.getText().toString();
                String telefono = txtTelefono.getText().toString();
                String user    = txtUsuarioR.getText().toString();
                String contrasenia   = txtContraseniaR.getText().toString();

                //Llamamos al metodo insert para añadir el usuario a la base de datos
                if(mDB.insert(nombre ,telefono ,user, contrasenia) != -1){
                    showToast("Se ha registrado correctamente", R.style.toastCorrecto);
                }else{
                    showToast("No se ha podido registrar", R.style.toastIncorrecto);
                }

                //Volvemos a la pagina anterior
                Intent i = new Intent(RegistroActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPreferences();
    }

    //Metodo loadPreferences que se usa para cargar las preferencias establecidas en la aplicacion
    public void loadPreferences(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(RegistroActivity.this);
        //La variable activo, recoge el estado del swich del fragment de preferencias para saber si esta activado o desactivado
        boolean activo = sharedPreferences.getBoolean("tema", false);
        setDayNigth(activo);
    }

    //Metodo para establecer el modo noche o modo normal, dependiendo de la variable "activo"
    public void setDayNigth(boolean modo){

        if (modo){
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else{
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    //Metodo que implementa una biblioteca externa para modificar los toast
    public void showToast(String msg, int style){
        StyleableToast.makeText(this, msg, Toast.LENGTH_LONG, style).show();
    }

}
