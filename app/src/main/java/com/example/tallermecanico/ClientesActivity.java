package com.example.tallermecanico;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ClientesActivity extends AppCompatActivity {

    private EditText edtNombre, edtCarnet, edtDireccion, edtCorreo, edtTelefono;
    private Button btnGuardarCliente;
    private RecyclerView recyclerClientes;
    private ClienteAdapter clienteAdapter;
    private List<Cliente> listaClientes;
    private DBTaller dbTaller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Gestión de Clientes");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        dbTaller = new DBTaller(this);

        edtNombre = findViewById(R.id.edtNombreCliente);
        edtCarnet = findViewById(R.id.edtCarnet);
        edtDireccion = findViewById(R.id.edtDireccion);
        edtCorreo = findViewById(R.id.edtCorreoCliente);
        edtTelefono = findViewById(R.id.edtTelefonoCliente);
        btnGuardarCliente = findViewById(R.id.btnGuardarCliente);

        recyclerClientes = findViewById(R.id.recyclerClientes);
        recyclerClientes.setLayoutManager(new LinearLayoutManager(this));

        cargarClientes();

        btnGuardarCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarCliente();
            }
        });
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
            limpiarCampos();
            cargarClientes();
        } else {
            Toast.makeText(this, "Error al registrar cliente", Toast.LENGTH_SHORT).show();
        }
    }

    private void cargarClientes() {
        listaClientes = dbTaller.obtenerClientes();
        clienteAdapter = new ClienteAdapter(listaClientes, new ClienteAdapter.OnClienteDeleteListener() {
            @Override
            public void onClienteDelete(Cliente cliente) {
                if (dbTaller.eliminarCliente(cliente.getCarnet())) {
                    Toast.makeText(ClientesActivity.this, "Cliente eliminado", Toast.LENGTH_SHORT).show();
                    cargarClientes();
                } else {
                    Toast.makeText(ClientesActivity.this, "Error al eliminar cliente", Toast.LENGTH_SHORT).show();
                }
            }
        });
        recyclerClientes.setAdapter(clienteAdapter);
    }

    private void limpiarCampos() {
        edtNombre.setText("");
        edtCarnet.setText("");
        edtDireccion.setText("");
        edtCorreo.setText("");
        edtTelefono.setText("");
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}