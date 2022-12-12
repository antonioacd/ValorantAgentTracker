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

import io.github.muddz.styleabletoast.StyleableToast;

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

        //Establecemos el timpo de texto del editText de la contraseña de tipo PASSWORD para que salgan los simbolos de puntos
        txtContrasenia.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        //Insertamos la imagen del inicio utilizando la biblioteca glide

        Glide.with(this)
                .load("https://yoolk.ninja/wp-content/uploads/2020/06/Games-Valorant-1024x1024.png")
                .placeholder(progressDrawable)
                .error(R.mipmap.ic_launcher)
                .into(logo);

        //Inicializamos el objeto mDB para tener acceso a los metodos de la clase DBAcces
        mDB = new DBAccess(this);

        //Obtenemos la versión
        mDB.getVersionDB();

        //Listener del boton Iniciar Sesion
        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Obtenemos el valor de los campos de usuario y contraseña
                String user    = txtUsuario.getText().toString();
                String contrasenia   = txtContrasenia.getText().toString();

                //llamamos al metodo, que comprobara si estan en la base de datos y si son correctos
                int num = mDB.getUser(user, contrasenia);

                //si nos devuelve un 0 estara correcto
                //si nos devuelve un -1 significa que el usuario no se ha registrado en la base de datos
                //si nos devuelve un 1 sera que la contraseña no coincide con el usuario
                if(num == 0){

                    //Si ha ido bien, llamamos a la siguiente actividad
                    Intent i = new Intent(LoginActivity.this, CargarApiActivity.class);

                    //Añadimos informacion extra que necesitaremos en la siguiente actividad
                    i.putExtra("INFO", 1);
                    i.putExtra("user", user);

                    startActivity(i);

                    showToast("Sesion Iniciada", R.style.toastCorrecto);

                }else if (num == -1){
                    showToast("Este usuario no se ha registrado", R.style.toastIncorrecto);
                }else{
                    showToast("Contraseña incorrecta", R.style.toastIncorrecto);
                }
            }
        });

        //Listener del boton registrar
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Abrimos la nueva actividad para registrar clientes
                Intent i = new Intent(LoginActivity.this,RegistroActivity.class);
                startActivity(i);
            }
        });

        //Listener del boton para ocultar o mostrar la contraseña (btnVisible)
        btnVisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Si ya esta oculta se cambiara a visible y al contrario
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

    //Metodo loadPreferences que se usa para cargar las preferencias establecidas en la aplicacion
    public void loadPreferences(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
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