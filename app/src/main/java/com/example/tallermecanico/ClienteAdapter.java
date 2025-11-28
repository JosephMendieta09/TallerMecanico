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
    private OnClienteEditListener editListener;

    public interface OnClienteDeleteListener {
        void onClienteDelete(Cliente cliente);
    }

    public interface OnClienteEditListener {
        void onClienteEdit(Cliente cliente);
    }

    public ClienteAdapter(List<Cliente> listaClientes, OnClienteDeleteListener deleteListener, OnClienteEditListener editListener) {
        this.listaClientes = listaClientes;
        this.deleteListener = deleteListener;
        this.editListener = editListener;
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
        holder.bind(cliente, deleteListener, editListener);
    }

    @Override
    public int getItemCount() {
        return listaClientes.size();
    }

    public static class ClienteViewHolder extends RecyclerView.ViewHolder {
        private TextView tvId, tvNombre, tvCarnet, tvDireccion, tvCorreo, tvTelefono;
        private Button btnEliminar, btnModificar;

        public ClienteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvIdCliente);
            tvNombre = itemView.findViewById(R.id.tvNombreCliente);
            tvCarnet = itemView.findViewById(R.id.tvCarnet);
            tvDireccion = itemView.findViewById(R.id.tvDireccion);
            tvCorreo = itemView.findViewById(R.id.tvCorreoCliente);
            tvTelefono = itemView.findViewById(R.id.tvTelefonoCliente);
            btnEliminar = itemView.findViewById(R.id.btnEliminarCliente);
            btnModificar = itemView.findViewById(R.id.btnModificarCliente);
        }

        public void bind(final Cliente cliente, final OnClienteDeleteListener listener, final OnClienteEditListener editListener) {
            tvId.setText("Id: " + cliente.getIdCliente());
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

            btnModificar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editListener.onClienteEdit(cliente);
                }
            });
        }
    }
}