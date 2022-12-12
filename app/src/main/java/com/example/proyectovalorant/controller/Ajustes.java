package com.example.proyectovalorant.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

import com.example.proyectovalorant.R;

public class Ajustes extends AppCompatActivity implements View.OnClickListener{

    Button btnCuenta;
    Button btnPreferences;
    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ajustes);

        btnCuenta = (Button) findViewById(R.id.btnCuenta);
        btnPreferences = (Button) findViewById(R.id.btnPreferencias);

        Intent i = getIntent();

        user = i.getStringExtra("user");

        btnCuenta.setOnClickListener(this);
        btnPreferences.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnCuenta:

                Intent i = new Intent(Ajustes.this, ControladorUsuario.class);

                i.putExtra("user", user);

                startActivity(i);

                break;
            case R.id.btnPreferencias:

                Intent e = new Intent(Ajustes.this, SettingActivity.class);
                startActivity(e);

                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadPreferences();

    }

    public void loadPreferences(){

        //Todo 4. Una vez creado todo, solo debemos de preocuparnos de acceder a la información,
        // ya  que Android se encarga del almacenamiento de los datos que introduce el usuario en
        // la ventana de preferencias.

        //Todo 4.1 Utilizamos PreferenceManager para obtener las preferencias compartidas de nuestra
        // aplicación. TENEIS QUE TENER EN CUENTA QUE ESTE ES EL MISMO PARA TODA LA APP (PATRÓN SINGLETON)


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Ajustes.this);
        // Todo 4.2 Una vez tenemos acceso a las preferencias compartidas, solo debemos acceder mediante la clave para obtener su valor
        boolean activo = sharedPreferences.getBoolean("tema", false);
        Log.d("H", "Devuelve: " + activo);

        setDayNigth(activo);

    }

    public void setDayNigth(boolean modo){

        if (modo){

            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        }else{

            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        }
    }


}
