package com.example.proyectovalorant.controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.example.proyectovalorant.R;
import com.example.proyectovalorant.adapter.RecyclerAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import io.github.muddz.styleabletoast.StyleableToast;

public class CargarApiDetallesActivity extends AppCompatActivity {

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

        //contexto = this;

        new CargarApiDetallesActivity.taskConnections().execute("GET", "/agents/" + id);

        loadPreferences();

    }

    @Override
    protected void onResume() {
        super.onResume();
        //Cargamos las preferencias
        loadPreferences();
    }

    public void loadPreferences(){

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(CargarApiDetallesActivity.this);
        boolean activo = sharedPreferences.getBoolean("tema", false);
        setDayNigth(activo);
    }

    public void setDayNigth(boolean modo){
        if (modo){
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            txtTipo.setTextColor(Color.rgb(255,255,255));
            txtDesc.setTextColor(Color.rgb(255,255,255));
            txtDescTipo.setTextColor(Color.rgb(255,255,255));
            txtTitulo.setTextColor(Color.rgb(255,255,255));
        }else{
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    //Se encarga de realizar la consulta sobre la base de datos y obtener los datos que deseamos
    private class taskConnections extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... strings) {
            String result = null;
            switch (strings[0]){
                case "GET":
                    result = HttpConnectValorant.getRequest(strings[1]);
                    break;
                case "POST":
                    result = Integer.toString(HttpConnectValorant.postRequest(strings[1],strings[2]));
                    break;
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                if(s != null){

                    //Cogemos el objeto que ha obtenido
                    JSONObject object = new JSONObject(s);
                    //Cogemos el objeto del objeto que tenemos arriba con el Srting data
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

                        progressDrawable = new CircularProgressDrawable(getApplicationContext());
                        progressDrawable.setStrokeWidth(10f);
                        progressDrawable.setStyle(CircularProgressDrawable.LARGE);
                        progressDrawable.setCenterRadius(30f);
                        progressDrawable.start();

                        txtTitulo.setText(name);
                        txtDesc.setText(descripcion);

                        Glide.with(getApplicationContext())
                                .load(urlImagen)
                                .placeholder(progressDrawable)
                                .error(R.mipmap.ic_launcher)
                                .into(img);

                        Glide.with(getApplicationContext())
                                .load(urlImagen2)
                                .placeholder(progressDrawable)
                                .error(R.mipmap.ic_launcher)
                                .into(img2);

                        txtTipo.setText(tipo);
                        txtDescTipo.setText(descTipo);

                    recAdapter.notifyDataSetChanged();

                }else{
                    showToast("Problema al implementar los datos", R.style.toastIncorrecto);
                }

            } catch (JSONException e) {
                showToast("Problema al implementar los datos", R.style.toastIncorrecto);
            }
        }
    }

    //Metodo que implementa la biblioteca externa styleabletoast para dar formato a los toast
    public void showToast(String msg, int style){
        StyleableToast.makeText(this, msg, Toast.LENGTH_LONG, style).show();
    }


}


