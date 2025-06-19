package com.example.clubdeportivo

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLiteHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "clubdeportivo.db"
        private const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS usuarios (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nombre TEXT, " +
                    "correo TEXT UNIQUE, " +
                    "contrasena TEXT)"
        )
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS clientes (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nombre TEXT, " +
                    "apellido TEXT, " +
                    "fecha_nac TEXT, " +
                    "dni TEXT, " +
                    "direccion TEXT, " +
                    "telefono TEXT, " +
                    "tipo TEXT)"
        )
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS pagos (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "cliente_id INTEGER, " +
                    "monto REAL, " +
                    "metodo_pago TEXT, " +
                    "fecha TEXT, " +
                    "FOREIGN KEY(cliente_id) REFERENCES clientes(id))"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS usuarios")
        db.execSQL("DROP TABLE IF EXISTS clientes")
        onCreate(db)
    }

    fun insertarUsuarioPrueba() {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("nombre", "Administrador")
            put("correo", "admin@club.com")
            put("contrasena", "1234")
        }
        db.insertWithOnConflict("usuarios", null, values, SQLiteDatabase.CONFLICT_IGNORE)
        db.close()
    }

    fun validarUsuario(correo: String, contrasena: String): Boolean {
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery(
            "SELECT * FROM usuarios WHERE correo = ? AND contrasena = ?",
            arrayOf(correo, contrasena)
        )
        val existe = cursor.count > 0
        cursor.close()
        db.close()
        return existe
    }

    fun obtenerClientes(): List<Cliente> {
        val clientes = mutableListOf<Cliente>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM clientes", null)
        if (cursor.moveToFirst()) {
            do {
                val cliente = Cliente(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                    apellido = cursor.getString(cursor.getColumnIndexOrThrow("apellido")),
                    fechaNac = cursor.getString(cursor.getColumnIndexOrThrow("fecha_nac")),
                    dni = cursor.getString(cursor.getColumnIndexOrThrow("dni")),
                    direccion = cursor.getString(cursor.getColumnIndexOrThrow("direccion")),
                    telefono = cursor.getString(cursor.getColumnIndexOrThrow("telefono")),
                    tipo = cursor.getString(cursor.getColumnIndexOrThrow("tipo"))
                )
                clientes.add(cliente)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return clientes
    }

    fun insertarPago(clienteId: Int, monto: Double, metodoPago: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("cliente_id", clienteId)
            put("monto", monto)
            put("metodo_pago", metodoPago)
            put("fecha", System.currentTimeMillis())
        }
        val result = db.insert("pagos", null, values)
        db.close()
        return result != -1L
    }

    fun obtenerPagosConCliente(): List<Map<String, String>> {
        val pagos = mutableListOf<Map<String, String>>() 
        val db = readableDatabase
        val query = "SELECT pagos.id, pagos.monto, pagos.metodo_pago, pagos.fecha, clientes.nombre, clientes.apellido, clientes.tipo FROM pagos INNER JOIN clientes ON pagos.cliente_id = clientes.id ORDER BY pagos.fecha DESC"
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                val pago = mapOf(
                    "id" to cursor.getInt(cursor.getColumnIndexOrThrow("id")).toString(),
                    "monto" to cursor.getDouble(cursor.getColumnIndexOrThrow("monto")).toString(),
                    "metodo_pago" to cursor.getString(cursor.getColumnIndexOrThrow("metodo_pago")),
                    "fecha" to cursor.getString(cursor.getColumnIndexOrThrow("fecha")),
                    "nombre" to cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                    "apellido" to cursor.getString(cursor.getColumnIndexOrThrow("apellido")),
                    "tipo" to cursor.getString(cursor.getColumnIndexOrThrow("tipo"))
                )
                pagos.add(pago)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return pagos
    }

    fun obtenerPagosDeCliente(clienteId: Int): List<Map<String, String>> {
        val pagos = mutableListOf<Map<String, String>>()
        val db = readableDatabase
        val query = "SELECT id, monto, metodo_pago, fecha FROM pagos WHERE cliente_id = ? ORDER BY fecha DESC"
        val cursor = db.rawQuery(query, arrayOf(clienteId.toString()))
        if (cursor.moveToFirst()) {
            do {
                val pago = mapOf(
                    "id" to cursor.getInt(cursor.getColumnIndexOrThrow("id")).toString(),
                    "monto" to cursor.getDouble(cursor.getColumnIndexOrThrow("monto")).toString(),
                    "metodo_pago" to cursor.getString(cursor.getColumnIndexOrThrow("metodo_pago")),
                    "fecha" to cursor.getString(cursor.getColumnIndexOrThrow("fecha"))
                )
                pagos.add(pago)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return pagos
    }

} 