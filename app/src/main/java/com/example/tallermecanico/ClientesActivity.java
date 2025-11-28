package com.example.tallermecanico;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ClientesActivity extends AppCompatActivity {

    private Button btnNuevoCliente;
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

        btnNuevoCliente = findViewById(R.id.btnNuevoCliente);
        recyclerClientes = findViewById(R.id.recyclerClientes);
        recyclerClientes.setLayoutManager(new LinearLayoutManager(this));

        listaClientes = new ArrayList<>();
        clienteAdapter = new ClienteAdapter(listaClientes,
                new ClienteAdapter.OnClienteDeleteListener() {
                    @Override
                    public void onClienteDelete(Cliente cliente) {
                        if (dbTaller.eliminarCliente(cliente.getCarnet())) {
                            Toast.makeText(ClientesActivity.this, "Cliente eliminado", Toast.LENGTH_SHORT).show();
                            cargarClientes();
                        } else {
                            Toast.makeText(ClientesActivity.this, "Error al eliminar cliente", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new ClienteAdapter.OnClienteEditListener() {
                    @Override
                    public void onClienteEdit(Cliente cliente) {
                        Intent intent = new Intent(ClientesActivity.this, FormClienteActivity.class);
                        intent.putExtra("cliente_id", cliente.getIdCliente());
                        intent.putExtra("cliente_nombre", cliente.getNombre());
                        intent.putExtra("cliente_carnet", cliente.getCarnet());
                        intent.putExtra("cliente_direccion", cliente.getDireccion());
                        intent.putExtra("cliente_correo", cliente.getCorreo());
                        intent.putExtra("cliente_telefono", cliente.getTelefono());
                        startActivity(intent);
                    }
                });
        recyclerClientes.setAdapter(clienteAdapter);

        btnNuevoCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClientesActivity.this, FormClienteActivity.class);
                startActivity(intent);
            }
        });

        cargarClientes();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarClientes();
    }

    private void cargarClientes() {
        // Obtener los clientes de la base de datos
        List<Cliente> clientesDB = dbTaller.obtenerClientes();

        // Limpiar la lista actual y agregar todos los clientes nuevos
        listaClientes.clear();
        listaClientes.addAll(clientesDB);

        // Notificar al adaptador que los datos han cambiado
        clienteAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}