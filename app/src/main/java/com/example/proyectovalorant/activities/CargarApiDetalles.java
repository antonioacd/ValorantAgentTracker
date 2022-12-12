package com.example.proyectovalorant.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.example.proyectovalorant.R;
import com.example.proyectovalorant.adapter.RecyclerAdapter;
import com.example.proyectovalorant.controller.HttpConnectValorant;

import org.json.JSONException;
import org.json.JSONObject;

public class CargarApiDetalles extends AppCompatActivity {

    RecyclerAdapter recAdapter;

    String id;

    TextView txtTitulo;
    TextView txtDesc;
    ImageView img;
    ImageView img2;
    TextView txtTipo;
    TextView txtDescTipo;
    Context contexto;

    CircularProgressDrawable progressDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles);

        txtTitulo = (TextView) findViewById(R.id.txtTituloDetalles);
        txtDesc = (TextView) findViewById(R.id.txtDescDetalles);
        img = (ImageView) findViewById(R.id.imgDetalles);
        img2 = (ImageView) findViewById(R.id.img2Detalles);
        txtTipo = (TextView) findViewById(R.id.txtTipoDetalles);
        txtDescTipo = (TextView) findViewById(R.id.txtDescTipoDetalles);

        recAdapter = new RecyclerAdapter(this);

        Intent i = getIntent();

        id = i.getStringExtra("id");

        contexto = this;

        new CargarApiDetalles.taskConnections().execute("GET", "/agents/" + id);

    }

    private class taskConnections extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... strings) {
            Log.d("Hola","Entra doInBack");
            String result = null;
            switch (strings[0]){
                case "GET":
                    result = HttpConnectValorant.getRequest(strings[1]);
                    Log.d("R", "Result: " + result + "strings[1]: " + strings[1]);
                    break;
                case "POST":
                    result = Integer.toString(HttpConnectValorant.postRequest(strings[1],strings[2]));
                    Log.d("R", "Result: " + result);
                    break;
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                if(s != null){

                    JSONObject object = new JSONObject(s);
                    
                    JSONObject objetoData = object.getJSONObject("data");

                    String name = "";
                    String descripcion = "";
                    String urlImagen = "";
                    String urlImagen2 = "";
                    String tipo = "";
                    String descTipo = "";

                        name = objetoData.getString("displayName");

                        descripcion = objetoData.getString("description");

                        urlImagen = objetoData.getString("displayIcon");

                        urlImagen2 = objetoData.getString("fullPortrait");

                        tipo = objetoData.getJSONObject("role").getString("displayName");

                        descTipo =objetoData.getJSONObject("role").getString("description");

                        //Todo 2.1 Configuración del CircularProgressDrawable
                        progressDrawable = new CircularProgressDrawable(contexto);
                        progressDrawable.setStrokeWidth(10f);
                        progressDrawable.setStyle(CircularProgressDrawable.LARGE);
                        progressDrawable.setCenterRadius(30f);
                        progressDrawable.start();

                        txtTitulo.setText(name);
                        txtDesc.setText(descripcion);
                        img.setBackground(contexto.getDrawable(R.drawable.degradado_morado));
                        Glide.with(contexto)
                                .load(urlImagen)
                                .placeholder(progressDrawable)
                                .error(R.mipmap.ic_launcher)
                                .into(img);

                        //img2.setBackground(contexto.getDrawable(R.drawable.degradado_morado));

                        //img2.set
                        Glide.with(contexto)
                                .load(urlImagen2)
                                .placeholder(progressDrawable)
                                .error(R.mipmap.ic_launcher)
                                .into(img2);

                        txtTipo.setText(tipo);
                        txtDescTipo.setText(descTipo);

                    recAdapter.notifyDataSetChanged();

                }else{
                    Toast.makeText(CargarApiDetalles.this, "Problema al cargar los datos", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}


