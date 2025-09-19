package com.example.tallermecanico;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    public ArrayList<Usuario> obtenerUsuarios(){
        ArrayList<Usuario> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM usuarios", null);
        if (cursor.moveToFirst()){
            do {
                Usuario usuario = new Usuario(
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(5),
                        cursor.getString(4)
                );
                lista.add(usuario);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lista;
    }
}
