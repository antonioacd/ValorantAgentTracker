package com.example.proyectovalorant.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.proyectovalorant.R;
import com.example.proyectovalorant.activities.CargarApiActivity;

public class LoginActivity extends AppCompatActivity {

    TextView txtUsuario;
    EditText txtContrasenia;
    Button btnIniciarSesion;
    Button btnRegistrar;
    Button btnVisible;
    DBAccess mDB;
    ImageView logo;
    CircularProgressDrawable progressDrawable;

    boolean oculta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciosesion);

        txtUsuario = (TextView) findViewById(R.id.txtUsuario);
        txtContrasenia = (EditText) findViewById(R.id.txtContrasenia);
        btnIniciarSesion = (Button) findViewById(R.id.btnIniciarSesion);
        btnRegistrar = (Button) findViewById(R.id.btnPagRegistro);
        logo = (ImageView) findViewById(R.id.imagenLogo);
        btnVisible = (Button) findViewById(R.id.btnVerPassword2);

        progressDrawable = new CircularProgressDrawable(this);
        progressDrawable.setStrokeWidth(10f);
        progressDrawable.setStyle(CircularProgressDrawable.LARGE);
        progressDrawable.setCenterRadius(30f);
        progressDrawable.start();

        txtContrasenia.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        //Insertamos la imagen del inicio utilizando la

        Glide.with(this)
                .load("https://yoolk.ninja/wp-content/uploads/2020/06/Games-Valorant-1024x1024.png")
                .placeholder(progressDrawable)
                .error(R.mipmap.ic_launcher)
                .into(logo);
        //https://seeklogo.com/images/V/valorant-logo-FAB2CA0E55-seeklogo.com.png
        mDB = new DBAccess(this);

        mDB.getVersionDB();

        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String user    = txtUsuario.getText().toString();
                String contrasenia   = txtContrasenia.getText().toString();

                int num = mDB.getUser(user, contrasenia);

                if(num == 0){

                    Intent i = new Intent(LoginActivity.this, CargarApiActivity.class);

                    i.putExtra("INFO", 1);
                    i.putExtra("user", user);

                    startActivity(i);


                }else if (num == -1){
                    Toast("Este usuario no se ha registrado");
                }else{
                    Toast("Contraseña incorrecta");
                }
            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(LoginActivity.this,RegistroActivity.class);

                startActivity(i);

            }
        });

        btnVisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (oculta == true){
                    txtContrasenia.setInputType(InputType.TYPE_CLASS_TEXT);
                    btnVisible.setBackground(getDrawable(R.drawable.ver));
                    oculta = false;
                }else{
                    txtContrasenia.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    btnVisible.setBackground(getDrawable(R.drawable.ojo));
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

        //Todo 4. Una vez creado todo, solo debemos de preocuparnos de acceder a la información,
        // ya  que Android se encarga del almacenamiento de los datos que introduce el usuario en
        // la ventana de preferencias.

        //Todo 4.1 Utilizamos PreferenceManager para obtener las preferencias compartidas de nuestra
        // aplicación. TENEIS QUE TENER EN CUENTA QUE ESTE ES EL MISMO PARA TODA LA APP (PATRÓN SINGLETON)


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
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

    public void Toast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}