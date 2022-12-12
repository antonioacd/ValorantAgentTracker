package com.example.proyectovalorant.activities;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.example.proyectovalorant.R;
import com.example.proyectovalorant.adapter.RecyclerAdapter;
import com.example.proyectovalorant.controller.DBAccess;
import com.example.proyectovalorant.model.Usuario;

public class ControladorUsuario extends AppCompatActivity {

    TextView txtNombre;
    TextView txtTelefono;
    TextView txtUsuario;
    TextView txtPass;
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

}
