package com.example.tallermecanico;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;

public class FormMecanicoActivity extends AppCompatActivity {

    private TextInputEditText edtNombre, edtCarnet, edtCorreo, edtTelefono;
    private Button btnGuardar, btnCancelar;
    private TextView tvTitulo;
    private DBTaller dbTaller;

    private boolean esEdicion = false;
    private int idMecanico = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_mecanico);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        dbTaller = new DBTaller(this);

        // Inicializar vistas
        tvTitulo = findViewById(R.id.tvTituloFormMecanico);
        edtNombre = findViewById(R.id.edtNombreMecanico);
        edtCarnet = findViewById(R.id.edtCarnetMecanico);
        edtCorreo = findViewById(R.id.edtCorreoMecanico);
        edtTelefono = findViewById(R.id.edtTelefonoMecanico);
        btnGuardar = findViewById(R.id.btnGuardarMecanico);
        btnCancelar = findViewById(R.id.btnCancelarMecanico);

        // Verificar si es edición o nuevo registro
        if (getIntent().hasExtra("mecanico_id")) {
            esEdicion = true;
            cargarDatosMecanico();
            tvTitulo.setText("Modificar Mecánico");
            btnGuardar.setText("Actualizar Mecánico");
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Modificar Mecánico");
            }
            // Deshabilitar edición del carnet
            edtCarnet.setEnabled(false);
        } else {
            tvTitulo.setText("Nuevo Mecánico");
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Nuevo Mecánico");
            }
        }

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (esEdicion) {
                    actualizarMecanico();
                } else {
                    guardarMecanico();
                }
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void cargarDatosMecanico() {
        idMecanico = getIntent().getIntExtra("mecanico_id", -1);
        String nombre = getIntent().getStringExtra("mecanico_nombre");
        int carnet = getIntent().getIntExtra("mecanico_carnet", 0);
        String correo = getIntent().getStringExtra("mecanico_correo");
        String telefono = getIntent().getStringExtra("mecanico_telefono");

        edtNombre.setText(nombre);
        edtCarnet.setText(String.valueOf(carnet));
        edtCorreo.setText(correo);
        edtTelefono.setText(telefono);
    }

    private void guardarMecanico() {
        String nombre = edtNombre.getText().toString().trim();
        String carnetStr = edtCarnet.getText().toString().trim();
        String correo = edtCorreo.getText().toString().trim();
        String telefono = edtTelefono.getText().toString().trim();

        if (nombre.isEmpty() || carnetStr.isEmpty() || correo.isEmpty() || telefono.isEmpty()) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        int carnet = Integer.parseInt(carnetStr);

        Mecanico mecanico = new Mecanico(nombre, carnet, correo, telefono);

        if (dbTaller.insertarMecanico(mecanico)) {
            Toast.makeText(this, "Mecánico registrado exitosamente", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error al registrar mecánico", Toast.LENGTH_SHORT).show();
        }
    }

    private void actualizarMecanico() {
        String nombre = edtNombre.getText().toString().trim();
        String carnetStr = edtCarnet.getText().toString().trim();
        String correo = edtCorreo.getText().toString().trim();
        String telefono = edtTelefono.getText().toString().trim();

        if (nombre.isEmpty() || carnetStr.isEmpty() || correo.isEmpty() || telefono.isEmpty()) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        int carnet = Integer.parseInt(carnetStr);

        Mecanico mecanico = new Mecanico(idMecanico, nombre, carnet, correo, telefono, "");

        if (dbTaller.actualizarMecanico(mecanico)) {
            Toast.makeText(this, "Mecánico actualizado exitosamente", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error al actualizar mecánico", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}