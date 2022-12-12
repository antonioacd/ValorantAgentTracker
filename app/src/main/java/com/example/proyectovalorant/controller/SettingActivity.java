package com.example.proyectovalorant.controller;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

import com.example.proyectovalorant.R;
import com.example.proyectovalorant.fragments.SettingsFragment;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //Ponemos el fragment por encima de nuestro layout
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.setting_container, new SettingsFragment())
                .commit();

        //Activamos la opcion de ir hacia atras
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        //Cargamos las preferencias
        loadPreferences();
    }

    public void loadPreferences(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SettingActivity.this);
        boolean activo = sharedPreferences.getBoolean("tema", false);
        setDayNigth(activo);
    }

    public void setDayNigth(boolean modo){
        if (modo){
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else{
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    //Este metodo implementa el boton de regresar a la pagina anterior
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}