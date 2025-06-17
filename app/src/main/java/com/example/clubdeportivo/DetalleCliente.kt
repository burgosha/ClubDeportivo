package com.example.clubdeportivo

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class DetalleCliente : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_cliente)

        val btnBack: ImageButton = findViewById(R.id.btnBack)
        val btnBorrar: Button = findViewById(R.id.btnBorrar)
        val tvNombre: TextView = findViewById(R.id.tvNombre)
        val tvApellido: TextView = findViewById(R.id.tvApellido)
        val tvFechaNac: TextView = findViewById(R.id.tvFechaNac)
        val tvDni: TextView = findViewById(R.id.tvDni)
        val tvDireccion: TextView = findViewById(R.id.tvDireccion)
        val tvTelefono: TextView = findViewById(R.id.tvTelefono)
        val tvTipo: TextView = findViewById(R.id.tvTipo)

        val id = intent.getIntExtra("id", -1)
        val nombre = intent.getStringExtra("nombre") ?: ""
        val apellido = intent.getStringExtra("apellido") ?: ""
        val fechaNac = intent.getStringExtra("fechaNac") ?: ""
        val dni = intent.getStringExtra("dni") ?: ""
        val direccion = intent.getStringExtra("direccion") ?: ""
        val telefono = intent.getStringExtra("telefono") ?: ""
        val tipo = intent.getStringExtra("tipo") ?: ""

        tvNombre.text = nombre
        tvApellido.text = apellido
        tvFechaNac.text = fechaNac
        tvDni.text = dni
        tvDireccion.text = direccion
        tvTelefono.text = telefono
        tvTipo.text = tipo

        btnBack.setOnClickListener { finish() }

        btnBorrar.setOnClickListener {
            if (id != -1) {
                val db = SQLiteHelper(this)
                db.writableDatabase.delete("clientes", "id=?", arrayOf(id.toString()))
                db.close()
                Toast.makeText(this, "Cliente borrado", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
} 