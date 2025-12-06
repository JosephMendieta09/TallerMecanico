package com.example.tallermecanico;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;

public class ContactosActivity extends AppCompatActivity implements OnMapReadyCallback {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private GoogleMap mMap;

    private static final double LATITUD = -17.778824789713884;
    private static final double LONGITUD = -63.14913753133914;
    private static final String NOMBRE_LUGAR = "Taller Mecánico El Garage";

    private void mostrarCerrarSesion() {
        new AlertDialog.Builder(this)
                .setTitle("Cerrar sesión")
                .setMessage("¿Estás seguro de que deseas cerrar sesión?")
                .setPositiveButton("Aceptar", (dialog, which) -> {
                    Intent intent = new Intent(ContactosActivity.this, InicioSesionActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Cancelar", null).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactos);

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
                    Intent intent = new Intent(ContactosActivity.this, HomeActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_clientes) {
                    Intent intent = new Intent(ContactosActivity.this, ClientesActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_vehiculos) {
                    Intent intent = new Intent(ContactosActivity.this, VehiculosActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_mecanicos) {
                    Intent intent = new Intent(ContactosActivity.this, MecanicosActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_diagnosticos) {
                    Intent intent = new Intent(ContactosActivity.this, DiagnosticosActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_servicios) {
                    Intent intent = new Intent(ContactosActivity.this, ServiciosActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_contactos) {
                    Toast.makeText(ContactosActivity.this, "Contactanos", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_sensores) {
                    Intent intent = new Intent(ContactosActivity.this, SensoresActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_usuarios) {
                    Intent intent = new Intent(ContactosActivity.this, VerUsuarioActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_cerrar_sesion) {
                    mostrarCerrarSesion();
                    return true;
                }
                return false;
            }
        });

        // Inicializar vistas
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Ubicación del taller
        LatLng ubicacionTaller = new LatLng(LATITUD, LONGITUD);

        // Agregar marcador
        mMap.addMarker(new MarkerOptions()
                .position(ubicacionTaller)
                .title(NOMBRE_LUGAR));

        // Mover cámara a la ubicación
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacionTaller, 14));

        // Configurar tipo de mapa
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Habilitar controles
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);

        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
    }
}