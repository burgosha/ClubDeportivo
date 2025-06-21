package com.example.clubdeportivo

import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.LinearLayout
import android.widget.TextView
import android.graphics.drawable.GradientDrawable
import android.view.ViewGroup
import android.graphics.Typeface
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistorialDePagos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_historial_pagos)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btnBack : ImageButton = findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }

        val dbHelper = SQLiteHelper(this)
        dbHelper.insertarClientesYPagosPrueba()
        val pagos = dbHelper.obtenerPagosConCliente()
        val layout = findViewById<LinearLayout>(R.id.layoutPagos)
        layout.removeAllViews()
        if (pagos.isEmpty()) {
            val tv = TextView(this)
            tv.text = "No hay pagos registrados."
            layout.addView(tv)
        } else {
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            for (pago in pagos) {
                val nombre = "${pago["apellido"]}, ${pago["nombre"]} (${pago["tipo"]})"
                val fechaLegible = try {
                    val fechaLong = pago["fecha"]?.toLongOrNull() ?: 0L
                    sdf.format(Date(fechaLong))
                } catch (e: Exception) {
                    pago["fecha"] ?: ""
                }
                val detalle = "Monto: $${pago["monto"]} - Método: ${pago["metodo_pago"]}\nFecha: $fechaLegible"
                val tv = TextView(this)
                tv.text = "$nombre\n$detalle"
                // Estilo tipo card
                val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                params.setMargins(0, 0, 0, 24)
                tv.layoutParams = params
                tv.setPadding(32, 24, 32, 24)
                val drawable = GradientDrawable()
                drawable.cornerRadius = 32f
                drawable.setColor(0xFFE1BEE7.toInt())
                tv.background = drawable
                tv.setTypeface(null, Typeface.BOLD)
                tv.textSize = 16f
                layout.addView(tv)
            }
        }
        val btnVencido : ImageButton = findViewById(R.id.btnVencido)
        btnVencido.setOnClickListener {
            val vencidos = dbHelper.obtenerClientesConPagosVencidos()
            layout.removeAllViews()
            if (vencidos.isEmpty()) {
                val tv = TextView(this)
                tv.text = "No hay socios con pagos vencidos."
                layout.addView(tv)
            } else {
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                for (cliente in vencidos) {
                    val nombre = "${cliente["apellido"]}, ${cliente["nombre"]} (${cliente["tipo"]})"
                    val fechaLegible = cliente["fecha"]?.toLongOrNull()?.let { sdf.format(Date(it)) } ?: "Sin pagos"
                    val detalle = "Último pago: $fechaLegible"
                    val tv = TextView(this)
                    tv.text = "$nombre\n$detalle"

                    val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                    params.setMargins(0, 0, 0, 24)
                    tv.layoutParams = params
                    tv.setPadding(32, 24, 32, 24)
                    val drawable = GradientDrawable()
                    drawable.cornerRadius = 32f
                    drawable.setColor(0xFFFFCDD2.toInt())
                    tv.background = drawable
                    tv.setTypeface(null, Typeface.BOLD)
                    tv.textSize = 16f
                    layout.addView(tv)
                }
            }
        }

    }
}