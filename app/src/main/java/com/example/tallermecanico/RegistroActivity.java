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

import java.util.Date;

public class RegistroActivity extends AppCompatActivity {

    Button Registro;
    TextView tvHave;
    EditText EdNombre, EdApellido, EdTelefono, EdCorreo, EdFecha, EdPassword, EdConfirmar;
    DBTaller dbTaller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        EdNombre = findViewById(R.id.edtName);
        EdApellido = findViewById(R.id.edtApellido);
        EdTelefono = findViewById(R.id.edtPhone);
        EdCorreo = findViewById(R.id.edtMail);
        EdFecha = findViewById(R.id.edtDate);
        EdPassword = findViewById(R.id.edtPassword);
        EdConfirmar = findViewById(R.id.edtPasswordConfi);
        dbTaller = new DBTaller(this);
        Registro = findViewById(R.id.btnNext);
        Registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = EdNombre.getText().toString();
                String apellido = EdApellido.getText().toString();
                String telefono = EdTelefono.getText().toString();
                String correo = EdCorreo.getText().toString();
                String fecha_nac = EdFecha.getText().toString();
                String contrasena = EdPassword.getText().toString();
                String confirmar = EdConfirmar.getText().toString();

                if (!contrasena.equals(confirmar)) {
                    Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden", Toast.LENGTH_LONG).show();
                    return;
                }

                Usuario usuario = new Usuario(nombre, apellido, correo, contrasena, telefono, fecha_nac);
                //Llamada y respuesta de la insercion con SQLite
                boolean exito = dbTaller.insertarUsuario(usuario);

                if (exito) {
                    Toast.makeText(getApplicationContext(), "Registro exitoso", Toast.LENGTH_LONG).show();
                    String mensaje = "Cuenta Creada, Verifica tu Correo";
                    Intent intent = new Intent(RegistroActivity.this, VerificacionActivity.class);
                    intent.putExtra("correo", usuario.getEmail());
                    intent.putExtra("mensaje", mensaje);
                    intent.putExtra("modo", "registrar");
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Error al registrar", Toast.LENGTH_LONG).show();
                }
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