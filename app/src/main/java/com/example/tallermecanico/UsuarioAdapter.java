package com.example.tallermecanico;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder> {
    private List<Usuario> listaUsuarios;
    public UsuarioAdapter(List<Usuario> listaUsuarios){
        this.listaUsuarios = listaUsuarios;
    }

    @NonNull
    @Override
    public UsuarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_usuario, parent, false);
        return new UsuarioViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuarioViewHolder holder, int position) {
        Usuario usuario = listaUsuarios.get(position);
        holder.bind(usuario);
    }

    @Override
    public int getItemCount() {
        return listaUsuarios.size();
    }

    public class UsuarioViewHolder extends RecyclerView.ViewHolder {
        private TextView nombreT, apellidoT, correoT, contraT;
        public UsuarioViewHolder(View item) {
            super(item);
            nombreT = item.findViewById(R.id.tvNombre);
            apellidoT = item.findViewById(R.id.tvApellido);
            correoT = item.findViewById(R.id.tvCorreo);
            contraT = item.findViewById(R.id.tvContrasena);
        }

        public void bind(Usuario usuario) {
            nombreT.setText("Nombre: " + usuario.getNombre());
            apellidoT.setText("Usuario: " + usuario.getUsuario());
            correoT.setText("Correo: " + usuario.getEmail());
            contraT.setText("Contraseña: " + usuario.getPassword());
        }
    }
}
