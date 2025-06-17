package com.example.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CuotasYPagos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cuotas_ypagos)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btnBack : ImageButton = findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }
        val btnCobroCuotas : Button = findViewById(R.id.btnCobroCuotas)
        btnCobroCuotas.setOnClickListener{
            val intent = Intent(this, CobroCuotas::class.java)
            startActivity(intent)
        }
        val btnCobroActividades : Button = findViewById(R.id.btnCobroActividades)
        btnCobroActividades.setOnClickListener{
            val intent = Intent(this, CobroActividades::class.java)
            startActivity(intent)
        }
        val btnHistorialDePagos : Button = findViewById(R.id.btnHistorialDePagos)
        btnHistorialDePagos.setOnClickListener{
            val intent = Intent(this, HistorialDePagos::class.java)
            startActivity(intent)
        }
    }
}