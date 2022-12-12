package com.example.proyectovalorant.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.example.proyectovalorant.R;
import com.example.proyectovalorant.model.Objeto;

import java.util.ArrayList;
import java.util.Locale;


/**
 *
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder>{

    public ArrayList<Objeto> listaObjetos;
    private CircularProgressDrawable progressDrawable;

    View.OnClickListener onClickListener;
    View.OnLongClickListener onLongClickListener;

    Context contexto;

    //Constructor de RecyclerAdapter
    public RecyclerAdapter(Context contexto) {
        this.contexto = contexto;
        listaObjetos = new ArrayList<Objeto>();
    }

    public void deleteItem(int seleccionado){

        Log.d("H", "delete: " +  listaObjetos.get(2).toString());

        listaObjetos.remove(seleccionado);
        this.notifyDataSetChanged();
        
    }

    public void insertarItem(Objeto o){

        listaObjetos.add(o);
        this.notifyDataSetChanged();

    }

    public void modItem(String id,String name, String desc){

        for (int i = 0; i < listaObjetos.size(); i++) {

            if (listaObjetos.get(i).getId().equals(id)){

                listaObjetos.get(i).setTitulo(name);
                listaObjetos.get(i).setDescripcion(desc);

            }
        }

        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_objeto,parent, false);
        RecyclerHolder recyclerHolder = new RecyclerHolder(view);

        view.setOnClickListener(onClickListener);
        view.setOnLongClickListener(onLongClickListener);

        return recyclerHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerHolder holder, int position) {

        progressDrawable = new CircularProgressDrawable(contexto);
        progressDrawable.setStrokeWidth(10f);
        progressDrawable.setStyle(CircularProgressDrawable.LARGE);
        progressDrawable.setCenterRadius(30f);
        progressDrawable.start();

        Objeto objeto = listaObjetos.get(position);
        holder.txtViewDesc.setText(objeto.getDescripcion());
        holder.txtViewTitle.setText(objeto.getTitulo());
        Glide.with(contexto)
                .load(objeto.getFotoId())
                .placeholder(progressDrawable)
                .error(R.mipmap.ic_launcher)
                .into(holder.img);
    }

    @Override
    public int getItemCount() {
        return listaObjetos.size();
    }

    public class RecyclerHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView  txtViewTitle;
        TextView  txtViewDesc;

        public RecyclerHolder(@NonNull View itemView) {
            super(itemView);

            img  = (ImageView) itemView.findViewById(R.id.imageView);
            txtViewTitle = (TextView)  itemView.findViewById(R.id.txtTitulo);
            txtViewDesc  = (TextView)  itemView.findViewById(R.id.txtDescripcion);

        }
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setOnLongClickListener(View.OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }
}
