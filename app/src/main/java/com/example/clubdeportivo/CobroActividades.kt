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

class CobroActividades : AppCompatActivity() {
    private lateinit var spinnerNoSocio: Spinner
    private lateinit var spinnerActividad: Spinner
    private lateinit var spinnerMetodoPago: Spinner
    private lateinit var etMonto: EditText
    private lateinit var tvResumen: TextView
    private lateinit var btnCobrar: Button

    private lateinit var dbHelper: SQLiteHelper
    private lateinit var noSocios: List<Cliente>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cobro_actividades)

        dbHelper = SQLiteHelper(this)

        spinnerNoSocio = findViewById(R.id.spinnerNoSocio)
        spinnerActividad = findViewById(R.id.spinnerActividad)
        spinnerMetodoPago = findViewById(R.id.spinnerMetodoPago)
        etMonto = findViewById(R.id.etMonto)
        tvResumen = findViewById(R.id.tvResumen)
        btnCobrar = findViewById(R.id.btnCobrar)

        cargarNoSocios()
        configurarResumen()

        btnCobrar.setOnClickListener {
            registrarPago()
        }

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish()
        }
    }

    private fun cargarNoSocios() {
        noSocios = dbHelper.obtenerClientes().filter { it.tipo == "No Socio" }
        val nombres = noSocios.map { "${it.apellido}, ${it.nombre}" }

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, nombres)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerNoSocio.adapter = adapter
    }

    private fun configurarResumen() {
        val listener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                actualizarResumen()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        spinnerNoSocio.onItemSelectedListener = listener
        spinnerActividad.onItemSelectedListener = listener
        spinnerMetodoPago.onItemSelectedListener = listener
        etMonto.setOnFocusChangeListener { _, _ -> actualizarResumen() }
    }

    private fun actualizarResumen() {
        if (spinnerNoSocio.selectedItemPosition < 0 || spinnerActividad.selectedItemPosition < 0 || spinnerMetodoPago.selectedItemPosition < 0) return

        val cliente = noSocios[spinnerNoSocio.selectedItemPosition]
        val actividad = spinnerActividad.selectedItem.toString()
        val metodo = spinnerMetodoPago.selectedItem.toString()
        val montoTexto = etMonto.text.toString().ifBlank { "$0.00" }

        val resumen = "No Socio: ${cliente.apellido}, ${cliente.nombre}\nActividad: $actividad\nMonto: $montoTexto\nMétodo: $metodo"
        tvResumen.text = resumen
    }

    private fun registrarPago() {
        if (noSocios.isEmpty()) {
            Toast.makeText(this, "No hay No Socios cargados", Toast.LENGTH_SHORT).show()
            return
        }

        val cliente = noSocios[spinnerNoSocio.selectedItemPosition]
        val montoTexto = etMonto.text.toString().replace(",", ".").replace("$", "").trim()
        val monto = montoTexto.toDoubleOrNull()
        val metodo = spinnerMetodoPago.selectedItem.toString()

        if (monto == null || monto <= 0) {
            Toast.makeText(this, "Ingrese un monto válido", Toast.LENGTH_SHORT).show()
            return
        }

        val exito = dbHelper.insertarPago(cliente.id, monto, metodo)

        if (exito) {
            Toast.makeText(this, "Pago registrado correctamente", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Error al registrar el pago", Toast.LENGTH_SHORT).show()
        }
    }
}