package com.example.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Clientes : AppCompatActivity() {
    private lateinit var listaLayout: LinearLayout
    private lateinit var db: SQLiteHelper
    private lateinit var inflater: LayoutInflater

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_clientes)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btnBack : ImageButton = findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }
        val btnAgregarCliente : ImageButton = findViewById(R.id.btnAgregarCliente)
        btnAgregarCliente.setOnClickListener{
            val intent = Intent(this, AgregarClientes::class.java)
            startActivity(intent)
        }
        db = SQLiteHelper(this)
        listaLayout = findViewById(R.id.listaClientesLayout)
        inflater = LayoutInflater.from(this)
        mostrarClientes()
    }

    override fun onResume() {
        super.onResume()
        mostrarClientes()
    }

    private fun mostrarClientes() {
        val clientes = db.obtenerClientes()
        listaLayout.removeAllViews()
        for (cliente in clientes) {
            val itemView = inflater.inflate(R.layout.item_cliente, listaLayout, false)
            val tvNombre = itemView.findViewById<TextView>(R.id.tvNombreCliente)
            val tvTipo = itemView.findViewById<TextView>(R.id.tvTipoCliente)
            tvNombre.text = "${cliente.apellido}, ${cliente.nombre}"
            tvTipo.text = cliente.tipo
            itemView.setOnClickListener {
                val intent = Intent(this, DetalleCliente::class.java)
                intent.putExtra("id", cliente.id)
                intent.putExtra("nombre", cliente.nombre)
                intent.putExtra("apellido", cliente.apellido)
                intent.putExtra("fechaNac", cliente.fechaNac)
                intent.putExtra("dni", cliente.dni)
                intent.putExtra("direccion", cliente.direccion)
                intent.putExtra("telefono", cliente.telefono)
                intent.putExtra("tipo", cliente.tipo)
                startActivity(intent)
            }
            listaLayout.addView(itemView)
        }
    }
}