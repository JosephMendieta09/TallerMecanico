package com.example.tallermecanico;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class VehiculosActivity extends AppCompatActivity {

    private EditText edtPlaca, edtMarca, edtModelo, edtAnio, edtColor, edtKilometraje;
    private Spinner spinnerClientes;
    private Button btnGuardarVehiculo;
    private Button btnScanPlate;
    private RecyclerView recyclerVehiculos;
    private VehiculoAdapter vehiculoAdapter;
    private List<Vehiculo> listaVehiculos;
    private DBTaller dbTaller;

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
        setContentView(R.layout.activity_vehiculos);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Gestión de Vehículos");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        dbTaller = new DBTaller(this);

        edtPlaca = findViewById(R.id.edtPlaca);
        edtMarca = findViewById(R.id.edtMarca);
        edtModelo = findViewById(R.id.edtModelo);
        edtAnio = findViewById(R.id.edtAnio);
        edtColor = findViewById(R.id.edtColor);
        edtKilometraje = findViewById(R.id.edtKilometraje);
        spinnerClientes = findViewById(R.id.spinnerClientes);
        btnScanPlate = findViewById(R.id.btnScanPlate);
        btnScanPlate.setOnClickListener(v -> {
            Intent intent = new Intent(this, ReconocimientoPlacaActivity.class);
            plateRecognitionLauncher.launch(intent);
        });
        btnGuardarVehiculo = findViewById(R.id.btnGuardarVehiculo);

        recyclerVehiculos = findViewById(R.id.recyclerVehiculos);
        recyclerVehiculos.setLayoutManager(new LinearLayoutManager(this));

        // Inicializar la lista y el adaptador UNA SOLA VEZ
        listaVehiculos = new ArrayList<>();
        vehiculoAdapter = new VehiculoAdapter(listaVehiculos, new VehiculoAdapter.OnVehiculoDeleteListener() {
            @Override
            public void onVehiculoDelete(Vehiculo vehiculo) {
                if (dbTaller.eliminarVehiculo(vehiculo.getPlaca())) {
                    Toast.makeText(VehiculosActivity.this, "Vehículo eliminado", Toast.LENGTH_SHORT).show();
                    cargarVehiculos();
                } else {
                    Toast.makeText(VehiculosActivity.this, "Error al eliminar vehículo", Toast.LENGTH_SHORT).show();
                }
            }
        });
        recyclerVehiculos.setAdapter(vehiculoAdapter);

        cargarSpinnerClientes();
        cargarVehiculos();

        btnGuardarVehiculo.setOnClickListener(v -> {
            String placa = edtPlaca.getText().toString().trim();

            if (placa.isEmpty()) {
                Toast.makeText(this, "Por favor ingresa o escanea una placa",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // Tu lógica para guardar el vehículo
            guardarVehiculo(placa);
        });
    }

    private void cargarSpinnerClientes() {
        List<String> nombresClientes = dbTaller.obtenerNombresClientes();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, nombresClientes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClientes.setAdapter(adapter);
    }

    private void guardarVehiculo(String placa) {
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
            limpiarCampos();
            cargarVehiculos();
        } else {
            Toast.makeText(this, "Error al registrar vehículo", Toast.LENGTH_SHORT).show();
        }
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

    private void limpiarCampos() {
        edtPlaca.setText("");
        edtMarca.setText("");
        edtModelo.setText("");
        edtAnio.setText("");
        edtColor.setText("");
        edtKilometraje.setText("");
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}