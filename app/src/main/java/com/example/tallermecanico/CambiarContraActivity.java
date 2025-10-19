package com.example.tallermecanico;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CambiarContraActivity extends AppCompatActivity {

    Button btncambio;
    EditText edtPassword2, edtPassword3;
    DBTaller dbTaller;
    String correoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_contra);

        dbTaller = new DBTaller(this);
        correoUsuario = getIntent().getStringExtra("correo");

        edtPassword2 = findViewById(R.id.edtPassword2);
        edtPassword3 = findViewById(R.id.edtPassword3);

        btncambio = findViewById(R.id.btnContinue);
        btncambio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nuevaContra = edtPassword2.getText().toString().trim();
                String confirmarContra = edtPassword3.getText().toString().trim();

                if (nuevaContra.isEmpty() || confirmarContra.isEmpty()) {
                    Toast.makeText(CambiarContraActivity.this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!nuevaContra.equals(confirmarContra)) {
                    Toast.makeText(CambiarContraActivity.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (nuevaContra.length() < 6) {
                    Toast.makeText(CambiarContraActivity.this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (dbTaller.actualizarContrasena(correoUsuario, nuevaContra)) {
                    Toast.makeText(CambiarContraActivity.this, "Contraseña actualizada exitosamente", Toast.LENGTH_SHORT).show();
                    Intent intentmen = new Intent(CambiarContraActivity.this, InicioSesionActivity.class);
                    intentmen.putExtra("mensajew", "Contraseña guardada exitosamente");
                    startActivity(intentmen);
                    finish();
                } else {
                    Toast.makeText(CambiarContraActivity.this, "Error al actualizar contraseña", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}