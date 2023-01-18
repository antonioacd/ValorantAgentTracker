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

        //Creamos un LinearLayout para establecer el Layout del recyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(CargarApiActivity.this);
        rV.setLayoutManager(layoutManager);

        //Implementamos el recyclerAdapter en el recyclerView
        rV.setAdapter(recAdapter);

        //Listener del recicler adapter, para que cuando realice una pulsacion prolongada sobre alguno
        //de sus elementos, se active
        recAdapter.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                //Capturamos el indice del elemento seleccionado
                seleccionado = rV.getChildAdapterPosition(view);

                //Activamos el actionMenu
                mActionMode = startSupportActionMode(mActionCallback);

                //Indicamos que se ha seleccionado un elemento de una vista
                view.setSelected(true);

                return true;
            }
        });

        //Listener del recicler adapter, para que cuando realice una pulsacion sobre alguno
        //de sus elementos, se active
        recAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Capturamos el indice del elemento seleccionado
                seleccionado = rV.getChildAdapterPosition(view);

                //Iniciamos la nueva actividad, que seria la vista maestra del elemento
                Intent i = new Intent(CargarApiActivity.this, CargarApiDetallesActivity.class);
                //Introducimos comoo srting extra el id de elemento seleccionado, para mas tarde
                //en esta clase de vista maestra poder realizar una consulta a la appi sobre
                //este mismo elemento y no tener que cargar todos de nuevo
                i.putExtra("id", recAdapter.listaObjetos.get(seleccionado).getId());
                startActivity(i);

                //Indicamos que se ha seleccionado un elemento de la vista
                view.setSelected(true);

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        //Cargamos las preferencias
        loadPreferences();
    }

    //Metodo loadPreferences que se usa para cargar las preferencias establecidas en la aplicacion
    public void loadPreferences(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(CargarApiActivity.this);
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

    //Metodo para implementar el menu en la vista
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_simple,menu);

        return true;
    }

    //Metodo para ejecutar una accion cuando se pulsa un elemento del menu, como en este caso
    //solamente tenemos un elemento, no es necesario utilizar un switch
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        //Abrimos la Actividad Ajustes
        Intent i = new Intent(CargarApiActivity.this, AjustesActivity.class);

        //Le pasamos el usuario que anteriormente habiamos recibido para que pueda consultarlo
        //y asi mostrarlo
        i.putExtra("user", user);

        startActivity(i);

        return true;
    }

    //Creamos un AlertDialog modificado para poder modificar los elementos de la vista,
    //los cuales no se actualizaran en la api como es obvio ya que no tenemos acceso,
    //pero almenos podemos tener una aproximacion visual de como seria
    public void alertMod(int seleccionado){

        //Creamos el alertBuilder para poder modificar el AlertDialog
        AlertDialog.Builder ventana = new AlertDialog.Builder(this);

        //Le ponemos un titulo
        ventana.setTitle("Modificar datos");

        //Asignamos a la vista el dialog modificado
        View v = getLayoutInflater().inflate(R.layout.custom_dialog, null);

        //Creamos los elementos necesarios
        EditText eName, eDesc;
        ImageView ivImage;

        //Asignamos los elementos a nuestro xml
        eName = v.findViewById(R.id.txtNombreDialog);
        eDesc = v.findViewById(R.id.txtDescDialog);
        ivImage = v.findViewById(R.id.imgDialog);
        Button aceptar = v.findViewById(R.id.btnAceptar);
        Button siguiente = v.findViewById(R.id.btnSiquiente);
        Button anterior = v.findViewById(R.id.btnAnterior);

        //Inicializamos el progressDrawable para usarlo en el glide
        progressDrawable = new CircularProgressDrawable(getApplicationContext());
        progressDrawable.setStrokeWidth(10f);
        progressDrawable.setStyle(CircularProgressDrawable.LARGE);
        progressDrawable.setCenterRadius(30f);
        progressDrawable.start();

        //Esta variable i nos sierve como contador para coger el url de las imagenes
        i = 0;
        imagen = recAdapter.listaObjetos.get(i).getFotoId();;

        Glide.with(getApplicationContext())
                .load(imagen)
                .placeholder(progressDrawable)
                .error(R.mipmap.ic_launcher)
                .into(ivImage);

        //Listener del boton siguiente, que servira para pasar a la siguiente imagen
        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (i < recAdapter.listaObjetos.size()-1){
                    i++;
                    imagen = recAdapter.listaObjetos.get(i).getFotoId();
                    Glide.with(getApplicationContext())
                            .load(imagen)
                            .placeholder(progressDrawable)
                            .error(R.mipmap.ic_launcher)
                            .into(ivImage);
                }

            }
        });

        //Listener del boton anterior, que servira para volver a la imagen anterior
        anterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (i > 0){
                    i--;
                    imagen = recAdapter.listaObjetos.get(i).getFotoId();
                    Glide.with(getApplicationContext())
                            .load(imagen)
                            .placeholder(progressDrawable)
                            .error(R.mipmap.ic_launcher)
                            .into(ivImage);
                }

            }
        });

        //Listener del boton aceptar, que servira para guardar los cambios
        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recAdapter.modItem(seleccionado, imagen, eName.getText().toString(), eDesc.getText().toString());
                showToast("Agente modificado", R.style.toastModificar);
                dialog.dismiss();
            }
        });

        //Se añade el AlertDialog a la vista
        ventana.setView(v);
        //Se crea el AlertDialog, y se muestra con .show
        dialog = ventana.create();
        dialog.show();

    }

    //Este metodo creara otro AlertDialog Modificado, en este caso para añadir agentes
    //es muy similar al anterior
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

    //Este metodo sera el encargado de realizar las peticiones a la app
    //y devolver los valores que deseamos
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
                if(s != null){

                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    //Creamos las variables necesarias para introducirlas en los campos
                    String id = "";
                    String name = "";
                    String descripcion = "";
                    String urlImagen = "";

                    //Recorremos nuestroArray que lleva los atrbutos de los agentes
                    //que ha obtenido en la consulta
                    for(int i=0; i<jsonArray.length(); i++){

                        id = jsonArray.getJSONObject(i).getString("uuid");
                        name = jsonArray.getJSONObject(i).getString("displayName");
                        descripcion = jsonArray.getJSONObject(i).getString("description");
                        urlImagen = jsonArray.getJSONObject(i).getString("displayIcon");

                        //Creamos el objeto y lo añadimos a nuestro recliclerAdapter
                        Objeto o = new Objeto(id, name, descripcion, urlImagen);
                        recAdapter.insertarItem(o);
                    }
                    //Notificamos que se han realizado cambios
                    recAdapter.notifyDataSetChanged();

                }else{
                    showToast("Problema al cargar los datos", R.style.toastIncorrecto);
                }

            } catch (JSONException e) {
                showToast("Problema al cargar los datos", R.style.toastIncorrecto);
            }
        }
    }

    //Implementamos el menu de accion, que se activara cuando mantengamos pulsado un elemento
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

        //Segun el item del menu que pulsmos se ejecutara una accion
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int itemId = item.getItemId();
            switch(itemId){
                case R.id.itemAnadir:
                    //En el caso del añadir, llamaremos al AlertDialog creado anteriormente
                    //para añadir un nuevo agente
                    alertAdd();
                    mode.finish();
                    break;
                case R.id.itemBorrar:
                    //Si pulsamos el item borrar, se borrará el elemento seleccionado
                    recAdapter.deleteItem(seleccionado);
                    showToast("Agente borrado", R.style.toastModificar);
                    mode.finish();
                    break;
                case R.id.itemModificar:
                    //En el caso de pulsar modificar, abrirá el AlertDialog para modificar clientes
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

    //Metodo que implementa la biblioteca externa styleabletoast para dar formato a los toast
    public void showToast(String msg, int style){
        StyleableToast.makeText(this, msg, Toast.LENGTH_LONG, style).show();
    }

}