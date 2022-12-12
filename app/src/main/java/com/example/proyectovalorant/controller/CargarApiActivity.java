package com.example.proyectovalorant.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.view.ActionMode;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bumptech.glide.Glide;
import com.example.proyectovalorant.R;
import com.example.proyectovalorant.adapter.RecyclerAdapter;
import com.example.proyectovalorant.model.Objeto;

import io.github.muddz.styleabletoast.StyleableToast;

public class CargarApiActivity extends AppCompatActivity {

    //Variable user, en la cual almacenaremos la info extra recogida de la actividad anterior
    String user;

    //Creamos las variables necesarias para implementar el recyclerView
    ConstraintLayout constraintLayout;
    RecyclerView rV;
    RecyclerAdapter recAdapter;


    private androidx.appcompat.view.ActionMode mActionMode;
    int seleccionado;
    AlertDialog dialog;
    CircularProgressDrawable progressDrawable;
    int i;
    String imagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicioapp);
        constraintLayout = (ConstraintLayout) findViewById(R.id.cLayout);

        //Cogemos el intent
        Intent i = getIntent();
        //Cogemos la infomacion extra, que sera el usuario que ha sido registrado
        user = i.getStringExtra("user");
        //Creamos un objeto del recicler adapter
        recAdapter = new RecyclerAdapter(this);

        //LLamamos el metodo taskConecctions para realizar una consulta a la api
        new CargarApiActivity.taskConnections().execute("GET", "/agents?isPlayableCharacter=true");

        //Asignamos a la variable rV el recyclerView
        rV = (RecyclerView) findViewById(R.id.recView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(CargarApiActivity.this);
        rV.setLayoutManager(layoutManager);
        rV.setAdapter(recAdapter);

        recAdapter.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                seleccionado = rV.getChildAdapterPosition(view);

                mActionMode = startSupportActionMode(mActionCallback);

                view.setSelected(true);

                return true;
            }
        });

        recAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                seleccionado = rV.getChildAdapterPosition(view);

                Intent i = new Intent(CargarApiActivity.this, CargarApiDetalles.class);
                i.putExtra("id", recAdapter.listaObjetos.get(seleccionado).getId());

                startActivity(i);

                view.setSelected(true);

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        loadPreferences();

    }

    public void loadPreferences(){

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(CargarApiActivity.this);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_simple,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //int itemId = item.getItemId();

        Intent i = new Intent(CargarApiActivity.this, Ajustes.class);

        i.putExtra("user", user);

        startActivity(i);

        return true;
    }

    public void alertMod(int seleccionado){

        AlertDialog.Builder ventana = new AlertDialog.Builder(this);

        ventana.setTitle("Modificar datos");

        View v = getLayoutInflater().inflate(R.layout.custom_dialog, null);

        EditText eName, eDesc;
        ImageView ivImage;

        eName = v.findViewById(R.id.txtNombreDialog);
        eDesc = v.findViewById(R.id.txtDescDialog);
        ivImage = v.findViewById(R.id.imgDialog);

        Button aceptar = v.findViewById(R.id.btnAceptar);
        Button siguiente = v.findViewById(R.id.btnSiquiente);
        Button anterior = v.findViewById(R.id.btnAnterior);

        Context context = this;

        progressDrawable = new CircularProgressDrawable(this);
        progressDrawable.setStrokeWidth(10f);
        progressDrawable.setStyle(CircularProgressDrawable.LARGE);
        progressDrawable.setCenterRadius(30f);
        progressDrawable.start();

        i = 0;

        imagen = recAdapter.listaObjetos.get(i).getFotoId();;

        Glide.with(context)
                .load(imagen)
                .placeholder(progressDrawable)
                .error(R.mipmap.ic_launcher)
                .into(ivImage);

        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (i < recAdapter.listaObjetos.size()-1){
                    i++;
                    imagen = recAdapter.listaObjetos.get(i).getFotoId();
                    Glide.with(context)
                            .load(imagen)
                            .placeholder(progressDrawable)
                            .error(R.mipmap.ic_launcher)
                            .into(ivImage);
                }

            }
        });

        anterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (i > 0){
                    i--;
                    imagen = recAdapter.listaObjetos.get(i).getFotoId();
                    Glide.with(context)
                            .load(imagen)
                            .placeholder(progressDrawable)
                            .error(R.mipmap.ic_launcher)
                            .into(ivImage);
                }

            }
        });

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recAdapter.listaObjetos.get(seleccionado).setTitulo(eName.getText().toString());
                recAdapter.listaObjetos.get(seleccionado).setDescripcion(eDesc.getText().toString());
                recAdapter.listaObjetos.get(seleccionado).setFotoId(imagen);
                recAdapter.notifyDataSetChanged();
                showToast("Agente modificado", R.style.toastModificar);
                dialog.dismiss();
            }
        });

        ventana.setView(v);

        dialog = ventana.create();

        dialog.show();

    }

    public void alertAdd(){

        AlertDialog.Builder ventana = new AlertDialog.Builder(this);

        ventana.setTitle("Añadir Agente");

        View v = getLayoutInflater().inflate(R.layout.custom_dialog, null);

        EditText eName, eDesc;
        ImageView ivImage;

        eName = v.findViewById(R.id.txtNombreDialog);
        eDesc = v.findViewById(R.id.txtDescDialog);
        ivImage = v.findViewById(R.id.imgDialog);

        Button aceptar = v.findViewById(R.id.btnAceptar);
        Button siguiente = v.findViewById(R.id.btnSiquiente);
        Button anterior = v.findViewById(R.id.btnAnterior);

        Context context = this;

        progressDrawable = new CircularProgressDrawable(this);
        progressDrawable.setStrokeWidth(10f);
        progressDrawable.setStyle(CircularProgressDrawable.LARGE);
        progressDrawable.setCenterRadius(30f);
        progressDrawable.start();

        i = 0;

        imagen = recAdapter.listaObjetos.get(i).getFotoId();;

        Glide.with(context)
                .load(imagen)
                .placeholder(progressDrawable)
                .error(R.mipmap.ic_launcher)
                .into(ivImage);

        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (i < recAdapter.listaObjetos.size()-1){
                    i++;
                    imagen = recAdapter.listaObjetos.get(i).getFotoId();
                    Glide.with(context)
                            .load(imagen)
                            .placeholder(progressDrawable)
                            .error(R.mipmap.ic_launcher)
                            .into(ivImage);
                }

            }
        });

        anterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (i > 0){
                    i--;
                    imagen = recAdapter.listaObjetos.get(i).getFotoId();
                    Glide.with(context)
                            .load(imagen)
                            .placeholder(progressDrawable)
                            .error(R.mipmap.ic_launcher)
                            .into(ivImage);
                }

            }
        });

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Objeto o = new Objeto(null,eName.getText().toString(),eDesc.getText().toString(), imagen);

                recAdapter.listaObjetos.add(o);

                recAdapter.notifyDataSetChanged();

                showToast("Agente añadido al final de la lista", R.style.toastCorrecto);

                dialog.dismiss();
            }
        });

        ventana.setView(v);

        dialog = ventana.create();

        dialog.show();

    }

    private class taskConnections extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... strings) {
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
                Log.d("D", "La s es: " + s);
                if(s != null){
                    Log.d("D","DATOS: "+s);

                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    String id = "";
                    String name = "";
                    String descripcion = "";
                    String urlImagen = "";
                    String tipo = "";

                    for(int i=0; i<jsonArray.length(); i++){

                        id = jsonArray.getJSONObject(i).getString("uuid");
                        name = jsonArray.getJSONObject(i).getString("displayName");
                        descripcion = jsonArray.getJSONObject(i).getString("description");
                        urlImagen = jsonArray.getJSONObject(i).getString("displayIcon");

                        Objeto o = new Objeto(id, name, descripcion, urlImagen);

                        recAdapter.insertarItem(o);

                        Log.d("A", "Objeto añadido");
                    }
                    Log.d("D", "To String: " + recAdapter.listaObjetos.toString());
                    recAdapter.notifyDataSetChanged();

                }else{
                    Toast.makeText(CargarApiActivity.this, "Problema al cargar los datos", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private ActionMode.Callback mActionCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.action_menu,menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int itemId = item.getItemId();
            switch(itemId){
                case R.id.itemAnadir:
                    alertAdd();
                    mode.finish();
                    break;
                case R.id.itemBorrar:
                    recAdapter.deleteItem(seleccionado);
                    showToast("Agente borrado", R.style.toastModificar);
                    mode.finish();
                    break;
                case R.id.itemModificar:
                    alertMod(seleccionado);
                    mode.finish();
                    break;
            }

            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
        }
    };

    public void showToast(String msg, int style){

        StyleableToast.makeText(this, msg, Toast.LENGTH_LONG, style).show();

    }

}