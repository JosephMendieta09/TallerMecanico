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

public class VerificacionActivity extends AppCompatActivity {

    Button btnsend;
    EditText edtemail;
    TextView tvmensaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificacion);

        edtemail = findViewById(R.id.edtEmail2);
        tvmensaje = findViewById(R.id.tvRecibido);
        btnsend = findViewById(R.id.btnSend);

        String correo = getIntent().getStringExtra("correo");
        String modo = getIntent().getStringExtra("modo");
        String mensaje1 = getIntent().getStringExtra("mensaje");
        String mensaje2 = getIntent().getStringExtra("mensajito");

        if ("registro".equals(modo)){
            edtemail.setText(correo);
            tvmensaje.setText(mensaje1);
            btnsend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent pasar = new Intent(VerificacionActivity.this, InicioSesionActivity.class);
                    startActivity(pasar);
                }
            });
        } else if ("cambiar".equals(modo)){
            tvmensaje.setText(mensaje2);
            btnsend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent pasar = new Intent(VerificacionActivity.this, CambiarContraActivity.class);
                    startActivity(pasar);
                }
            });
        }
    }
}