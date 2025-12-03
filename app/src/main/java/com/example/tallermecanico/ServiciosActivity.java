package com.example.tallermecanico;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ServiciosActivity extends AppCompatActivity implements ServicioAdapter.OnServicioListener {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private ServicioAdapter adapter;
    private List<Servicio> servicioList;
    private ProgressBar progressBar;
    private FloatingActionButton fabAgregar;
    private static final String API_URL = "https://web-production-b5c7d.up.railway.app/api.php";

    private void mostrarCerrarSesion() {
        new AlertDialog.Builder(this)
                .setTitle("Cerrar sesión")
                .setMessage("¿Estás seguro de que deseas cerrar sesión?")
                .setPositiveButton("Aceptar", (dialog, which) -> {
                    Intent intent = new Intent(ServiciosActivity.this, InicioSesionActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Cancelar", null).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicios);

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
                    Intent intent = new Intent(ServiciosActivity.this, HomeActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_clientes) {
                    Intent intent = new Intent(ServiciosActivity.this, ClientesActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_vehiculos) {
                    Intent intent = new Intent(ServiciosActivity.this, VehiculosActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_servicios) {
                    Toast.makeText(ServiciosActivity.this, "Gestión de Servicios", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_contactos) {
                    Intent intent = new Intent(ServiciosActivity.this, ContactosActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_sensores) {
                    Intent intent = new Intent(ServiciosActivity.this, SensoresActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_usuarios) {
                    Intent intent = new Intent(ServiciosActivity.this, VerUsuarioActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_cerrar_sesion) {
                    mostrarCerrarSesion();
                    return true;
                }
                return false;
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        fabAgregar = findViewById(R.id.fabAgregar);

        servicioList = new ArrayList<>();
        adapter = new ServicioAdapter(this, servicioList, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        fabAgregar.setOnClickListener(v -> {
            Intent intent = new Intent(ServiciosActivity.this, FormServicioActivity.class);
            startActivity(intent);
        });

        cargarServicios();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarServicios();
    }

    private void cargarServicios() {
        progressBar.setVisibility(View.VISIBLE);

        new Thread(() -> {
            try {
                URL url = new URL(API_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
                conn.setConnectTimeout(15000);
                conn.setReadTimeout(15000);

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    String jsonString = response.toString().trim();
                    List<Servicio> servicios = new ArrayList<>();

                    try {
                        if (jsonString.startsWith("{")) {
                            JSONObject jsonResponse = new JSONObject(jsonString);

                            if (jsonResponse.has("data")) {
                                JSONArray jsonArray = jsonResponse.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    Servicio servicio = new Servicio(
                                            obj.getInt("id"),
                                            obj.getString("nombre"),
                                            obj.getDouble("precio"),
                                            obj.getString("imagen")
                                    );
                                    servicios.add(servicio);
                                }
                            }
                        } else if (jsonString.startsWith("[")) {
                            JSONArray jsonArray = new JSONArray(jsonString);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                Servicio servicio = new Servicio(
                                        obj.getInt("id"),
                                        obj.getString("nombre"),
                                        obj.getDouble("precio"),
                                        obj.getString("imagen")
                                );
                                servicios.add(servicio);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        mostrarError("❌ Error al procesar datos: " + e.getMessage());
                        return;
                    }

                    List<Servicio> finalServicios = servicios;
                    new Handler(Looper.getMainLooper()).post(() -> {
                        servicioList.clear();
                        servicioList.addAll(finalServicios);
                        adapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);

                        if (finalServicios.isEmpty()) {
                            Toast.makeText(ServiciosActivity.this,
                                    "📋 No hay servicios disponibles", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ServiciosActivity.this,
                                    "✅ " + finalServicios.size() + " servicios cargados",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    mostrarError("❌ Error del servidor. Código: " + responseCode);
                }

                conn.disconnect();

            } catch (Exception e) {
                e.printStackTrace();
                mostrarError("❌ Error de conexión: " + e.getMessage());
            }
        }).start();
    }

    @Override
    public void onEditarServicio(Servicio servicio) {
        Intent intent = new Intent(ServiciosActivity.this, FormServicioActivity.class);
        intent.putExtra("servicio", servicio);
        startActivity(intent);
    }

    @Override
    public void onEliminarServicio(Servicio servicio) {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Confirmar eliminación")
                .setMessage("¿Está seguro de que desea eliminar este servicio?")
                .setPositiveButton("Eliminar", (dialog, which) -> eliminarServicio(servicio))
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void eliminarServicio(Servicio servicio) {
        progressBar.setVisibility(View.VISIBLE);

        new Thread(() -> {
            HttpURLConnection conn = null;
            try {
                // CORRECCIÓN: Usar DELETE real con el ID en la URL
                URL url = new URL(API_URL + "?id=" + servicio.getId());
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("DELETE");
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setConnectTimeout(15000);
                conn.setReadTimeout(15000);

                // DELETE no necesita body, pero algunos servidores esperan que se lea la respuesta
                int responseCode = conn.getResponseCode();

                BufferedReader reader;
                if (responseCode >= 200 && responseCode < 300) {
                    reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                } else {
                    reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                }
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                final String responseMsg = response.toString();

                new Handler(Looper.getMainLooper()).post(() -> {
                    progressBar.setVisibility(View.GONE);

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        Toast.makeText(ServiciosActivity.this,
                                "✅ Servicio eliminado exitosamente", Toast.LENGTH_SHORT).show();
                        cargarServicios();
                    } else {
                        Toast.makeText(ServiciosActivity.this,
                                "❌ Error al eliminar servicio (Código: " + responseCode + "): " + responseMsg,
                                Toast.LENGTH_LONG).show();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                mostrarError("❌ Error al eliminar: " + e.getMessage());
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }).start();
    }

    private void mostrarError(String mensaje) {
        new Handler(Looper.getMainLooper()).post(() -> {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(ServiciosActivity.this, mensaje, Toast.LENGTH_LONG).show();
        });
    }
}