package com.example.tallermecanico;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class VerificacionActivity extends AppCompatActivity {

    Button btnsend;
    EditText edtemail;
    TextView tvRecibido;
    DBTaller dbTaller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificacion);

        dbTaller = new DBTaller(this);
        edtemail = findViewById(R.id.edtEmail2);
        tvRecibido = findViewById(R.id.tvRecibido);

        String correoMen = getIntent().getStringExtra("correo");
        if (correoMen != null) {
            edtemail.setText(correoMen);
            tvRecibido.setText("Código enviado a tu correo");
        }

        btnsend = findViewById(R.id.btnSend);
        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String correo = edtemail.getText().toString().trim();

                if (correo.isEmpty()) {
                    Toast.makeText(VerificacionActivity.this, "Ingrese un correo", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (dbTaller.existeCorreo(correo)) {
                    Intent cambiar = new Intent(VerificacionActivity.this, CambiarContraActivity.class);
                    cambiar.putExtra("correo", correo);
                    startActivity(cambiar);
                } else {
                    Toast.makeText(VerificacionActivity.this, "El correo no está registrado", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}