package com.example.tallermecanico;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class InicioSesionActivity extends AppCompatActivity {

    TextView tvForgot, tvRegistrar, tvMensaje;
    EditText Nombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);

        tvMensaje = findViewById(R.id.tvCambio);
        Nombre = findViewById(R.id.edtEmail);

        tvForgot = findViewById(R.id.tvForgot);
        tvForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mensaje = "Para cambiar Contraseña, Verifica tu Correo";
                Intent olvidar = new Intent(InicioSesionActivity.this, VerificacionActivity.class);
                olvidar.putExtra("mensajito", mensaje);
                olvidar.putExtra("modo", "cambiar");
                startActivity(olvidar);
            }
        });

        tvRegistrar = findViewById(R.id.tvRegister);
        tvRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registracion = new Intent(InicioSesionActivity.this, RegistroActivity.class);
                startActivity(registracion);
            }
        });
    }
}