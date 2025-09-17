package com.example.tallermecanico;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class DBTaller extends SQLiteOpenHelper {
    //constructor
    public DBTaller(@Nullable Context context){
        //invocar al constructor de la clase padre
        super(context, "Taller.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE usuarios(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT, "+
                "apellido TEXT, "+
                "correo TEXT UNIQUE, " +
                "contrasena TEXT, " +
                "telefono TEXT, " +
                "fecha_nac TEXT, " +
                "verificado INTEGER DEFAULT 0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS usuarios");
        onCreate(db);
    }

    public boolean insertarUsuario(String nombre, String apellido, String correo, String contrasena, String telefono, String fecha_nac) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("nombre", nombre);
        valores.put("apellido", apellido);
        valores.put("correo", correo);
        valores.put("contrasena", contrasena);
        valores.put("telefono", telefono);
        valores.put("fecha_nac", fecha_nac);
        long resultado = db.insert("usuarios", null, valores);
        db.close();
        return resultado != -1;
    }

    public boolean insertarUsuario(Usuario usuario){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("nombre", usuario.getNombre());
        valores.put("apellido", usuario.getApellido());
        valores.put("correo", usuario.getEmail());
        valores.put("contrasena", usuario.getPassword());
        if (usuario.getTelefono() != null){
            valores.put("telefono", usuario.getTelefono());
        } else{
            valores.put("telefono", "");
        }

        if (usuario.getFechaNacimiento() != null) {
            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String fechaFormateada = formato.format(usuario.getFechaNacimiento());
            valores.put("fecha_nac", fechaFormateada);
        } else {
            valores.put("fecha_nac", "");
        }
        try {
            long resultado = db.insertOrThrow("usuarios", null, valores);
            db.close();
            return resultado != -1;
        } catch (Exception e) {
            Log.e("DB_ERROR", "Error insertando usuario: " + e.getMessage());
            return false;
        }
    }
}
