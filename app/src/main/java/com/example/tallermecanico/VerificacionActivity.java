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

    Button btnsend, btnver;
    EditText edtemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificacion);

        edtemail = findViewById(R.id.edtEmail2);
        String correoMen = getIntent().getStringExtra("correo");
        edtemail.setText(correoMen);

        btnsend = findViewById(R.id.btnSend);
        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pasar = new Intent(VerificacionActivity.this, InicioSesionActivity.class);
                startActivity(pasar);
            }
        });

        btnver = findViewById(R.id.btnVer);
        btnver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ver = new Intent(VerificacionActivity.this, VerUsuarioActivity.class);
                startActivity(ver);
            }
        });
    }
}