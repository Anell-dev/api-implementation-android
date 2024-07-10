package com.edwingonzalez.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.edwingonzalez.Clases.Noticia;
import com.edwingonzalez.apiplaces.R;

import java.util.ArrayList;

public class NoticiaAdaptador extends RecyclerView.Adapter<NoticiaAdaptador.ViewHolder> {

    Context context;
    ArrayList<Noticia> lista;

    public NoticiaAdaptador(Context context, ArrayList<Noticia> lista) {
        this.context = context;
        this.lista = lista;
    }

    @NonNull
    @Override
    public NoticiaAdaptador.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.noticia_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticiaAdaptador.ViewHolder holder, int position) {
        //? siempre poner la logica en este metodo




    }

    @Override
    public int getItemCount() {
        return lista.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        //? Variables que participan dentro de mi layout, inicializarlas y vincularlas
        TextView textViewTitulo, textViewInfoDetalles;
        ImageView imageViewNoticia;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitulo = itemView.findViewById(R.id.textViewTitulo);
            textViewInfoDetalles = itemView.findViewById(R.id.textViewInfoDetalles);

            imageViewNoticia = itemView.findViewById(R.id.imageViewNoticia);

        }
    }
}
