package com.example.tallermecanico;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ClienteAdapter extends RecyclerView.Adapter<ClienteAdapter.ClienteViewHolder> {

    private List<Cliente> listaClientes;
    private OnClienteDeleteListener deleteListener;

    public interface OnClienteDeleteListener {
        void onClienteDelete(Cliente cliente);
    }

    public ClienteAdapter(List<Cliente> listaClientes, OnClienteDeleteListener deleteListener) {
        this.listaClientes = listaClientes;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ClienteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cliente, parent, false);
        return new ClienteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClienteViewHolder holder, int position) {
        Cliente cliente = listaClientes.get(position);
        holder.bind(cliente, deleteListener);
    }

    @Override
    public int getItemCount() {
        return listaClientes.size();
    }

    public static class ClienteViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNombre, tvCarnet, tvDireccion, tvCorreo, tvTelefono;
        private Button btnEliminar;

        public ClienteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreCliente);
            tvCarnet = itemView.findViewById(R.id.tvCarnet);
            tvDireccion = itemView.findViewById(R.id.tvDireccion);
            tvCorreo = itemView.findViewById(R.id.tvCorreoCliente);
            tvTelefono = itemView.findViewById(R.id.tvTelefonoCliente);
            btnEliminar = itemView.findViewById(R.id.btnEliminarCliente);
        }

        public void bind(final Cliente cliente, final OnClienteDeleteListener listener) {
            tvNombre.setText("Nombre: " + cliente.getNombre());
            tvCarnet.setText("Carnet: " + cliente.getCarnet());
            tvDireccion.setText("Dirección: " + cliente.getDireccion());
            tvCorreo.setText("Correo: " + cliente.getCorreo());
            tvTelefono.setText("Teléfono: " + cliente.getTelefono());

            btnEliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClienteDelete(cliente);
                }
            });
        }
    }
}