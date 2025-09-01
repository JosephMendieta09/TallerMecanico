package com.example.tallermecanico;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegistroActivity extends AppCompatActivity {

    Button Registro;
    TextView tvHave;
    EditText EdNombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        EdNombre = findViewById(R.id.edtName);
        Registro = findViewById(R.id.btnNext);
        Registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mensaje = "Registro Exitoso";
                String nombre = EdNombre.getText().toString();
                Bundle bundel = new Bundle();
                bundel.putString("mensajito", mensaje);
                bundel.putString("nombrecito", nombre);
                Intent regis = new Intent(RegistroActivity.this, InicioSesionActivity.class);
                regis.putExtras(bundel);
                startActivity(regis);
            }
        });

        tvHave = findViewById(R.id.tvHave);
        tvHave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent volver = new Intent(RegistroActivity.this, InicioSesionActivity.class);
                startActivity(volver);
            }
        });
    }
}