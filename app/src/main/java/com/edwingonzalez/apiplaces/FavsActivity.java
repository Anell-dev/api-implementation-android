package com.edwingonzalez.apiplaces;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.edwingonzalez.Adaptadores.FavsAdaptador;
import com.edwingonzalez.Clases.Noticia;
import com.edwingonzalez.Funciones.Utilidades;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class FavsActivity extends AppCompatActivity {
    ImageButton imageButtonHome, imageButtonCerrar;
    FavsAdaptador adaptador;
    RequestQueue requestQueue;
    StringRequest stringRequest;
    String url = "https://p2.qr4me.net/";
    RecyclerView RecyclerViewNoticiasFavs;
    private PopupWindow tooltipWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_favs);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        setupViews();
        setupListeners();
    }

    //-------------------------------Views--------------------------------\\
    private void setupViews() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        imageButtonHome = findViewById(R.id.imageButtonHome);
        imageButtonCerrar = findViewById(R.id.imageButtonCerrar);
        RecyclerViewNoticiasFavs = findViewById(R.id.RecyclerViewNoticiasFavs);

        ArrayList<Noticia> noticias = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);
        stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        noticias.add(new Noticia(
                                jsonObject.getString("id"),
                                jsonObject.getString("titulo"),
                                jsonObject.getString("autor"),
                                jsonObject.getString("detalles"),
                                "",
                                jsonObject.getString("imagen"),
                                jsonObject.getString("icono")
                        ));
                    }
                    filterFavoriteNews(noticias);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(FavsActivity.this, "Error parsing data", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(FavsActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }

    private void filterFavoriteNews(ArrayList<Noticia> noticias) {
        Noticia ojb = new Noticia(FavsActivity.this);
        ArrayList<Noticia> noticiasFavsList = ojb.ListarTodos(); // Lista de noticias favoritas
        Set<String> setNoticiasFavsIds = new HashSet<>();
        for (Noticia noticiaFav : noticiasFavsList) {
            setNoticiasFavsIds.add(noticiaFav.getId());
        }

        ArrayList<Noticia> noticiasFavs = new ArrayList<>();
        for (Noticia noticia : noticias) {
            if (setNoticiasFavsIds.contains(noticia.getId())) {
                noticiasFavs.add(noticia);
            }
        }

        adaptador = new FavsAdaptador(this, noticiasFavs);
        RecyclerViewNoticiasFavs.setLayoutManager(new LinearLayoutManager(FavsActivity.this));
        RecyclerViewNoticiasFavs.setAdapter(adaptador);
        adaptador.notifyDataSetChanged();
    }

    //-------------------------------Botones--------------------------------\\
    @SuppressLint("ClickableViewAccessibility")
    private void setupListeners() {
        // Funcionamiento de los botones
        Utilidades.setTooltip(this, imageButtonHome, "Noticias");
        Utilidades.setTooltip(this, imageButtonCerrar, "Salir");

        imageButtonCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilidades.mostrarDialogoSalida(FavsActivity.this);
            }
        });

        imageButtonHome.setOnClickListener(view -> startActivity(new Intent(FavsActivity.this, MainActivity.class)));
    }
}
