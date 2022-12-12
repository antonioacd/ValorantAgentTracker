package com.example.proyectovalorant.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectovalorant.R;
import com.example.proyectovalorant.adapter.RecyclerAdapter;

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

                Intent e = new Intent(Ajustes.this, Preferencias.class);

                e.putExtra("user", user);

                startActivity(e);

                break;
        }
    }


}
