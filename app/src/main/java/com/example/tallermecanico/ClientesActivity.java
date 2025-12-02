package com.example.tallermecanico;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class ClientesActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private Button btnNuevoCliente;
    private RecyclerView recyclerClientes;
    private ClienteAdapter clienteAdapter;
    private List<Cliente> listaClientes;
    private DBTaller dbTaller;
    private void mostrarCerrarSesion() {
        new AlertDialog.Builder(this)
                .setTitle("Cerrar sesión")
                .setMessage("¿Estás seguro de que deseas cerrar sesión?")
                .setPositiveButton("Aceptar", (dialog, which) -> {
                    Intent intent = new Intent(ClientesActivity.this, InicioSesionActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Cancelar", null).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);

        drawerLayout = findViewById(R.id.drawer_layoutc);
        navigationView = findViewById(R.id.nav_viewc);
        toolbar = findViewById(R.id.toolbarc);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                drawerLayout.closeDrawers();
                int id = menuItem.getItemId();

                if (id == R.id.nav_inicio) {
                    Intent intent = new Intent(ClientesActivity.this, HomeActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_clientes) {
                    Toast.makeText(ClientesActivity.this, "Gestión de Clientes", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_vehiculos) {
                    Intent intent = new Intent(ClientesActivity.this, VehiculosActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_servicios) {
                    Intent intent = new Intent(ClientesActivity.this, ServiciosActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_contactos) {
                    Intent intent = new Intent(ClientesActivity.this, ContactosActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_sensores) {
                    Intent intent = new Intent(ClientesActivity.this, SensoresActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_usuarios) {
                    Intent intent = new Intent(ClientesActivity.this, VerUsuarioActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_cerrar_sesion) {
                    mostrarCerrarSesion();
                    return true;
                }
                return false;
            }
        });

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