package com.example.proyectovalorant.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.example.proyectovalorant.R;
import com.example.proyectovalorant.model.Usuario;

public class ControladorUsuario extends AppCompatActivity {

    TextView txtNombre;
    TextView txtTelefono;
    TextView txtUsuario;
    TextView txtPass;

    TextView lblNombre;
    TextView lblTelefono;
    TextView lblUsuario;
    TextView lblPass;
    Button visible;
    ImageView imagen;

    CircularProgressDrawable progressDrawable;

    DBAccess dba = new DBAccess(this);

    String usuario;
    boolean oculta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ajustes_usuario);

        txtNombre = (TextView) findViewById(R.id.txtNombreUser);
        txtTelefono = (TextView) findViewById(R.id.txtTelefonoUser);
        txtUsuario = (TextView) findViewById(R.id.txtUsuarioUser);
        txtPass = (TextView) findViewById(R.id.txtPassUser);
        visible = (Button) findViewById(R.id.btnVerPassword);
        imagen = (ImageView) findViewById(R.id.imagenDatos);

        lblNombre = (TextView) findViewById(R.id.lblNombre);
        lblTelefono = (TextView) findViewById(R.id.lblTelefono);
        lblUsuario = (TextView) findViewById(R.id.lblUsuario);
        lblPass = (TextView) findViewById(R.id.lblPass);

        Intent i = getIntent();

        usuario = i.getStringExtra("user");

        Usuario u = dba.getDataUser(usuario);

        txtNombre.setText(u.getNombre());
        txtTelefono.setText(u.getTelefono());
        txtUsuario.setText(u.getUsuario());
        txtPass.setText(u.getContraseña());

        progressDrawable = new CircularProgressDrawable(this);
        progressDrawable.setStrokeWidth(10f);
        progressDrawable.setStyle(CircularProgressDrawable.LARGE);
        progressDrawable.setCenterRadius(30f);
        progressDrawable.start();

        Glide.with(this)
                .load("https://cdn-icons-png.flaticon.com/512/4803/4803145.png")
                .placeholder(progressDrawable)
                .error(R.mipmap.ic_launcher)
                .into(imagen);

        oculta = true;

        visible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (oculta == true){
                    txtPass.setInputType(InputType.TYPE_CLASS_TEXT);
                    visible.setBackground(getDrawable(R.drawable.ver));
                    oculta = false;
                }else{
                    txtPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    visible.setBackground(getDrawable(R.drawable.ojo));
                    oculta = true;
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        loadPreferences();

    }


    public void loadPreferences(){

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ControladorUsuario.this);
        boolean activo = sharedPreferences.getBoolean("tema", false);
        Log.d("H", "Devuelve: " + activo);

        setDayNigth(activo);

    }

    public void setDayNigth(boolean modo){

        if (modo){

            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);

            lblNombre.setTextColor(Color.rgb(255,255,255));
            lblTelefono.setTextColor(Color.rgb(255,255,255));
            lblUsuario.setTextColor(Color.rgb(255,255,255));
            lblPass.setTextColor(Color.rgb(255,255,255));


        }else{

            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        }
    }

}
