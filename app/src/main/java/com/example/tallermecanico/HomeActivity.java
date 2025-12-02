package com.example.tallermecanico;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity{

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    CardView cardcliente, cardvehiculo, cardservicio, cardcontacto, cardsensor, cardusuario;

    private void mostrarCerrarSesion() {
        new AlertDialog.Builder(this)
                .setTitle("Cerrar sesión")
                .setMessage("¿Estás seguro de que deseas cerrar sesión?")
                .setPositiveButton("Aceptar", (dialog, which) -> {
                    Intent intent = new Intent(HomeActivity.this, InicioSesionActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Cancelar", null).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
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
                    Toast.makeText(HomeActivity.this, "Inicio", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_clientes) {
                    Intent intent = new Intent(HomeActivity.this, ClientesActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_vehiculos) {
                    Intent intent = new Intent(HomeActivity.this, VehiculosActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_servicios) {
                    Intent intent = new Intent(HomeActivity.this, ServiciosActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_contactos) {
                    Intent intent = new Intent(HomeActivity.this, ContactosActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_sensores) {
                    Intent intent = new Intent(HomeActivity.this, SensoresActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_usuarios) {
                    Intent intent = new Intent(HomeActivity.this, VerUsuarioActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_cerrar_sesion) {
                    mostrarCerrarSesion();
                    return true;
                }
                return false;
            }
        });

        cardcliente = findViewById(R.id.cardclientes);
        cardcliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent jugando = new Intent(HomeActivity.this, ClientesActivity.class);
                startActivity(jugando);
            }
        });

        cardvehiculo = findViewById(R.id.cardvehiculos);
        cardvehiculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gener = new Intent(HomeActivity.this, VehiculosActivity.class);
                startActivity(gener);
            }
        });

        cardservicio = findViewById(R.id.cardservicios);
        cardservicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent agregar = new Intent(HomeActivity.this, ServiciosActivity.class);
                startActivity(agregar);
            }
        });

        cardcontacto = findViewById(R.id.cardcontactos);
        cardcontacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent jugando = new Intent(HomeActivity.this, ContactosActivity.class);
                startActivity(jugando);
            }
        });

        cardsensor = findViewById(R.id.cardsensores);
        cardsensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gener = new Intent(HomeActivity.this, SensoresActivity.class);
                startActivity(gener);
            }
        });

        cardusuario = findViewById(R.id.cardusuarios);
        cardusuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent agregar = new Intent(HomeActivity.this, VerUsuarioActivity.class);
                startActivity(agregar);
            }
        });
    }
}