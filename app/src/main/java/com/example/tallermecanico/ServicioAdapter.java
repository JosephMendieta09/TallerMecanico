package com.example.tallermecanico;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class ServicioAdapter extends RecyclerView.Adapter<ServicioAdapter.ServicioViewHolder> {

    private Context context;
    private List<Servicio> servicioList;
    private OnServicioListener listener;

    public interface OnServicioListener {
        void onEditarServicio(Servicio servicio);
        void onEliminarServicio(Servicio servicio);
    }

    public ServicioAdapter(Context context, List<Servicio> servicioList, OnServicioListener listener) {
        this.context = context;
        this.servicioList = servicioList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ServicioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_servicio, parent, false);
        return new ServicioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServicioViewHolder holder, int position) {
        Servicio servicio = servicioList.get(position);

        holder.tvNombre.setText(servicio.getNombre());
        holder.tvPrecio.setText("Bs" + String.format("%.2f", servicio.getPrecio()));
        holder.ivImagen.setImageResource(android.R.drawable.ic_menu_gallery);
        if (servicio.getImagen() != null && !servicio.getImagen().isEmpty()) {
            cargarImagen(servicio.getImagen(), holder.ivImagen);
        }

        holder.btnEditar.setOnClickListener(v -> listener.onEditarServicio(servicio));
        holder.btnEliminar.setOnClickListener(v -> listener.onEliminarServicio(servicio));
    }

    private void cargarImagen(String imageUrl, ImageView imageView) {
        new Thread(() -> {
            try {
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);
                connection.connect();

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream input = connection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(input);

                    new Handler(Looper.getMainLooper()).post(() -> {
                        if (bitmap != null) {
                            imageView.setImageBitmap(bitmap);
                        }
                    });
                }

                connection.disconnect();

            } catch (Exception e) {
                e.printStackTrace();
                // Mantener imagen por defecto si falla
                new Handler(Looper.getMainLooper()).post(() -> {
                    imageView.setImageResource(android.R.drawable.ic_menu_gallery);
                });
            }
        }).start();
    }

    @Override
    public int getItemCount() {
        return servicioList.size();
    }

    static class ServicioViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvPrecio;
        ImageView ivImagen;
        ImageButton btnEditar, btnEliminar;

        public ServicioViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            ivImagen = itemView.findViewById(R.id.ivImagen);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }
}
