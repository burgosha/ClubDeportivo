package com.example.clubdeportivo

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CobroCuotas : AppCompatActivity() {
    private lateinit var spinnerSocio: Spinner
    private lateinit var spinnerMetodoPago: Spinner
    private lateinit var tvMonto: EditText
    private lateinit var tvResumen: TextView
    private lateinit var btnCobrar: Button
    private lateinit var dbHelper: SQLiteHelper
    private lateinit var socios: List<Cliente>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cobro_cuotas)
        dbHelper = SQLiteHelper(this)

        spinnerSocio = findViewById(R.id.spinnerSocio)
        spinnerMetodoPago = findViewById(R.id.spinnerMetodoPago)
        tvMonto = findViewById(R.id.tvMonto)
        tvResumen = findViewById(R.id.tvResumen)
        btnCobrar = findViewById(R.id.btnCobrar)

        cargarSocios()
        configurarMetodoPago()

        spinnerSocio.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                actualizarResumen()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        })

        spinnerMetodoPago.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                actualizarResumen()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        })

        btnCobrar.setOnClickListener {
            val socioSeleccionado = socios[spinnerSocio.selectedItemPosition]
            val montoStr = tvMonto.text.toString().replace("$", "").replace(",", ".")
            val metodoPago = spinnerMetodoPago.selectedItem.toString()

            val monto = montoStr.toDoubleOrNull()
            if (monto == null || monto <= 0) {
                Toast.makeText(this, "Ingrese un monto válido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val exito = dbHelper.insertarPago(socioSeleccionado.id, monto, metodoPago)
            if (exito) {
                Toast.makeText(this, "Pago registrado exitosamente", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Error al registrar el pago", Toast.LENGTH_SHORT).show()
            }
        }

        val btnBack: ImageButton = findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun cargarSocios() {
        socios = dbHelper.obtenerClientes().filter { it.tipo == "Socio" }
        val nombres = socios.map { "${it.apellido}, ${it.nombre}" }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, nombres)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSocio.adapter = adapter
    }

    private fun configurarMetodoPago() {
        val metodos = listOf("Efectivo", "Tarjeta crédito", "Tarjeta débito", "Transferencia")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, metodos)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMetodoPago.adapter = adapter
    }

    private fun actualizarResumen() {
        if (spinnerSocio.selectedItemPosition >= 0 && spinnerMetodoPago.selectedItemPosition >= 0) {
            val socio = socios[spinnerSocio.selectedItemPosition]
            val metodo = spinnerMetodoPago.selectedItem.toString()
            val monto = tvMonto.text.toString()
            val resumen = "Socio: ${socio.apellido}, ${socio.nombre}\nMonto: $monto\nMétodo: $metodo"
            tvResumen.text = resumen
        }
    }
}