package com.edwingonzalez.apiplaces;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.bumptech.glide.Glide;
import com.edwingonzalez.Clases.Noticia;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NewsActivity extends AppCompatActivity {
    TextView textViewTituloDetalle, textViewAutor, textViewLikes, textViewInfoDetalle;
    ImageButton imageButtonRegresar, imageButtonAddFavs, imageButtonCompartir;
    ImageView imageViewNoticiaDetalles;
    RequestQueue requestQueue;
    StringRequest stringRequest;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Mostrar el layout del loader
        setContentView(R.layout.activity_louder);

        progressBar = findViewById(R.id.progressBar);

        // Configurar la cola de solicitudes y realizar la solicitud GET
        requestQueue = Volley.newRequestQueue(this);

        String id = getIntent().getExtras().getString("id");
        String url = "https://p2.qr4me.net/search/?id=" + id;

        stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Cambiar al layout principal después de recibir la respuesta
                setContentView(R.layout.activity_news);

                // Vinculación de vistas
                textViewTituloDetalle = findViewById(R.id.textViewTituloDetalle);
                textViewAutor = findViewById(R.id.textViewAutor);
                textViewLikes = findViewById(R.id.textViewLikes);
                textViewInfoDetalle = findViewById(R.id.textViewInfoDetalle);
                imageViewNoticiaDetalles = findViewById(R.id.imageViewNoticiaDetalles);
                imageButtonRegresar = findViewById(R.id.imageButtonRegresar);
                imageButtonAddFavs = findViewById(R.id.imageButtonAddFavs);
                imageButtonCompartir = findViewById(R.id.imageButtonCompartir);

                // Configurar el botón regresar
                imageButtonRegresar.setOnClickListener(view -> finish());

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

                Noticia obj = new Noticia(NewsActivity.this);

                if (obj.existe(id)) {
                    imageButtonAddFavs.setImageResource(R.drawable.ic_estrella2);
                }

                imageButtonAddFavs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (obj.existe(id)) {
                            obj.setId(id);
                            obj.eliminar();
                            imageButtonAddFavs.setImageResource(R.drawable.ic_estrella1);
                        } else {
                            obj.setId(id);
                            obj.insertar();
                            imageButtonAddFavs.setImageResource(R.drawable.ic_estrella2);
                        }
                    }
                });

                imageButtonCompartir.setOnClickListener(v -> {
                    Bitmap bitmap = captureScreen();
                    String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Captura de pantalla", null);
                    Uri uri = Uri.parse(path);
                    shareImage(uri);
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Manejar el error (puedes mostrar un mensaje de error en la UI)
                error.printStackTrace();
            }
        });

        requestQueue.add(stringRequest);
    }

    //?---------------------Funcion para la captura de pantalla---------------------------\\
    private Bitmap captureScreen() {
        View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        rootView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(rootView.getDrawingCache());
        rootView.setDrawingCacheEnabled(false);
        return bitmap;
    }

    //?---------------------Funcion para compartir la captura captura---------------------------\\
    private void shareImage(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(intent, "Compartir imagen"));
    }
}