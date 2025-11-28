package com.example.tallermecanico;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VehiculoAdapter extends RecyclerView.Adapter<VehiculoAdapter.VehiculoViewHolder> {

    private List<Vehiculo> listaVehiculos;
    private OnVehiculoDeleteListener deleteListener;
    private OnVehiculoEditListener editListener;

    public interface OnVehiculoDeleteListener {
        void onVehiculoDelete(Vehiculo vehiculo);
    }

    public interface OnVehiculoEditListener {
        void onVehiculoEdit(Vehiculo vehiculo);
    }

    public VehiculoAdapter(List<Vehiculo> listaVehiculos, OnVehiculoDeleteListener deleteListener, OnVehiculoEditListener editListener) {
        this.listaVehiculos = listaVehiculos;
        this.deleteListener = deleteListener;
        this.editListener = editListener;
    }

    @NonNull
    @Override
    public VehiculoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vehiculo, parent, false);
        return new VehiculoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VehiculoViewHolder holder, int position) {
        Vehiculo vehiculo = listaVehiculos.get(position);
        holder.bind(vehiculo, deleteListener, editListener);
    }

    @Override
    public int getItemCount() {
        return listaVehiculos.size();
    }

    public static class VehiculoViewHolder extends RecyclerView.ViewHolder {
        private TextView tvId, tvPlaca, tvCliente, tvMarca, tvModelo, tvAnio, tvColor, tvKilometraje;
        private Button btnEliminar, btnModificar;

        public VehiculoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvIdVehiculo);
            tvPlaca = itemView.findViewById(R.id.tvPlaca);
            tvCliente = itemView.findViewById(R.id.tvCliente);
            tvMarca = itemView.findViewById(R.id.tvMarca);
            tvModelo = itemView.findViewById(R.id.tvModelo);
            tvAnio = itemView.findViewById(R.id.tvAnio);
            tvColor = itemView.findViewById(R.id.tvColor);
            tvKilometraje = itemView.findViewById(R.id.tvKilometraje);
            btnEliminar = itemView.findViewById(R.id.btnEliminarVehiculo);
            btnModificar = itemView.findViewById(R.id.btnModificarVehiculo);
        }

        public void bind(final Vehiculo vehiculo, final OnVehiculoDeleteListener listener, final OnVehiculoEditListener editListener) {
            tvId.setText("ID: " + vehiculo.getIdvehiculo());
            tvPlaca.setText("Placa: " + vehiculo.getPlaca());
            tvCliente.setText("Nombre: " + vehiculo.getNombreCliente());
            tvMarca.setText("Marca: " + vehiculo.getMarca());
            tvModelo.setText("Modelo: " + vehiculo.getModelo());
            tvAnio.setText("Año: " + vehiculo.getAnio());
            tvColor.setText("Color: " + vehiculo.getColor());
            tvKilometraje.setText("Kilometraje: " + vehiculo.getKilometraje() + " km");

            btnEliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onVehiculoDelete(vehiculo);
                }
            });

            btnModificar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editListener.onVehiculoEdit(vehiculo);
                }
            });
        }
    }
}