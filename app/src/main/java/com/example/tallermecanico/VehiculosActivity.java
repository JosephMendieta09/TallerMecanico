package com.example.tallermecanico;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VehiculosActivity extends AppCompatActivity {

    private EditText edtPlaca, edtMarca, edtModelo, edtAnio, edtColor, edtKilometraje;
    private Spinner spinnerClientes;
    private Button btnGuardarVehiculo;
    private RecyclerView recyclerVehiculos;
    private VehiculoAdapter vehiculoAdapter;
    private List<Vehiculo> listaVehiculos;
    private DBTaller dbTaller;

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
        btnGuardarVehiculo = findViewById(R.id.btnGuardarVehiculo);

        recyclerVehiculos = findViewById(R.id.recyclerVehiculos);
        recyclerVehiculos.setLayoutManager(new LinearLayoutManager(this));

        cargarSpinnerClientes();
        cargarVehiculos();

        btnGuardarVehiculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarVehiculo();
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
            limpiarCampos();
            cargarVehiculos();
        } else {
            Toast.makeText(this, "Error al registrar vehículo", Toast.LENGTH_SHORT).show();
        }
    }

    private void cargarVehiculos() {
        listaVehiculos = dbTaller.obtenerVehiculos();

        // Cargar el nombre del cliente para cada vehículo
        for (Vehiculo vehiculo : listaVehiculos) {
            String nombreCliente = dbTaller.obtenerNombreClientePorId(vehiculo.getIdcliente());
            vehiculo.setNombreCliente(nombreCliente);
        }

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