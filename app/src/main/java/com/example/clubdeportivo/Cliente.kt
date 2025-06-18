package com.example.clubdeportivo

data class Cliente(
    val id: Int,
    val nombre: String,
    val apellido: String,
    val fechaNac: String,
    val dni: String,
    val direccion: String,
    val telefono: String,
    val tipo: String
) 