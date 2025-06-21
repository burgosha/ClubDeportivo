package com.example.clubdeportivo

import android.os.Bundle
import android.view.View
import android.widget.*
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
        configurarResumen()

        btnCobrar.setOnClickListener {
            registrarPago()
        }

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
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

    private fun configurarResumen() {
        val listener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                actualizarResumen()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        spinnerSocio.onItemSelectedListener = listener
        spinnerMetodoPago.onItemSelectedListener = listener

        tvMonto.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                actualizarResumen()
            }
        }
    }

    private fun actualizarResumen() {
        if (socios.isEmpty() || spinnerSocio.selectedItemPosition !in socios.indices) return
        if (spinnerMetodoPago.selectedItem == null) return

        val socio = socios[spinnerSocio.selectedItemPosition]
        val metodo = spinnerMetodoPago.selectedItem.toString()
        val monto = tvMonto.text.toString().ifBlank { "$0.00" }

        val resumen = """
            Socio: ${socio.apellido}, ${socio.nombre}
            Monto: $monto
            Método: $metodo
        """.trimIndent()

        tvResumen.text = resumen
    }

    private fun registrarPago() {
        if (socios.isEmpty() || spinnerSocio.selectedItemPosition !in socios.indices) {
            Toast.makeText(this, "Seleccione un socio válido", Toast.LENGTH_SHORT).show()
            return
        }

        val socio = socios[spinnerSocio.selectedItemPosition]
        val metodo = spinnerMetodoPago.selectedItem?.toString() ?: ""
        val montoTexto = tvMonto.text.toString()
            .replace(",", ".")
            .replace("$", "")
            .trim()

        val monto = montoTexto.toDoubleOrNull()
        if (monto == null || monto <= 0.0) {
            Toast.makeText(this, "Ingrese un monto válido", Toast.LENGTH_SHORT).show()
            return
        }

        val exito = dbHelper.insertarPago(socio.id, monto, metodo)
        if (exito) {
            Toast.makeText(this, "Pago registrado exitosamente", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Error al registrar el pago", Toast.LENGTH_SHORT).show()
        }
    }
}
