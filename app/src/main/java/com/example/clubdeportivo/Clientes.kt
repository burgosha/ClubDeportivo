package com.example.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Clientes : AppCompatActivity() {
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
        val btnClienteLayout : LinearLayout = findViewById(R.id.btnClienteLayout)
        btnClienteLayout.setOnClickListener{
            val intent = Intent(this, PerfilCliente::class.java)
            startActivity(intent)
        }
    }
}