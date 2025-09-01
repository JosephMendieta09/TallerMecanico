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

        String mensajetx2 = getIntent().getStringExtra("mensajew");

        Bundle recibe = getIntent().getExtras();
        String mensajetx = null;
        String nombretx = null;
        if (recibe != null){
            mensajetx = recibe.getString("mensajito");
            nombretx = recibe.getString("nombrecito");
        }

        if (mensajetx != null){
            tvMensaje.setText(mensajetx);
        } else if (mensajetx2 != null) {
            tvMensaje.setText(mensajetx2);
        }

        if (nombretx != null){
            Nombre.setText(nombretx);
        }

        tvForgot = findViewById(R.id.tvForgot);
        tvForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent olvidar = new Intent(InicioSesionActivity.this, VerificacionActivity.class);
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