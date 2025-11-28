package com.example.tallermecanico;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class FormClienteActivity extends AppCompatActivity {

    private TextInputEditText edtNombre, edtCarnet, edtDireccion, edtCorreo, edtTelefono;
    private Button btnGuardar, btnCancelar;
    private TextView tvTitulo;
    private DBTaller dbTaller;

    private boolean esEdicion = false;
    private int idCliente = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cliente);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        dbTaller = new DBTaller(this);

        // Inicializar vistas
        tvTitulo = findViewById(R.id.tvTituloForm);
        edtNombre = findViewById(R.id.edtNombreCliente);
        edtCarnet = findViewById(R.id.edtCarnet);
        edtDireccion = findViewById(R.id.edtDireccion);
        edtCorreo = findViewById(R.id.edtCorreoCliente);
        edtTelefono = findViewById(R.id.edtTelefonoCliente);
        btnGuardar = findViewById(R.id.btnGuardarCliente);
        btnCancelar = findViewById(R.id.btnCancelar);

        // Verificar si es edición o nuevo registro
        if (getIntent().hasExtra("cliente_id")) {
            esEdicion = true;
            cargarDatosCliente();
            tvTitulo.setText("Modificar Cliente");
            btnGuardar.setText("Actualizar Cliente");
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Modificar Cliente");
            }
            // Deshabilitar edición del carnet
            edtCarnet.setEnabled(false);
        } else {
            tvTitulo.setText("Nuevo Cliente");
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Nuevo Cliente");
            }
        }

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (esEdicion) {
                    actualizarCliente();
                } else {
                    guardarCliente();
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

    private void cargarDatosCliente() {
        idCliente = getIntent().getIntExtra("cliente_id", -1);
        String nombre = getIntent().getStringExtra("cliente_nombre");
        int carnet = getIntent().getIntExtra("cliente_carnet", 0);
        String direccion = getIntent().getStringExtra("cliente_direccion");
        String correo = getIntent().getStringExtra("cliente_correo");
        String telefono = getIntent().getStringExtra("cliente_telefono");

        edtNombre.setText(nombre);
        edtCarnet.setText(String.valueOf(carnet));
        edtDireccion.setText(direccion);
        edtCorreo.setText(correo);
        edtTelefono.setText(telefono);
    }

    private void guardarCliente() {
        String nombre = edtNombre.getText().toString().trim();
        String carnetStr = edtCarnet.getText().toString().trim();
        String direccion = edtDireccion.getText().toString().trim();
        String correo = edtCorreo.getText().toString().trim();
        String telefono = edtTelefono.getText().toString().trim();

        if (nombre.isEmpty() || carnetStr.isEmpty() || direccion.isEmpty() ||
                correo.isEmpty() || telefono.isEmpty()) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        int carnet = Integer.parseInt(carnetStr);
        Cliente cliente = new Cliente(nombre, carnet, direccion, correo, telefono);

        if (dbTaller.insertarCliente(cliente)) {
            Toast.makeText(this, "Cliente registrado exitosamente", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error al registrar cliente", Toast.LENGTH_SHORT).show();
        }
    }

    private void actualizarCliente() {
        String nombre = edtNombre.getText().toString().trim();
        String carnetStr = edtCarnet.getText().toString().trim();
        String direccion = edtDireccion.getText().toString().trim();
        String correo = edtCorreo.getText().toString().trim();
        String telefono = edtTelefono.getText().toString().trim();

        if (nombre.isEmpty() || carnetStr.isEmpty() || direccion.isEmpty() ||
                correo.isEmpty() || telefono.isEmpty()) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        int carnet = Integer.parseInt(carnetStr);
        Cliente cliente = new Cliente(idCliente, nombre, carnet, direccion, correo, telefono);

        if (dbTaller.actualizarCliente(cliente)) {
            Toast.makeText(this, "Cliente actualizado exitosamente", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error al actualizar cliente", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}