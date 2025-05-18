package com.example.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Main_Screen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btnBack : ImageButton = findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }
        val btnClientes : Button = findViewById(R.id.btnClientes)
        btnClientes.setOnClickListener{
            val intent = Intent(this, Clientes::class.java)
            startActivity(intent)
        }
        val btnCuotas : Button = findViewById(R.id.btnCuotas)
        btnCuotas.setOnClickListener{
            val intent = Intent(this, CuotasYPagos::class.java)
            startActivity(intent)
        }
        val btnActividades : Button = findViewById(R.id.btnActividades)
        btnActividades.setOnClickListener{
            val intent = Intent(this, ActividadesActivity::class.java)
            startActivity(intent)
        }
    }
}