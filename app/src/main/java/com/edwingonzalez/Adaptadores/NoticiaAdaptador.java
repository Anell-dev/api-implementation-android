package com.edwingonzalez.Adaptadores;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.edwingonzalez.Clases.Noticia;
import com.edwingonzalez.apiplaces.MainActivity;
import com.edwingonzalez.apiplaces.NewsActivity;
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



        holder.textViewTitulo.setText( lista.get(position).getTitulo());
        holder.textViewInfoDetalles.setText(lista.get(position).getDetalles());
        Glide.with(context).load(lista.get(position).getImagen()).into(holder.imageViewNoticia);

        holder.imageViewNoticia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NewsActivity.class);
                intent.putExtra("id", lista.get(position).getId());
                context.startActivity(intent);
            }
        });
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
