package com.example.proyectovalorant.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.service.controls.Control;
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
import com.example.proyectovalorant.controller.HttpConnectValorant;
import com.example.proyectovalorant.model.Objeto;

import kotlin.time.ExperimentalTime;

/**
 * Actividad peticiones REST: En esta actividad se verán las bases de las peticiones REST a través
 * de las aplicaciones Android
 * @author José Carlos Alfaro
 * @version 0.1
 *
 * CONSIDERACIONES PREVIAS:
 *  1. En este ejemplo se usa la vista ListView para simplificar el ejemplo. Se recomienda el uso
 *  de RecyclerView
 *
 *  2. Hay que tener en cuenta los permisos en Android Manifest, en este ejemplo es necesario
 *  el uso de internet.
 *
 *
 */
public class CargarApiActivity extends AppCompatActivity {

    String user;
    private ConstraintLayout constraintLayout;
    public RecyclerView rV;
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
        //Cogemos la infomacion extra
        user = i.getStringExtra("user");
        //Creamos un objeto del recicler adapter
        recAdapter = new RecyclerAdapter(this);

        new CargarApiActivity.taskConnections().execute("GET", "/agents?isPlayableCharacter=true");

        rV = (RecyclerView) findViewById(R.id.recView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(CargarApiActivity.this);
        rV.setLayoutManager(layoutManager);
        rV.setAdapter(recAdapter);

        recAdapter.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                Toast.makeText(CargarApiActivity.this,"Long", Toast.LENGTH_LONG);

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

    //Todo 1. Se sobreescribe el metodo onCreateOptionsMenu para indicar que nuestra app tendra
    // un menu personalizado.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Todo 1.1 Se usa un inflater para construir la vista y se pasa el menu por defecto para
        // que Android se encargue de colocarlo en la vista
        getMenuInflater().inflate(R.menu.menu_simple,menu);

        return true;
    }

    //Todo 2. Se sobreescribe el metodo onOptionsItemSelected para manejar las selecciones a través
    // de los diferentes item del menu.
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

                    //Todo 3. La respuesta que nos devuelve es un texto en formato JSON. Para ello,
                    // en este caso, haremos uso de las clases que nos proporciona Android. Antes
                    // que nada, se deberá consultar la documentación para conocer el formato de
                    // la respuesta del servidor, y así saber cómo deserializar el mensaje.

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
                    myToast("Agente añadido al final de la lista");
                    mode.finish();
                    break;
                case R.id.itemBorrar:
                    recAdapter.deleteItem(seleccionado);
                    myToast("Agente borrado");
                    mode.finish();
                    break;
                case R.id.itemModificar:
                    alertMod(seleccionado);
                    myToast("Agente modificado");
                    mode.finish();
                    break;
            }

            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
        }
    };

    public void myToast(String msg){

        Toast.makeText(this, msg, Toast.LENGTH_LONG);

    }

}