package com.example.tallermecanico;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DiagnosticoAdapter extends RecyclerView.Adapter<DiagnosticoAdapter.DiagnosticoViewHolder> {

    private List<Diagnostico> listaDiagnosticos;
    private OnDiagnosticoDeleteListener deleteListener;
    private OnDiagnosticoEditListener editListener;

    public interface OnDiagnosticoDeleteListener {
        void onDiagnosticoDelete(Diagnostico diagnostico);
    }

    public interface OnDiagnosticoEditListener {
        void onDiagnosticoEdit(Diagnostico diagnostico);
    }

    public DiagnosticoAdapter(List<Diagnostico> listaDiagnosticos,
                              OnDiagnosticoDeleteListener deleteListener,
                              OnDiagnosticoEditListener editListener) {
        this.listaDiagnosticos = listaDiagnosticos;
        this.deleteListener = deleteListener;
        this.editListener = editListener;
    }

    @NonNull
    @Override
    public DiagnosticoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_diagnostico, parent, false);
        return new DiagnosticoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiagnosticoViewHolder holder, int position) {
        Diagnostico diagnostico = listaDiagnosticos.get(position);
        holder.bind(diagnostico, deleteListener, editListener);
    }

    @Override
    public int getItemCount() {
        return listaDiagnosticos.size();
    }

    public static class DiagnosticoViewHolder extends RecyclerView.ViewHolder {
        private TextView tvId, tvProblema, tvVehiculo, tvMecanico, tvFecha,
                tvResultado, tvObservacion, tvEstado;
        private Button btnEliminar, btnModificar;

        public DiagnosticoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvIdDiagnostico);
            tvProblema = itemView.findViewById(R.id.tvProblemaDiagnostico);
            tvVehiculo = itemView.findViewById(R.id.tvVehiculoDiagnostico);
            tvMecanico = itemView.findViewById(R.id.tvMecanicoDiagnostico);
            tvFecha = itemView.findViewById(R.id.tvFechaDiagnostico);
            tvResultado = itemView.findViewById(R.id.tvResultadoDiagnostico);
            tvObservacion = itemView.findViewById(R.id.tvObservacionDiagnostico);
            tvEstado = itemView.findViewById(R.id.tvEstadoDiagnostico);
            btnEliminar = itemView.findViewById(R.id.btnEliminarDiagnostico);
            btnModificar = itemView.findViewById(R.id.btnModificarDiagnostico);
        }

        public void bind(final Diagnostico diagnostico,
                         final OnDiagnosticoDeleteListener listener,
                         final OnDiagnosticoEditListener editListener) {
            tvId.setText("ID: " + diagnostico.getIdDiagnostico());
            tvProblema.setText("Problema: " + diagnostico.getProblema());
            tvVehiculo.setText("Vehículo: " + diagnostico.getPlacaVehiculo() +
                    " - " + diagnostico.getNombreCliente());

            String mecanico = diagnostico.getNombreMecanico() != null ?
                    diagnostico.getNombreMecanico() : "Sin asignar";
            tvMecanico.setText("Mecánico: " + mecanico);
            tvFecha.setText("Fecha: " + diagnostico.getFecha());

            String resultado = diagnostico.getResultado() != null ?
                    diagnostico.getResultado() : "Pendiente";
            tvResultado.setText("Resultado: " + resultado);

            String observacion = diagnostico.getObservacion() != null ?
                    diagnostico.getObservacion() : "Sin observaciones";
            tvObservacion.setText("Observación: " + observacion);
            tvEstado.setText("Estado: " + diagnostico.getEstado());

            // Solo permitir editar si no está finalizado
            if (diagnostico.getEstado().equals("Finalizado")) {
                btnModificar.setEnabled(false);
                btnModificar.setAlpha(0.5f);
            } else {
                btnModificar.setEnabled(true);
                btnModificar.setAlpha(1.0f);
            }

            btnEliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onDiagnosticoDelete(diagnostico);
                }
            });

            btnModificar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editListener.onDiagnosticoEdit(diagnostico);
                }
            });
        }
    }
}