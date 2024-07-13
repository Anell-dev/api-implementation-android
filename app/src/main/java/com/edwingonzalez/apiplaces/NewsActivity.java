package com.edwingonzalez.apiplaces;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.edwingonzalez.Clases.Noticia;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NewsActivity extends AppCompatActivity {
    TextView textViewTituloDetalle, textViewAutor, textViewLikes, textViewInfoDetalle;
    ImageButton imageButtonRegresar, imageButtonAddFavs;
    ImageView imageViewNoticiaDetalles;
    RequestQueue requestQueue;
    StringRequest stringRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_news);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //? Vinculación de vistas
        textViewTituloDetalle = findViewById(R.id.textViewTituloDetalle);
        textViewAutor = findViewById(R.id.textViewAutor);
        textViewLikes = findViewById(R.id.textViewLikes);
        textViewInfoDetalle = findViewById(R.id.textViewInfoDetalle);
        imageViewNoticiaDetalles = findViewById(R.id.imageViewNoticiaDetalles);
        imageButtonRegresar = findViewById(R.id.imageButtonRegresar);
        imageButtonAddFavs = findViewById(R.id.imageButtonAddFavs);


        //? Configurar el botón regresar
        imageButtonRegresar.setOnClickListener(view -> finish());


        //? Configurar la cola de solicitudes y realizar la solicitud GET
        requestQueue = Volley.newRequestQueue(this);


        String id = getIntent().getExtras().getString("id");

        String url = "https://p2.qr4me.net/search/?id="+id;

        stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    if (jsonArray.length() > 0) {
                        JSONObject news = jsonArray.getJSONObject(0);

                        String titulo = news.getString("titulo");
                        String autor = news.getString("autor");
                        String fecha = news.getString("fecha");
                        String imagen = news.getString("imagen");
                        String info = news.getString("detalles");

                        // Setear los campos en la UI
                        textViewTituloDetalle.setText(titulo);
                        textViewAutor.setText(autor);
                        textViewLikes.setText(fecha);
                        textViewInfoDetalle.setText(info);

                        // Usar Glide para cargar la imagen
                        Glide.with(NewsActivity.this)
                                .load(imagen)
                                .into(imageViewNoticiaDetalles);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(stringRequest);

        imageButtonAddFavs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // aqui
            }
        });
    }
}