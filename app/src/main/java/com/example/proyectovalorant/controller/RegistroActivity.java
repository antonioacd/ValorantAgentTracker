package com.example.proyectovalorant.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectovalorant.R;

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

        Intent i = getIntent();
        //String msg = i.getStringExtra("INFO");

        mDB = new DBAccess(this);

        mDB.getVersionDB();

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = txtNombre.getText().toString();
                String telefono = txtTelefono.getText().toString();
                String user    = txtUsuarioR.getText().toString();
                String contrasenia   = txtContraseniaR.getText().toString();

                if(mDB.insert(nombre ,telefono ,user, contrasenia) != -1){
                    Toast("Se ha registrado correctamente");
                }else{
                    Toast("No se ha podido registrar");
                }

                Intent i = new Intent(RegistroActivity.this, LoginActivity.class);

                startActivity(i);
            }
        });
        //txtResultado.setText(msg);

    }

    public void Toast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
