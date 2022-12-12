package com.example.proyectovalorant.controller;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

import com.example.proyectovalorant.R;

public class Preferencias extends AppCompatActivity {

    Switch swi;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferencias);

        swi = (Switch) findViewById(R.id.tema);

        SharedPreferences sp = getSharedPreferences("SP", this.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        swi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (swi.isChecked()){
                    setDayNigth(0);
                }else{
                    setDayNigth(1);
                }

            }
        });
    }

    public void setDayNigth(int modo){

        if (modo == 0){

            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        }else{

            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        }
    }


}
