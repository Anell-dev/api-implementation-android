package com.edwingonzalez.apiplaces;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.edwingonzalez.Adaptadores.NoticiaAdaptador;
import com.edwingonzalez.Clases.Noticia;
import com.edwingonzalez.Funciones.Utilidades;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    RecyclerView RecyclerViewNoticias;
    RequestQueue requestQueue;
    StringRequest stringRequest;
    NoticiaAdaptador adaptador;
    String url ="https://p.qr4me.net/";
    ImageButton imageButtonFavoritos, imageButtonCerrar;
    private PopupWindow tooltipWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        setupViews();
        setupListeners();
    }

    //?-------------------------------Views--------------------------------\\
    @SuppressLint("NotifyDataSetChanged")
    private void setupViews() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        imageButtonFavoritos = findViewById(R.id.imageButtonFavoritos);
        imageButtonCerrar = findViewById(R.id.imageButtonCerrar);


        RecyclerViewNoticias = findViewById(R.id.RecyclerViewNoticias);
        ArrayList<Noticia> noticias = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        stringRequest = new StringRequest(Request.Method.GET, url, s -> {
            try {
                JSONArray jsonArray = new JSONArray(s);
                for (int i=0; i<jsonArray.length();i++){
                    JSONObject jsonObject = new JSONObject(String.valueOf(jsonArray.getJSONObject(i)));
                    noticias.add( new Noticia(jsonObject.getString("id"),
                            jsonObject.getString("titulo"),
                            jsonObject.getString("autor"),
                            "",
                            0,
                            jsonObject.getString("imagen"),
                            jsonObject.getString("icono")  ));
                }
                adaptador.notifyDataSetChanged();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        }, volleyError -> {

        });
        requestQueue.add(stringRequest);

        adaptador = new NoticiaAdaptador(this, noticias);
        RecyclerViewNoticias.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        RecyclerViewNoticias.setAdapter(adaptador);

    }

    //?-------------------------------Botones--------------------------------\\
    @SuppressLint("ClickableViewAccessibility")
    private void setupListeners() {
        //? Funcionamiento de los botones
        Utilidades.setTooltip(this, imageButtonFavoritos, "Favoritos");
        Utilidades.setTooltip(this, imageButtonCerrar, "Salir");

        imageButtonCerrar.setOnClickListener(v -> Utilidades.mostrarDialogoSalida(MainActivity.this));

        imageButtonFavoritos.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, FavsActivity.class)));

    }
}

