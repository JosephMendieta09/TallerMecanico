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

public class RegistroActivity extends AppCompatActivity {

    Button Registro;
    TextView tvHave;
    EditText EdNombre, EdApellido, EdCorreo, EdPassword, EdConfirmar;
    DBTaller dbTaller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro);

        EdNombre = findViewById(R.id.edtName);
        EdApellido = findViewById(R.id.edtApellido);
        EdCorreo = findViewById(R.id.edtMail);
        EdPassword = findViewById(R.id.edtPassword);
        EdConfirmar = findViewById(R.id.edtPasswordConfi);
        Registro = findViewById(R.id.btnNext);
        dbTaller = new DBTaller(this);
        Registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = EdNombre.getText().toString();
                String apellido = EdApellido.getText().toString();
                String correo = EdCorreo.getText().toString();
                String contrasena = EdPassword.getText().toString();
                String confirmar = EdConfirmar.getText().toString();

                if (!contrasena.equals(confirmar)) {
                    Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    return;
                }

                Usuario usuario = new Usuario(nombre, apellido, correo, contrasena);
                //Llamada y respuesta de la insercion con SQLite
                boolean exito = dbTaller.insertarUsuario(usuario);

                if (exito) {
                    Toast.makeText(getApplicationContext(), "Registro exitoso", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),VerificacionActivity.class);
                    intent.putExtra("correo", usuario.getEmail());
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Error al registrar", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvHave = findViewById(R.id.tvHave);
        tvHave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent volver = new Intent(RegistroActivity.this, InicioSesionActivity.class);
                startActivity(volver);
            }
        });
    }
}