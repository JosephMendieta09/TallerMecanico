package com.example.tallermecanico;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

public class DiagnosticosActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private Button btnNuevoDiagnostico;
    private RecyclerView recyclerDiagnosticos;
    private DiagnosticoAdapter diagnosticoAdapter;
    private List<Diagnostico> listaDiagnosticos;
    private DBTaller dbTaller;

    private void mostrarCerrarSesion() {
        new AlertDialog.Builder(this)
                .setTitle("Cerrar sesión")
                .setMessage("¿Estás seguro de que deseas cerrar sesión?")
                .setPositiveButton("Aceptar", (dialog, which) -> {
                    Intent intent = new Intent(DiagnosticosActivity.this, InicioSesionActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Cancelar", null).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnosticos);

        drawerLayout = findViewById(R.id.drawer_layout_diagnosticos);
        navigationView = findViewById(R.id.nav_view_diagnosticos);
        toolbar = findViewById(R.id.toolbar_diagnosticos);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                drawerLayout.closeDrawers();
                int id = menuItem.getItemId();

                if (id == R.id.nav_inicio) {
                    Intent intent = new Intent(DiagnosticosActivity.this, HomeActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_usuarios) {
                    Intent intent = new Intent(DiagnosticosActivity.this, VerUsuarioActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_clientes) {
                    Intent intent = new Intent(DiagnosticosActivity.this, ClientesActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_vehiculos) {
                    Intent intent = new Intent(DiagnosticosActivity.this, VehiculosActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_mecanicos) {
                    Intent intent = new Intent(DiagnosticosActivity.this, MecanicosActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_diagnosticos) {
                    Toast.makeText(DiagnosticosActivity.this, "Gestión de Diagnósticos", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_servicios) {
                    Intent intent = new Intent(DiagnosticosActivity.this, ServiciosActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_contactos) {
                    Intent intent = new Intent(DiagnosticosActivity.this, ContactosActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_sensores) {
                    Intent intent = new Intent(DiagnosticosActivity.this, SensoresActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_cerrar_sesion) {
                    mostrarCerrarSesion();
                    return true;
                }
                return false;
            }
        });

        dbTaller = new DBTaller(this);
        btnNuevoDiagnostico = findViewById(R.id.btnNuevoDiagnostico);
        recyclerDiagnosticos = findViewById(R.id.recyclerDiagnosticos);
        recyclerDiagnosticos.setLayoutManager(new LinearLayoutManager(this));

        listaDiagnosticos = new ArrayList<>();
        diagnosticoAdapter = new DiagnosticoAdapter(listaDiagnosticos,
                new DiagnosticoAdapter.OnDiagnosticoDeleteListener() {
                    @Override
                    public void onDiagnosticoDelete(Diagnostico diagnostico) {
                        if (dbTaller.eliminarDiagnostico(diagnostico.getIdDiagnostico())) {
                            Toast.makeText(DiagnosticosActivity.this,
                                    "Diagnóstico eliminado", Toast.LENGTH_SHORT).show();
                            cargarDiagnosticos();
                        } else {
                            Toast.makeText(DiagnosticosActivity.this,
                                    "Error al eliminar diagnóstico", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new DiagnosticoAdapter.OnDiagnosticoEditListener() {
                    @Override
                    public void onDiagnosticoEdit(Diagnostico diagnostico) {
                        Intent intent = new Intent(DiagnosticosActivity.this, FormDiagnosticoActivity.class);
                        intent.putExtra("diagnostico_id", diagnostico.getIdDiagnostico());
                        intent.putExtra("diagnostico_problema", diagnostico.getProblema());
                        intent.putExtra("diagnostico_id_vehiculo", diagnostico.getIdVehiculo());
                        intent.putExtra("diagnostico_id_mecanico", diagnostico.getIdMecanico());
                        startActivity(intent);
                    }
                });
        recyclerDiagnosticos.setAdapter(diagnosticoAdapter);

        btnNuevoDiagnostico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DiagnosticosActivity.this, FormDiagnosticoActivity.class);
                startActivity(intent);
            }
        });

        cargarDiagnosticos();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarDiagnosticos();
    }

    private void cargarDiagnosticos() {
        List<Diagnostico> diagnosticosDB = dbTaller.obtenerDiagnosticos();

        // Cargar información adicional para cada diagnóstico
        for (Diagnostico diagnostico : diagnosticosDB) {
            try {
                String placa = dbTaller.obtenerPlacaVehiculoPorId(diagnostico.getIdVehiculo());
                diagnostico.setPlacaVehiculo(placa);

                // Obtener id del cliente del vehículo de forma más eficiente
                int idCliente = dbTaller.obtenerIdClientePorIdVehiculo(diagnostico.getIdVehiculo());
                String nombreCliente = dbTaller.obtenerNombreClientePorId(idCliente);
                diagnostico.setNombreCliente(nombreCliente);

                if (diagnostico.getIdMecanico() != 0) {
                    String nombreMecanico = dbTaller.obtenerNombreMecanicoPorId(diagnostico.getIdMecanico());
                    diagnostico.setNombreMecanico(nombreMecanico);
                } else {
                    diagnostico.setNombreMecanico("Sin asignar");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        listaDiagnosticos.clear();
        listaDiagnosticos.addAll(diagnosticosDB);
        diagnosticoAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}