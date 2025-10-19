package com.example.tallermecanico;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private String correoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        correoUsuario = getIntent().getStringExtra("correo");

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Mostrar correo en el header del navigation drawer
        View headerView = navigationView.getHeaderView(0);
        TextView tvUserEmail = headerView.findViewById(R.id.tvUserEmail);
        if (correoUsuario != null) {
            tvUserEmail.setText(correoUsuario);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_inicio) {
            // Ya estamos en Home, no hacer nada
            Toast.makeText(this, "Inicio", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_clientes) {
            Intent intent = new Intent(HomeActivity.this, ClientesActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_vehiculos) {
            Intent intent = new Intent(HomeActivity.this, VehiculosActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_usuarios) {
            Intent intent = new Intent(HomeActivity.this, VerUsuarioActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_cerrar_sesion) {
            Toast.makeText(this, "Cerrando sesión...", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(HomeActivity.this, InicioSesionActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}