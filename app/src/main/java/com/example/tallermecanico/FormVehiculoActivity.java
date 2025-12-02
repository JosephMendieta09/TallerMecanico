package com.example.tallermecanico;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class FormVehiculoActivity extends AppCompatActivity {

    private TextInputEditText edtPlaca, edtMarca, edtModelo, edtAnio, edtColor, edtKilometraje;
    private Spinner spinnerClientes;
    private Button btnGuardar, btnCancelar, btnScanPlate;
    private TextView tvTitulo;
    private DBTaller dbTaller;

    private boolean esEdicion = false;
    private int idVehiculo = -1;
    private String placaOriginal = "";

    private final ActivityResultLauncher<Intent> plateRecognitionLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String plateNumber = result.getData().getStringExtra("PLATE_NUMBER");
                    if (plateNumber != null) {
                        edtPlaca.setText(plateNumber);
                        Toast.makeText(this, "Placa escaneada: " + plateNumber,
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_vehiculo);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        dbTaller = new DBTaller(this);

        // Inicializar vistas
        tvTitulo = findViewById(R.id.tvTituloFormVehiculo);
        edtPlaca = findViewById(R.id.edtPlacaVehiculo);
        spinnerClientes = findViewById(R.id.spinnerClientesForm);
        edtMarca = findViewById(R.id.edtMarcaVehiculo);
        edtModelo = findViewById(R.id.edtModeloVehiculo);
        edtAnio = findViewById(R.id.edtAnioVehiculo);
        edtColor = findViewById(R.id.edtColorVehiculo);
        edtKilometraje = findViewById(R.id.edtKilometrajeVehiculo);
        btnGuardar = findViewById(R.id.btnGuardarVehiculo);
        btnCancelar = findViewById(R.id.btnCancelarVehiculo);
        btnScanPlate = findViewById(R.id.btnScanPlateForm);

        // Cargar spinner de clientes
        cargarSpinnerClientes();

        // Configurar botón de escaneo de placa
        btnScanPlate.setOnClickListener(v -> {
            Intent intent = new Intent(this, ReconocimientoPlacaActivity.class);
            plateRecognitionLauncher.launch(intent);
        });

        // Verificar si es edición o nuevo registro
        if (getIntent().hasExtra("vehiculo_id")) {
            esEdicion = true;
            cargarDatosVehiculo();
            tvTitulo.setText("Modificar Vehículo");
            btnGuardar.setText("Actualizar Vehículo");
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Modificar Vehículo");
            }
            // Deshabilitar edición de la placa
            edtPlaca.setEnabled(false);
            btnScanPlate.setEnabled(false);
        } else {
            tvTitulo.setText("Nuevo Vehículo");
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Nuevo Vehículo");
            }
        }

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (esEdicion) {
                    actualizarVehiculo();
                } else {
                    guardarVehiculo();
                }
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void cargarSpinnerClientes() {
        List<String> nombresClientes = dbTaller.obtenerNombresClientes();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, nombresClientes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClientes.setAdapter(adapter);
    }

    private void cargarDatosVehiculo() {
        idVehiculo = getIntent().getIntExtra("vehiculo_id", -1);
        placaOriginal = getIntent().getStringExtra("vehiculo_placa");
        int idCliente = getIntent().getIntExtra("vehiculo_idcliente", -1);
        String marca = getIntent().getStringExtra("vehiculo_marca");
        String modelo = getIntent().getStringExtra("vehiculo_modelo");
        int anio = getIntent().getIntExtra("vehiculo_anio", 0);
        String color = getIntent().getStringExtra("vehiculo_color");
        int kilometraje = getIntent().getIntExtra("vehiculo_kilometraje", 0);

        edtPlaca.setText(placaOriginal);
        edtMarca.setText(marca);
        edtModelo.setText(modelo);
        edtAnio.setText(String.valueOf(anio));
        edtColor.setText(color);
        edtKilometraje.setText(String.valueOf(kilometraje));

        // Seleccionar el cliente en el spinner
        String nombreCliente = dbTaller.obtenerNombreClientePorId(idCliente);
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinnerClientes.getAdapter();
        int position = adapter.getPosition(nombreCliente);
        if (position >= 0) {
            spinnerClientes.setSelection(position);
        }
    }

    private void guardarVehiculo() {
        String placa = edtPlaca.getText().toString().trim();
        String marca = edtMarca.getText().toString().trim();
        String modelo = edtModelo.getText().toString().trim();
        String anioStr = edtAnio.getText().toString().trim();
        String color = edtColor.getText().toString().trim();
        String kilometrajeStr = edtKilometraje.getText().toString().trim();

        if (placa.isEmpty() || marca.isEmpty() || modelo.isEmpty() ||
                anioStr.isEmpty() || color.isEmpty() || kilometrajeStr.isEmpty()) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (spinnerClientes.getSelectedItem() == null) {
            Toast.makeText(this, "Seleccione un cliente", Toast.LENGTH_SHORT).show();
            return;
        }

        String nombreCliente = spinnerClientes.getSelectedItem().toString();
        int idCliente = dbTaller.obtenerIdClientePorNombre(nombreCliente);

        int anio = Integer.parseInt(anioStr);
        int kilometraje = Integer.parseInt(kilometrajeStr);

        Vehiculo vehiculo = new Vehiculo(placa, idCliente, marca, modelo, anio, color, kilometraje);

        if (dbTaller.insertarVehiculo(vehiculo)) {
            Toast.makeText(this, "Vehículo registrado exitosamente", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error al registrar vehículo", Toast.LENGTH_SHORT).show();
        }
    }

    private void actualizarVehiculo() {
        String placa = edtPlaca.getText().toString().trim();
        String marca = edtMarca.getText().toString().trim();
        String modelo = edtModelo.getText().toString().trim();
        String anioStr = edtAnio.getText().toString().trim();
        String color = edtColor.getText().toString().trim();
        String kilometrajeStr = edtKilometraje.getText().toString().trim();

        if (placa.isEmpty() || marca.isEmpty() || modelo.isEmpty() ||
                anioStr.isEmpty() || color.isEmpty() || kilometrajeStr.isEmpty()) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (spinnerClientes.getSelectedItem() == null) {
            Toast.makeText(this, "Seleccione un cliente", Toast.LENGTH_SHORT).show();
            return;
        }

        String nombreCliente = spinnerClientes.getSelectedItem().toString();
        int idCliente = dbTaller.obtenerIdClientePorNombre(nombreCliente);

        int anio = Integer.parseInt(anioStr);
        int kilometraje = Integer.parseInt(kilometrajeStr);

        Vehiculo vehiculo = new Vehiculo(idVehiculo, placa, idCliente, marca, modelo, anio, color, kilometraje);

        if (dbTaller.actualizarVehiculo(vehiculo)) {
            Toast.makeText(this, "Vehículo actualizado exitosamente", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error al actualizar vehículo", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // vuelve a la actividad anterior
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}