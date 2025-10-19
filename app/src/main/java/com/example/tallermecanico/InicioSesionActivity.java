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

public class InicioSesionActivity extends AppCompatActivity {

    TextView tvForgot, tvRegistrar, tvMensaje;
    EditText edtEmail, edtPassword;
    Button btnLogin;
    DBTaller dbTaller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);

        tvMensaje = findViewById(R.id.tvCambio);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        dbTaller = new DBTaller(this);

        String mensaje = getIntent().getStringExtra("mensajew");
        if (mensaje != null && !mensaje.isEmpty()) {
            tvMensaje.setText(mensaje);
            tvMensaje.setVisibility(View.VISIBLE);
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String correo = edtEmail.getText().toString().trim();
                String contrasena = edtPassword.getText().toString().trim();

                if (correo.isEmpty() || contrasena.isEmpty()) {
                    Toast.makeText(InicioSesionActivity.this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (dbTaller.validarUsuario(correo, contrasena)) {
                    Toast.makeText(InicioSesionActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(InicioSesionActivity.this, HomeActivity.class);
                    intent.putExtra("correo", correo);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(InicioSesionActivity.this, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                }
            }
        });

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