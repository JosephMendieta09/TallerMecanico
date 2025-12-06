package com.example.tallermecanico;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MecanicoAdapter extends RecyclerView.Adapter<MecanicoAdapter.MecanicoViewHolder> {

    private List<Mecanico> listaMecanicos;
    private OnMecanicoDeleteListener deleteListener;
    private OnMecanicoEditListener editListener;

    public interface OnMecanicoDeleteListener {
        void onMecanicoDelete(Mecanico mecanico);
    }

    public interface OnMecanicoEditListener {
        void onMecanicoEdit(Mecanico mecanico);
    }

    public MecanicoAdapter(List<Mecanico> listaMecanicos,
                           OnMecanicoDeleteListener deleteListener,
                           OnMecanicoEditListener editListener) {
        this.listaMecanicos = listaMecanicos;
        this.deleteListener = deleteListener;
        this.editListener = editListener;
    }

    @NonNull
    @Override
    public MecanicoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mecanico, parent, false);
        return new MecanicoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MecanicoViewHolder holder, int position) {
        Mecanico mecanico = listaMecanicos.get(position);
        holder.bind(mecanico, deleteListener, editListener);
    }

    @Override
    public int getItemCount() {
        return listaMecanicos.size();
    }

    public static class MecanicoViewHolder extends RecyclerView.ViewHolder {
        private TextView tvId, tvNombre, tvCarnet, tvCorreo, tvTelefono, tvEstado, tvIndicadorEstado;
        private Button btnEliminar, btnModificar;

        public MecanicoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvIdMecanico);
            tvNombre = itemView.findViewById(R.id.tvNombreMecanico);
            tvCarnet = itemView.findViewById(R.id.tvCarnetMecanico);
            tvCorreo = itemView.findViewById(R.id.tvCorreoMecanico);
            tvTelefono = itemView.findViewById(R.id.tvTelefonoMecanico);
            tvEstado = itemView.findViewById(R.id.tvEstadoMecanico);
            tvIndicadorEstado = itemView.findViewById(R.id.tvIndicadorEstado);
            btnEliminar = itemView.findViewById(R.id.btnEliminarMecanico);
            btnModificar = itemView.findViewById(R.id.btnModificarMecanico);
        }

        public void bind(final Mecanico mecanico,
                         final OnMecanicoDeleteListener listener,
                         final OnMecanicoEditListener editListener) {
            tvId.setText("ID: " + mecanico.getIdMecanico());
            tvNombre.setText("Nombre: " + mecanico.getNombre());
            tvCarnet.setText("Carnet: " + mecanico.getCarnet());
            tvCorreo.setText("Correo: " + mecanico.getCorreo());
            tvTelefono.setText("Teléfono: " + mecanico.getTelefono());
            tvEstado.setText("Estado: " + mecanico.getEstado());

            // Configurar indicador de estado (círculo verde/rojo)
            if (mecanico.getEstado().equals("Disponible")) {
                tvIndicadorEstado.setText("●");
                tvIndicadorEstado.setTextColor(Color.GREEN);
            } else {
                tvIndicadorEstado.setText("●");
                tvIndicadorEstado.setTextColor(Color.RED);
            }

            btnEliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onMecanicoDelete(mecanico);
                }
            });

            btnModificar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editListener.onMecanicoEdit(mecanico);
                }
            });
        }
    }
}