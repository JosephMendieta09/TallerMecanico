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

public class MecanicosActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private Button btnNuevoMecanico;
    private RecyclerView recyclerMecanicos;
    private MecanicoAdapter mecanicoAdapter;
    private List<Mecanico> listaMecanicos;
    private DBTaller dbTaller;

    private void mostrarCerrarSesion() {
        new AlertDialog.Builder(this)
                .setTitle("Cerrar sesión")
                .setMessage("¿Estás seguro de que deseas cerrar sesión?")
                .setPositiveButton("Aceptar", (dialog, which) -> {
                    Intent intent = new Intent(MecanicosActivity.this, InicioSesionActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Cancelar", null).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mecanicos);

        drawerLayout = findViewById(R.id.drawer_layout_mecanicos);
        navigationView = findViewById(R.id.nav_view_mecanicos);
        toolbar = findViewById(R.id.toolbar_mecanicos);
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
                    Intent intent = new Intent(MecanicosActivity.this, HomeActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_usuarios) {
                    Intent intent = new Intent(MecanicosActivity.this, VerUsuarioActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_clientes) {
                    Intent intent = new Intent(MecanicosActivity.this, ClientesActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_vehiculos) {
                    Intent intent = new Intent(MecanicosActivity.this, VehiculosActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_mecanicos) {
                    Toast.makeText(MecanicosActivity.this, "Gestión de Mecánicos", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_diagnosticos) {
                    Intent intent = new Intent(MecanicosActivity.this, DiagnosticosActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_servicios) {
                    Intent intent = new Intent(MecanicosActivity.this, ServiciosActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_contactos) {
                    Intent intent = new Intent(MecanicosActivity.this, ContactosActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_sensores) {
                    Intent intent = new Intent(MecanicosActivity.this, SensoresActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_cerrar_sesion) {
                    mostrarCerrarSesion();
                    return true;
                }
                return false;
            }
        });

        dbTaller = new DBTaller(this);
        btnNuevoMecanico = findViewById(R.id.btnNuevoMecanico);
        recyclerMecanicos = findViewById(R.id.recyclerMecanicos);
        recyclerMecanicos.setLayoutManager(new LinearLayoutManager(this));

        listaMecanicos = new ArrayList<>();
        mecanicoAdapter = new MecanicoAdapter(listaMecanicos,
                new MecanicoAdapter.OnMecanicoDeleteListener() {
                    @Override
                    public void onMecanicoDelete(Mecanico mecanico) {
                        if (dbTaller.eliminarMecanico(mecanico.getCarnet())) {
                            Toast.makeText(MecanicosActivity.this,
                                    "Mecánico eliminado", Toast.LENGTH_SHORT).show();
                            cargarMecanicos();
                        } else {
                            Toast.makeText(MecanicosActivity.this,
                                    "Error al eliminar mecánico", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new MecanicoAdapter.OnMecanicoEditListener() {
                    @Override
                    public void onMecanicoEdit(Mecanico mecanico) {
                        Intent intent = new Intent(MecanicosActivity.this, FormMecanicoActivity.class);
                        intent.putExtra("mecanico_id", mecanico.getIdMecanico());
                        intent.putExtra("mecanico_nombre", mecanico.getNombre());
                        intent.putExtra("mecanico_carnet", mecanico.getCarnet());
                        intent.putExtra("mecanico_correo", mecanico.getCorreo());
                        intent.putExtra("mecanico_telefono", mecanico.getTelefono());
                        startActivity(intent);
                    }
                });
        recyclerMecanicos.setAdapter(mecanicoAdapter);

        btnNuevoMecanico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MecanicosActivity.this, FormMecanicoActivity.class);
                startActivity(intent);
            }
        });

        cargarMecanicos();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarMecanicos();
    }

    private void cargarMecanicos() {
        List<Mecanico> mecanicosDB = dbTaller.obtenerMecanicos();
        listaMecanicos.clear();
        listaMecanicos.addAll(mecanicosDB);
        mecanicoAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}