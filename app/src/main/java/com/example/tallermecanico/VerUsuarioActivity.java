package com.example.tallermecanico;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VerUsuarioActivity extends AppCompatActivity {
    private RecyclerView recyclerUsuario;
    private UsuarioAdapter usuarioAdapter;
    private List<Usuario> listaUsuarios;
    DBTaller dbTaller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_usuario);

        recyclerUsuario = findViewById(R.id.RecyclerUsuario);
        recyclerUsuario.setLayoutManager(new LinearLayoutManager(this));
        dbTaller = new DBTaller(this);
        listaUsuarios = dbTaller.obtenerUsuarios();
        usuarioAdapter = new UsuarioAdapter(listaUsuarios);
        recyclerUsuario.setAdapter(usuarioAdapter);
    }
}