package com.example.tallermecanico;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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

public class VehiculosActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private Button btnNuevoVehiculo;
    private RecyclerView recyclerVehiculos;
    private VehiculoAdapter vehiculoAdapter;
    private List<Vehiculo> listaVehiculos;
    private DBTaller dbTaller;
    private void mostrarCerrarSesion() {
        new AlertDialog.Builder(this)
                .setTitle("Cerrar sesión")
                .setMessage("¿Estás seguro de que deseas cerrar sesión?")
                .setPositiveButton("Aceptar", (dialog, which) -> {
                    Intent intent = new Intent(VehiculosActivity.this, InicioSesionActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Cancelar", null).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehiculos);

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
                    Intent intent = new Intent(VehiculosActivity.this, HomeActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_clientes) {
                    Intent intent = new Intent(VehiculosActivity.this, ClientesActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_vehiculos) {
                    Toast.makeText(VehiculosActivity.this, "Gestión de Vehiculos", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_servicios) {
                    Intent intent = new Intent(VehiculosActivity.this, ServiciosActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_contactos) {
                    Intent intent = new Intent(VehiculosActivity.this, ContactosActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_sensores) {
                    Intent intent = new Intent(VehiculosActivity.this, SensoresActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_usuarios) {
                    Intent intent = new Intent(VehiculosActivity.this, VerUsuarioActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_cerrar_sesion) {
                    mostrarCerrarSesion();
                    return true;
                }
                return false;
            }
        });

        dbTaller = new DBTaller(this);

        btnNuevoVehiculo = findViewById(R.id.btnNuevoVehiculo);

        recyclerVehiculos = findViewById(R.id.recyclerVehiculos);
        recyclerVehiculos.setLayoutManager(new LinearLayoutManager(this));

        // Inicializar la lista y el adaptador UNA SOLA VEZ
        listaVehiculos = new ArrayList<>();
        vehiculoAdapter = new VehiculoAdapter(listaVehiculos,
                new VehiculoAdapter.OnVehiculoDeleteListener() {
                    @Override
                    public void onVehiculoDelete(Vehiculo vehiculo) {
                        if (dbTaller.eliminarVehiculo(vehiculo.getPlaca())) {
                            Toast.makeText(VehiculosActivity.this, "Vehículo eliminado", Toast.LENGTH_SHORT).show();
                            cargarVehiculos();
                        } else {
                            Toast.makeText(VehiculosActivity.this, "Error al eliminar vehículo", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new VehiculoAdapter.OnVehiculoEditListener() {
                    @Override
                    public void onVehiculoEdit(Vehiculo vehiculo) {
                        Intent intent = new Intent(VehiculosActivity.this, FormVehiculoActivity.class);
                        intent.putExtra("vehiculo_id", vehiculo.getIdvehiculo());
                        intent.putExtra("vehiculo_placa", vehiculo.getPlaca());
                        intent.putExtra("vehiculo_idcliente", vehiculo.getIdcliente());
                        intent.putExtra("vehiculo_marca", vehiculo.getMarca());
                        intent.putExtra("vehiculo_modelo", vehiculo.getModelo());
                        intent.putExtra("vehiculo_anio", vehiculo.getAnio());
                        intent.putExtra("vehiculo_color", vehiculo.getColor());
                        intent.putExtra("vehiculo_kilometraje", vehiculo.getKilometraje());
                        startActivity(intent);
                    }
                });
        recyclerVehiculos.setAdapter(vehiculoAdapter);

        btnNuevoVehiculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VehiculosActivity.this, FormVehiculoActivity.class);
                startActivity(intent);
            }
        });

        cargarVehiculos();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarVehiculos();
    }

    private void cargarVehiculos() {
        List<Vehiculo> vehiculosDB = dbTaller.obtenerVehiculos();

        // Cargar el nombre del cliente para cada vehículo
        for (Vehiculo vehiculo : vehiculosDB) {
            String nombreCliente = dbTaller.obtenerNombreClientePorId(vehiculo.getIdcliente());
            vehiculo.setNombreCliente(nombreCliente);
        }

        // Limpiar la lista actual y agregar todos los vehículos nuevos
        listaVehiculos.clear();
        listaVehiculos.addAll(vehiculosDB);

        // Notificar al adaptador que los datos han cambiado
        vehiculoAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}