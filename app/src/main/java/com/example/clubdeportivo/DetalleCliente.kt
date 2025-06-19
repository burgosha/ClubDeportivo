package com.example.clubdeportivo

import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
        val layoutPagos: LinearLayout = findViewById(R.id.layoutPagosCliente)

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

        val dbHelper = SQLiteHelper(this)
        val pagos: List<Map<String, String>> = if (id != -1) dbHelper.obtenerPagosDeCliente(id) else emptyList()
        if (pagos.isEmpty()) {
            val tv = TextView(this)
            tv.text = "No hay pagos registrados."
            layoutPagos.addView(tv)
        } else {
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            for (i in pagos.indices) {
                val pago = pagos[i]
                val fechaLegible = try {
                    val fechaLong = pago["fecha"]?.toLongOrNull() ?: 0L
                    sdf.format(Date(fechaLong))
                } catch (e: Exception) {
                    pago["fecha"] ?: ""
                }
                val tv = TextView(this)
                tv.text = "Monto: $${pago["monto"]} - Método: ${pago["metodo_pago"]}\nFecha: $fechaLegible"
                tv.setTypeface(null, Typeface.BOLD)
                tv.textSize = 16f
                val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                params.setMargins(0, 0, 0, 16)
                tv.layoutParams = params
                tv.setPadding(32, 24, 32, 24)
                val drawable = GradientDrawable()
                drawable.cornerRadius = 32f
                drawable.setColor(0xFFE1BEE7.toInt())
                tv.background = drawable
                layoutPagos.addView(tv)
            }
        }
    }
} 