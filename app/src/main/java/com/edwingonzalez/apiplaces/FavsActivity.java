package com.edwingonzalez.apiplaces;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupWindow;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.edwingonzalez.Funciones.Utilidades;

import java.util.Objects;

public class FavsActivity extends AppCompatActivity {
    ImageButton imageButtonHome, imageButtonCerrar;
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
    }

    //-------------------------------Botones--------------------------------\\
    @SuppressLint("ClickableViewAccessibility")
    private void setupListeners() {
        //Funcionamiento de los botones
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