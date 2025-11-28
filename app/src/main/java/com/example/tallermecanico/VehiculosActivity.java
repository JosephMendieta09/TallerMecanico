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
    private Button btnNuevoVehiculo;
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

        btnNuevoVehiculo = findViewById(R.id.btnNuevoVehiculo);

        recyclerVehiculos = findViewById(R.id.recyclerVehiculos);
        recyclerVehiculos.setLayoutManager(new LinearLayoutManager(this));

        // Inicializar la lista y el adaptador UNA SOLA VEZ
        listaVehiculos = new ArrayList<>();
        vehiculoAdapter = new VehiculoAdapter(listaVehiculos,
                new VehiculoAdapter.OnVehiculoDeleteListener() {
                    @Override
                    public void onVehiculoDelete(Vehiculo vehiculo) {
                        if (dbTaller.eliminarVehiculo(vehiculo.getPlaca())) {
                            Toast.makeText(VehiculosActivity.this, "Vehículo eliminado", Toast.LENGTH_SHORT).show();
                            cargarVehiculos();
                        } else {
                            Toast.makeText(VehiculosActivity.this, "Error al eliminar vehículo", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new VehiculoAdapter.OnVehiculoEditListener() {
                    @Override
                    public void onVehiculoEdit(Vehiculo vehiculo) {
                        Intent intent = new Intent(VehiculosActivity.this, FormVehiculoActivity.class);
                        intent.putExtra("vehiculo_id", vehiculo.getIdvehiculo());
                        intent.putExtra("vehiculo_placa", vehiculo.getPlaca());
                        intent.putExtra("vehiculo_idcliente", vehiculo.getIdcliente());
                        intent.putExtra("vehiculo_marca", vehiculo.getMarca());
                        intent.putExtra("vehiculo_modelo", vehiculo.getModelo());
                        intent.putExtra("vehiculo_anio", vehiculo.getAnio());
                        intent.putExtra("vehiculo_color", vehiculo.getColor());
                        intent.putExtra("vehiculo_kilometraje", vehiculo.getKilometraje());
                        startActivity(intent);
                    }
                });
        recyclerVehiculos.setAdapter(vehiculoAdapter);

        btnNuevoVehiculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VehiculosActivity.this, FormVehiculoActivity.class);
                startActivity(intent);
            }
        });

        cargarVehiculos();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarVehiculos();
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

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}