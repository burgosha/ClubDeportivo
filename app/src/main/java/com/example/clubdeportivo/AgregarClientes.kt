package com.example.clubdeportivo

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import android.widget.ToggleButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AgregarClientes : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_agregar_clientes)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btnBack : ImageButton = findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }

        val toggleTipo = findViewById<ToggleButton>(R.id.toggleButton)
        val etNombre = findViewById<EditText>(R.id.editTextText6)
        val etApellido = findViewById<EditText>(R.id.editTextText7)
        val etFechaNac = findViewById<EditText>(R.id.editTextText8)
        val etDni = findViewById<EditText>(R.id.editTextText9)
        val etDireccion = findViewById<EditText>(R.id.editTextText10)
        val etTelefono = findViewById<EditText>(R.id.editTextText11)
        val btnAnadir = findViewById<Button>(R.id.button5)

        btnAnadir.setOnClickListener {
            val tipo = if (toggleTipo.isChecked) "Socio" else "No Socio"
            val nombre = etNombre.text.toString().trim()
            val apellido = etApellido.text.toString().trim()
            val fechaNac = etFechaNac.text.toString().trim()
            val dni = etDni.text.toString().trim()
            val direccion = etDireccion.text.toString().trim()
            val telefono = etTelefono.text.toString().trim()

            if (nombre.isEmpty() || apellido.isEmpty() || fechaNac.isEmpty() || dni.isEmpty() || direccion.isEmpty() || telefono.isEmpty()) {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val db = SQLiteHelper(this)
            val values = android.content.ContentValues().apply {
                put("nombre", nombre)
                put("apellido", apellido)
                put("fecha_nac", fechaNac)
                put("dni", dni)
                put("direccion", direccion)
                put("telefono", telefono)
                put("tipo", tipo)
            }
            db.writableDatabase.execSQL(
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
            val result = db.writableDatabase.insert("clientes", null, values)
            db.close()
            if (result != -1L) {
                Toast.makeText(this, "Cliente añadido correctamente", Toast.LENGTH_SHORT).show()
                etNombre.text.clear()
                etApellido.text.clear()
                etFechaNac.text.clear()
                etDni.text.clear()
                etDireccion.text.clear()
                etTelefono.text.clear()
            } else {
                Toast.makeText(this, "Error al añadir cliente", Toast.LENGTH_SHORT).show()
            }
        }
    }
}