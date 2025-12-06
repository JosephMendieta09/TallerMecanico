package com.example.tallermecanico;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

public class FormDiagnosticoActivity extends AppCompatActivity {

    private TextInputEditText edtProblema, edtResultado, edtObservacion;
    private TextInputLayout layoutResultado, layoutObservacion;
    private Spinner spinnerVehiculos, spinnerMecanicos;
    private Button btnGuardar, btnCancelar;
    private TextView tvTitulo, tvVehiculoSeleccionado;
    private DBTaller dbTaller;

    private boolean esEdicion = false;
    private int idDiagnostico = -1;
    private int idVehiculoOriginal = -1;
    private int idMecanicoOriginal = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_diagnostico);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        dbTaller = new DBTaller(this);

        // Inicializar vistas
        tvTitulo = findViewById(R.id.tvTituloFormDiagnostico);
        tvVehiculoSeleccionado = findViewById(R.id.tvVehiculoSeleccionado);
        edtProblema = findViewById(R.id.edtProblemaDiagnostico);
        spinnerVehiculos = findViewById(R.id.spinnerVehiculosForm);
        spinnerMecanicos = findViewById(R.id.spinnerMecanicosForm);
        layoutResultado = findViewById(R.id.layoutResultadoDiagnostico);
        layoutObservacion = findViewById(R.id.layoutObservacionDiagnostico);
        edtResultado = findViewById(R.id.edtResultadoDiagnostico);
        edtObservacion = findViewById(R.id.edtObservacionDiagnostico);
        btnGuardar = findViewById(R.id.btnGuardarDiagnostico);
        btnCancelar = findViewById(R.id.btnCancelarDiagnostico);

        // Verificar si es edición o nuevo registro
        if (getIntent().hasExtra("diagnostico_id")) {
            esEdicion = true;
            cargarDatosDiagnostico();
            tvTitulo.setText("Finalizar Diagnóstico");
            btnGuardar.setText("Guardar y Finalizar");
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Finalizar Diagnóstico");
            }

            // Mostrar campos de resultado y observación
            layoutResultado.setVisibility(View.VISIBLE);
            layoutObservacion.setVisibility(View.VISIBLE);

            // Ocultar spinners y mostrar información de vehículo/mecánico
            spinnerVehiculos.setVisibility(View.GONE);
            spinnerMecanicos.setVisibility(View.GONE);
            tvVehiculoSeleccionado.setVisibility(View.VISIBLE);

        } else {
            tvTitulo.setText("Nuevo Diagnóstico");
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Nuevo Diagnóstico");
            }

            // Ocultar campos de resultado y observación
            layoutResultado.setVisibility(View.GONE);
            layoutObservacion.setVisibility(View.GONE);
            tvVehiculoSeleccionado.setVisibility(View.GONE);

            // Cargar spinners
            cargarSpinnerVehiculos();
            cargarSpinnerMecanicos();
        }

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (esEdicion) {
                    finalizarDiagnostico();
                } else {
                    guardarDiagnostico();
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

    private void cargarSpinnerVehiculos() {
        List<String> vehiculosConCliente = dbTaller.obtenerPlacasVehiculosConCliente();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, vehiculosConCliente);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVehiculos.setAdapter(adapter);
    }

    private void cargarSpinnerMecanicos() {
        List<String> nombresMecanicos = dbTaller.obtenerNombresMecanicosDisponibles();

        if (nombresMecanicos.isEmpty()) {
            nombresMecanicos.add("No hay mecánicos disponibles");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, nombresMecanicos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMecanicos.setAdapter(adapter);
    }

    private void cargarDatosDiagnostico() {
        idDiagnostico = getIntent().getIntExtra("diagnostico_id", -1);
        String problema = getIntent().getStringExtra("diagnostico_problema");
        idVehiculoOriginal = getIntent().getIntExtra("diagnostico_id_vehiculo", -1);
        idMecanicoOriginal = getIntent().getIntExtra("diagnostico_id_mecanico", -1);

        edtProblema.setText(problema);
        edtProblema.setEnabled(false);

        // Mostrar información del vehículo y mecánico
        String placa = dbTaller.obtenerPlacaVehiculoPorId(idVehiculoOriginal);
        int idCliente = 0;
        for (Vehiculo v : dbTaller.obtenerVehiculos()) {
            if (v.getIdvehiculo() == idVehiculoOriginal) {
                idCliente = v.getIdcliente();
                break;
            }
        }
        String nombreCliente = dbTaller.obtenerNombreClientePorId(idCliente);
        String nombreMecanico = dbTaller.obtenerNombreMecanicoPorId(idMecanicoOriginal);

        tvVehiculoSeleccionado.setText("Vehículo: " + placa + " - " + nombreCliente +
                "\nMecánico: " + nombreMecanico);
    }

    private void guardarDiagnostico() {
        String problema = edtProblema.getText().toString().trim();

        if (problema.isEmpty()) {
            Toast.makeText(this, "Ingrese el problema", Toast.LENGTH_SHORT).show();
            return;
        }

        if (spinnerVehiculos.getSelectedItem() == null) {
            Toast.makeText(this, "Seleccione un vehículo", Toast.LENGTH_SHORT).show();
            return;
        }

        // Extraer la placa del formato "PLACA - CLIENTE"
        String vehiculoSeleccionado = spinnerVehiculos.getSelectedItem().toString();
        String placa = vehiculoSeleccionado.split(" - ")[0];
        int idVehiculo = dbTaller.obtenerIdVehiculoPorPlaca(placa);

        int idMecanico = -1;
        if (spinnerMecanicos.getSelectedItem() != null &&
                !spinnerMecanicos.getSelectedItem().toString().equals("No hay mecánicos disponibles")) {
            String nombreMecanico = spinnerMecanicos.getSelectedItem().toString();
            idMecanico = dbTaller.obtenerIdMecanicoPorNombre(nombreMecanico);
        }

        Diagnostico diagnostico = new Diagnostico(problema, idVehiculo, idMecanico);

        if (dbTaller.insertarDiagnostico(diagnostico)) {
            if (idMecanico == -1) {
                Toast.makeText(this, "Diagnóstico registrado como PENDIENTE " +
                        "(no hay mecánicos disponibles)", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Diagnóstico registrado y ASIGNADO exitosamente",
                        Toast.LENGTH_SHORT).show();
            }
            finish();
        } else {
            Toast.makeText(this, "Error al registrar diagnóstico", Toast.LENGTH_SHORT).show();
        }
    }

    private void finalizarDiagnostico() {
        String resultado = edtResultado.getText().toString().trim();
        String observacion = edtObservacion.getText().toString().trim();

        if (resultado.isEmpty() || observacion.isEmpty()) {
            Toast.makeText(this, "Complete el resultado y la observación",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Diagnostico diagnostico = new Diagnostico(
                idDiagnostico,
                edtProblema.getText().toString().trim(),
                idVehiculoOriginal,
                idMecanicoOriginal,
                null,
                resultado,
                observacion,
                "Finalizado"
        );

        if (dbTaller.actualizarDiagnostico(diagnostico)) {
            Toast.makeText(this, "Diagnóstico finalizado exitosamente. " +
                    "Mecánico liberado.", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error al finalizar diagnóstico", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
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