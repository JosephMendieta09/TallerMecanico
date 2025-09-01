package com.example.tallermecanico;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CambiarContraActivity extends AppCompatActivity {

    Button btncambio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_contra);

        btncambio = findViewById(R.id.btnContinue);
        btncambio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mensajef = "Contraseña Guardada";
                Intent intentmen = new Intent(CambiarContraActivity.this, InicioSesionActivity.class);
                intentmen.putExtra("mensajew", mensajef);
                startActivity(intentmen);
            }
        });
    }
}