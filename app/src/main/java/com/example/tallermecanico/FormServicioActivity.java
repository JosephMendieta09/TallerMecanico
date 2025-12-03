package com.example.tallermecanico;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FormServicioActivity extends AppCompatActivity {

    private EditText etNombre, etPrecio, etImagen;
    private ImageView ivPreviewImagen;
    private TextView tvUrlCompleta;
    private Button btnGuardar, btnCancelar;
    private ProgressBar progressBar;
    private Servicio servicioEditar;
    private boolean esEdicion = false;
    private static final String API_URL = "https://web-production-b5c7d.up.railway.app/api.php";
    private static final String BASE_URL = "https://web-production-b5c7d.up.railway.app";
    private static final String IMAGES_PATH = BASE_URL + "/api.php?imagen=";
    private static final String DEFAULT_IMAGE = "default.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_servicio);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        etNombre = findViewById(R.id.etNombre);
        etPrecio = findViewById(R.id.etPrecio);
        etImagen = findViewById(R.id.etImagen);
        ivPreviewImagen = findViewById(R.id.ivPreviewImagen);
        tvUrlCompleta = findViewById(R.id.tvUrlCompleta);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnCancelar = findViewById(R.id.btnCancelar);
        progressBar = findViewById(R.id.progressBar);

        servicioEditar = (Servicio) getIntent().getSerializableExtra("servicio");

        if (servicioEditar != null) {
            esEdicion = true;
            cargarDatosServicio();
            btnGuardar.setText("Actualizar");
        } else {
            actualizarVistaPrevia(DEFAULT_IMAGE);
        }

        etImagen.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String nombreImagen = s.toString().trim();
                if (nombreImagen.isEmpty()) {
                    nombreImagen = DEFAULT_IMAGE;
                }
                actualizarVistaPrevia(nombreImagen);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnGuardar.setOnClickListener(v -> guardarServicio());
        btnCancelar.setOnClickListener(v -> finish());
    }

    private void cargarDatosServicio() {
        etNombre.setText(servicioEditar.getNombre());
        etPrecio.setText(String.valueOf(servicioEditar.getPrecio()));

        String imagenUrl = servicioEditar.getImagen();
        String nombreImagen = extraerNombreImagen(imagenUrl);
        etImagen.setText(nombreImagen);

        actualizarVistaPrevia(nombreImagen);
    }

    private String extraerNombreImagen(String urlCompleta) {
        if (urlCompleta == null || urlCompleta.isEmpty()) {
            return DEFAULT_IMAGE;
        }

        if (!urlCompleta.contains("/")) {
            return urlCompleta;
        }

        String[] partes = urlCompleta.split("/");
        return partes[partes.length - 1];
    }

    private String construirUrlImagen(String nombreImagen) {
        if (nombreImagen == null || nombreImagen.trim().isEmpty()) {
            return IMAGES_PATH + DEFAULT_IMAGE;
        }

        nombreImagen = nombreImagen.trim();

        if (nombreImagen.startsWith("http://") || nombreImagen.startsWith("https://")) {
            return nombreImagen;
        }

        return IMAGES_PATH + nombreImagen;
    }

    private void actualizarVistaPrevia(String nombreImagen) {
        String urlCompleta = construirUrlImagen(nombreImagen);
        tvUrlCompleta.setText("URL: " + urlCompleta);

        new Thread(() -> {
            try {
                URL url = new URL(urlCompleta);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                connection.connect();

                InputStream input = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(input);

                new Handler(Looper.getMainLooper()).post(() -> {
                    if (bitmap != null) {
                        ivPreviewImagen.setImageBitmap(bitmap);
                    } else {
                        ivPreviewImagen.setImageResource(android.R.drawable.ic_menu_gallery);
                    }
                });

                connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(() -> {
                    ivPreviewImagen.setImageResource(android.R.drawable.ic_menu_gallery);
                });
            }
        }).start();
    }

    private void guardarServicio() {
        String nombre = etNombre.getText().toString().trim();
        String precioStr = etPrecio.getText().toString().trim();
        String nombreImagen = etImagen.getText().toString().trim();

        if (nombre.isEmpty() || precioStr.isEmpty()) {
            Toast.makeText(this, "Por favor complete nombre y precio", Toast.LENGTH_SHORT).show();
            return;
        }

        double precio;
        try {
            precio = Double.parseDouble(precioStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Precio inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        // CAMBIO: Guardar solo el nombre del archivo, NO la URL completa
        if (nombreImagen.isEmpty()) {
            nombreImagen = DEFAULT_IMAGE;
        }

        progressBar.setVisibility(View.VISIBLE);
        btnGuardar.setEnabled(false);

        if (esEdicion) {
            actualizarServicio(nombre, precio, nombreImagen);
        } else {
            agregarServicio(nombre, precio, nombreImagen);
        }
    }

    private void agregarServicio(String nombre, double precio, String imagen) {
        new Thread(() -> {
            try {
                URL url = new URL(API_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);
                conn.setConnectTimeout(15000);
                conn.setReadTimeout(15000);

                JSONObject jsonServicio = new JSONObject();
                jsonServicio.put("nombre", nombre);
                jsonServicio.put("precio", precio);
                jsonServicio.put("imagen", imagen);

                OutputStream os = conn.getOutputStream();
                os.write(jsonServicio.toString().getBytes("UTF-8"));
                os.flush();
                os.close();

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

                new Handler(Looper.getMainLooper()).post(() -> {
                    progressBar.setVisibility(View.GONE);
                    btnGuardar.setEnabled(true);

                    if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                        Toast.makeText(FormServicioActivity.this,
                                "✅ Servicio agregado exitosamente", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(FormServicioActivity.this,
                                "❌ Error al agregar servicio (Código: " + responseCode + ")",
                                Toast.LENGTH_LONG).show();
                    }
                });

                conn.disconnect();

            } catch (Exception e) {
                e.printStackTrace();
                mostrarError("❌ Error de conexión: " + e.getMessage());
            }
        }).start();
    }

    private void actualizarServicio(String nombre, double precio, String imagen) {
        new Thread(() -> {
            HttpURLConnection conn = null;
            try {
                // CORRECCIÓN: Usar PUT real con el ID en la URL
                URL url = new URL(API_URL + "?id=" + servicioEditar.getId());
                conn = (HttpURLConnection) url.openConnection();

                // Importante: setDoOutput debe ir ANTES de setRequestMethod para PUT
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestMethod("PUT");
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setRequestProperty("Accept", "application/json");
                conn.setUseCaches(false);
                conn.setConnectTimeout(15000);
                conn.setReadTimeout(15000);

                JSONObject jsonServicio = new JSONObject();
                jsonServicio.put("nombre", nombre);
                jsonServicio.put("precio", precio);
                jsonServicio.put("imagen", imagen);

                byte[] outputBytes = jsonServicio.toString().getBytes("UTF-8");

                OutputStream os = conn.getOutputStream();
                os.write(outputBytes);
                os.flush();
                os.close();

                int responseCode = conn.getResponseCode();

                BufferedReader reader;
                if (responseCode >= 200 && responseCode < 300) {
                    reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                } else {
                    reader = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"));
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
                    btnGuardar.setEnabled(true);

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        Toast.makeText(FormServicioActivity.this,
                                "✅ Servicio actualizado exitosamente", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(FormServicioActivity.this,
                                "❌ Error al actualizar (Código: " + responseCode + "): " + responseMsg,
                                Toast.LENGTH_LONG).show();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                mostrarError("❌ Error de conexión: " + e.getMessage());
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
            btnGuardar.setEnabled(true);
            Toast.makeText(FormServicioActivity.this, mensaje, Toast.LENGTH_LONG).show();
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}