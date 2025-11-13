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

    public interface OnVehiculoDeleteListener {
        void onVehiculoDelete(Vehiculo vehiculo);
    }

    public VehiculoAdapter(List<Vehiculo> listaVehiculos, OnVehiculoDeleteListener deleteListener) {
        this.listaVehiculos = listaVehiculos;
        this.deleteListener = deleteListener;
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
        holder.bind(vehiculo, deleteListener);
    }

    @Override
    public int getItemCount() {
        return listaVehiculos.size();
    }

    public static class VehiculoViewHolder extends RecyclerView.ViewHolder {
        private TextView tvPlaca, tvCliente, tvMarca, tvModelo, tvAnio, tvColor, tvKilometraje;
        private Button btnEliminar;

        public VehiculoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPlaca = itemView.findViewById(R.id.tvPlaca);
            tvCliente = itemView.findViewById(R.id.tvCliente);
            tvMarca = itemView.findViewById(R.id.tvMarca);
            tvModelo = itemView.findViewById(R.id.tvModelo);
            tvAnio = itemView.findViewById(R.id.tvAnio);
            tvColor = itemView.findViewById(R.id.tvColor);
            tvKilometraje = itemView.findViewById(R.id.tvKilometraje);
            btnEliminar = itemView.findViewById(R.id.btnEliminarVehiculo);
        }

        public void bind(final Vehiculo vehiculo, final OnVehiculoDeleteListener listener) {
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
        }
    }
}