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

        db.execSQL("CREATE TABLE usuario(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT, "+
                "usuario TEXT, "+
                "correo TEXT UNIQUE, " +
                "contrasena TEXT, " +
                "fecha_creacion DATE DEFAULT (DATE('now')))");

        db.execSQL("CREATE TABLE cliente(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                "nombre TEXT," +
                "carnet INTEGER UNIQUE, " +
                "direccion TEXT," +
                "correo TEXT," +
                "telefono TEXT)");

        db.execSQL("CREATE TABLE vehiculo(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "placa TEXT UNIQUE, " +
                "id_cliente INTEGER, " +
                "marca TEXT, " +
                "modelo TEXT, " +
                "anio INTEGER, " +
                "color TEXT, " +
                "kilometraje INTEGER, " +
                "FOREIGN KEY(id_cliente) REFERENCES cliente(id))");

        db.execSQL("CREATE TABLE mecanico(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT, " +
                "carnet INTEGER UNIQUE, " +
                "correo TEXT, " +
                "telefono TEXT, " +
                "estado TEXT)");

        db.execSQL("CREATE TABLE diagnostico(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "problema TEXT, " +
                "id_vehiculo INTEGER, " +
                "id_mecanico INTEGER, " +
                "fecha DATE DEFAULT (DATE('now')), " +
                "resultado TEXT, " +
                "observacion TEXT, " +
                "estado TEXT, " +
                "FOREIGN KEY(id_vehiculo) REFERENCES vehiculo(id), " +
                "FOREIGN KEY(id_mecanico) REFERENCES mecanico(id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS usuario");
        db.execSQL("DROP TABLE IF EXISTS cliente");
        db.execSQL("DROP TABLE IF EXISTS vehiculo");
        db.execSQL("DROP TABLE IF EXISTS mecanico");
        db.execSQL("DROP TABLE IF EXISTS diagnostico");
        onCreate(db);
    }



    public boolean insertarUsuario(Usuario usuario){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("nombre", usuario.getNombre());
        valores.put("usuario", usuario.getUsuario());
        valores.put("correo", usuario.getEmail());
        valores.put("contrasena", usuario.getPassword());
        long resultado = db.insert("usuario", null, valores);
        db.close();
        return resultado != -1;
    }

    public ArrayList<Usuario> obtenerUsuarios(){
        ArrayList<Usuario> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM usuario", null);
        if (cursor.moveToFirst()){
            do {
                Usuario usuario = new Usuario(
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4)
                );
                lista.add(usuario);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lista;
    }

    public boolean validarUsuario(String correo, String contrasena) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM usuario WHERE correo=? AND contrasena=?",
                new String[]{correo, contrasena});
        boolean existe = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return existe;
    }

    public boolean existeCorreo(String correo) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM usuario WHERE correo = ?", new String[]{correo});
        boolean existe = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return existe;
    }

    public boolean actualizarContrasena(String correo, String nuevaContra) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("contrasena", nuevaContra);
        int filas = db.update("usuario", valores, "correo=?", new String[]{correo});
        db.close();
        return filas > 0;
    }

    // MÉTODOS CLIENTE
    public boolean insertarCliente(Cliente cliente){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("nombre", cliente.getNombre());
        valores.put("carnet", cliente.getCarnet());
        valores.put("direccion", cliente.getDireccion());
        valores.put("correo", cliente.getCorreo());
        valores.put("telefono", cliente.getTelefono());
        long resultado = db.insert("cliente", null, valores);
        db.close();
        return resultado != -1;
    }

    public ArrayList<Cliente> obtenerClientes() {
        ArrayList<Cliente> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM cliente", null);
        if (cursor.moveToFirst()) {
            do {
                Cliente cliente = new Cliente(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getInt(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5)
                );
                lista.add(cliente);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lista;
    }

    public boolean eliminarCliente(int carnet) {
        SQLiteDatabase db = this.getWritableDatabase();
        int filas = db.delete("cliente", "carnet=?", new String[]{String.valueOf(carnet)});
        db.close();
        return filas > 0;
    }

    public ArrayList<String> obtenerNombresClientes() {
        ArrayList<String> nombres = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT nombre FROM cliente", null);
        if (cursor.moveToFirst()) {
            do {
                nombres.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return nombres;
    }

    public int obtenerIdClientePorNombre(String nombreCliente) {
        int id = -1;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM cliente WHERE nombre=?", new String[]{nombreCliente});
        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return id;
    }

    // MÉTODOS VEHÍCULO
    public boolean insertarVehiculo(Vehiculo vehiculo){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("placa", vehiculo.getPlaca());
        valores.put("id_cliente", vehiculo.getIdcliente());
        valores.put("marca", vehiculo.getMarca());
        valores.put("modelo", vehiculo.getModelo());
        valores.put("anio", vehiculo.getAnio());
        valores.put("color", vehiculo.getColor());
        valores.put("kilometraje", vehiculo.getKilometraje());
        long resultado = db.insert("vehiculo", null, valores);
        db.close();
        return resultado != -1;
    }

    public ArrayList<Vehiculo> obtenerVehiculos() {
        ArrayList<Vehiculo> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM vehiculo", null);
        if (cursor.moveToFirst()) {
            do {
                Vehiculo vehiculo = new Vehiculo(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getInt(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getInt(5),
                        cursor.getString(6),
                        cursor.getInt(7)
                );
                lista.add(vehiculo);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lista;
    }

    public boolean eliminarVehiculo(String placa) {
        SQLiteDatabase db = this.getWritableDatabase();
        int filas = db.delete("vehiculo", "placa=?", new String[]{placa});
        db.close();
        return filas > 0;
    }

    public String obtenerNombreClientePorId(int idCliente) {
        String nombre = "Desconocido";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT nombre FROM cliente WHERE id=?", new String[]{String.valueOf(idCliente)});
        if (cursor.moveToFirst()) {
            nombre = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return nombre;
    }

    public boolean actualizarCliente(Cliente cliente) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("nombre", cliente.getNombre());
        valores.put("direccion", cliente.getDireccion());
        valores.put("correo", cliente.getCorreo());
        valores.put("telefono", cliente.getTelefono());

        // Actualizamos por ID, no por carnet, ya que el carnet no se debe modificar
        int filas = db.update("cliente", valores, "id=?",
                new String[]{String.valueOf(cliente.getIdCliente())});
        db.close();
        return filas > 0;
    }

    public boolean actualizarVehiculo(Vehiculo vehiculo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("id_cliente", vehiculo.getIdcliente());
        valores.put("marca", vehiculo.getMarca());
        valores.put("modelo", vehiculo.getModelo());
        valores.put("anio", vehiculo.getAnio());
        valores.put("color", vehiculo.getColor());
        valores.put("kilometraje", vehiculo.getKilometraje());

        // Actualizamos por ID, no por placa, ya que la placa no se debe modificar
        int filas = db.update("vehiculo", valores, "id=?",
                new String[]{String.valueOf(vehiculo.getIdvehiculo())});
        db.close();
        return filas > 0;
    }

    // MÉTODOS MECÁNICO
    public boolean insertarMecanico(Mecanico mecanico){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("nombre", mecanico.getNombre());
        valores.put("carnet", mecanico.getCarnet());
        valores.put("correo", mecanico.getCorreo());
        valores.put("telefono", mecanico.getTelefono());
        valores.put("estado", "Disponible"); // Estado por defecto
        long resultado = db.insert("mecanico", null, valores);
        db.close();
        return resultado != -1;
    }

    public ArrayList<Mecanico> obtenerMecanicos() {
        ArrayList<Mecanico> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM mecanico", null);
        if (cursor.moveToFirst()) {
            do {
                Mecanico mecanico = new Mecanico(
                        cursor.getInt(0),      // id
                        cursor.getString(1),   // nombre
                        cursor.getInt(2),      // carnet
                        cursor.getString(3),   // correo
                        cursor.getString(4),   // telefono
                        cursor.getString(5)    // estado
                );
                lista.add(mecanico);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lista;
    }

    public boolean eliminarMecanico(int carnet) {
        SQLiteDatabase db = this.getWritableDatabase();
        int filas = db.delete("mecanico", "carnet=?", new String[]{String.valueOf(carnet)});
        db.close();
        return filas > 0;
    }

    public boolean actualizarMecanico(Mecanico mecanico) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("nombre", mecanico.getNombre());
        valores.put("correo", mecanico.getCorreo());
        valores.put("telefono", mecanico.getTelefono());
        // No actualizamos el estado aquí, se maneja desde diagnóstico

        int filas = db.update("mecanico", valores, "id=?",
                new String[]{String.valueOf(mecanico.getIdMecanico())});
        db.close();
        return filas > 0;
    }

    public ArrayList<String> obtenerNombresMecanicosDisponibles() {
        ArrayList<String> nombres = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT nombre FROM mecanico WHERE estado='Disponible'", null);
        if (cursor.moveToFirst()) {
            do {
                nombres.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return nombres;
    }

    public int obtenerIdMecanicoPorNombre(String nombreMecanico) {
        int id = -1;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM mecanico WHERE nombre=?", new String[]{nombreMecanico});
        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return id;
    }

    public boolean actualizarEstadoMecanico(int idMecanico, String nuevoEstado) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("estado", nuevoEstado);
        int filas = db.update("mecanico", valores, "id=?",
                new String[]{String.valueOf(idMecanico)});
        db.close();
        return filas > 0;
    }

    public String obtenerNombreMecanicoPorId(int idMecanico) {
        String nombre = "Desconocido";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT nombre FROM mecanico WHERE id=?",
                new String[]{String.valueOf(idMecanico)});
        if (cursor.moveToFirst()) {
            nombre = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return nombre;
    }

    // MÉTODOS DIAGNÓSTICO
    public boolean insertarDiagnostico(Diagnostico diagnostico){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("problema", diagnostico.getProblema());
        valores.put("id_vehiculo", diagnostico.getIdVehiculo());

        // Verificar si hay mecánicos disponibles
        ArrayList<String> mecanicosDisponibles = obtenerNombresMecanicosDisponibles();

        if (mecanicosDisponibles.size() > 0 && diagnostico.getIdMecanico() != -1) {
            valores.put("id_mecanico", diagnostico.getIdMecanico());
            valores.put("estado", "Asignado");
            // Cambiar estado del mecánico a Ocupado
            actualizarEstadoMecanico(diagnostico.getIdMecanico(), "Ocupado");
        } else {
            valores.putNull("id_mecanico");
            valores.put("estado", "Pendiente");
        }

        valores.putNull("resultado");
        valores.putNull("observacion");

        long resultado = db.insert("diagnostico", null, valores);
        db.close();
        return resultado != -1;
    }

    public ArrayList<Diagnostico> obtenerDiagnosticos() {
        ArrayList<Diagnostico> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM diagnostico", null);
        if (cursor.moveToFirst()) {
            do {
                Diagnostico diagnostico = new Diagnostico(
                        cursor.getInt(0),      // id
                        cursor.getString(1),   // problema
                        cursor.getInt(2),      // id_vehiculo
                        cursor.getInt(3),      // id_mecanico
                        cursor.getString(4),   // fecha
                        cursor.getString(5),   // resultado
                        cursor.getString(6),   // observacion
                        cursor.getString(7)    // estado
                );
                lista.add(diagnostico);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lista;
    }

    public boolean eliminarDiagnostico(int idDiagnostico) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Obtener id del mecánico antes de eliminar para liberar su estado
        Cursor cursor = db.rawQuery("SELECT id_mecanico, estado FROM diagnostico WHERE id=?",
                new String[]{String.valueOf(idDiagnostico)});
        if (cursor.moveToFirst()) {
            int idMecanico = cursor.getInt(0);
            String estado = cursor.getString(1);
            // Si el diagnóstico no está finalizado y tiene mecánico, liberarlo
            if (!estado.equals("Finalizado") && idMecanico != 0) {
                actualizarEstadoMecanico(idMecanico, "Disponible");
            }
        }
        cursor.close();

        int filas = db.delete("diagnostico", "id=?", new String[]{String.valueOf(idDiagnostico)});
        db.close();
        return filas > 0;
    }

    public boolean actualizarDiagnostico(Diagnostico diagnostico) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("resultado", diagnostico.getResultado());
        valores.put("observacion", diagnostico.getObservacion());
        valores.put("estado", "Finalizado");

        // Liberar al mecánico
        if (diagnostico.getIdMecanico() != 0) {
            actualizarEstadoMecanico(diagnostico.getIdMecanico(), "Disponible");
        }

        int filas = db.update("diagnostico", valores, "id=?",
                new String[]{String.valueOf(diagnostico.getIdDiagnostico())});
        db.close();
        return filas > 0;
    }

    public ArrayList<String> obtenerPlacasVehiculosConCliente() {
        ArrayList<String> placasConCliente = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT v.placa, c.nombre FROM vehiculo v " +
                        "INNER JOIN cliente c ON v.id_cliente = c.id", null);
        if (cursor.moveToFirst()) {
            do {
                String placa = cursor.getString(0);
                String nombreCliente = cursor.getString(1);
                placasConCliente.add(placa + " - " + nombreCliente);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return placasConCliente;
    }

    public int obtenerIdVehiculoPorPlaca(String placa) {
        int id = -1;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM vehiculo WHERE placa=?", new String[]{placa});
        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return id;
    }

    public String obtenerPlacaVehiculoPorId(int idVehiculo) {
        String placa = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT placa FROM vehiculo WHERE id=?",
                new String[]{String.valueOf(idVehiculo)});
        if (cursor.moveToFirst()) {
            placa = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return placa;
    }
}
