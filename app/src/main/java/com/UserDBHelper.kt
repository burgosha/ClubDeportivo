package com

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class UserDBHelper(context: Context): SQLiteOpenHelper(context, "clubdeportivo", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
    db.execSQL("""
            CREATE TABLE usuarios (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT UNIQUE,
                contrasenia TEXT
            )
        """.trimIndent())
    db.execSQL("INSERT INTO usuarios (nombre, contrasenia) VALUES ('admin', '1234')")
}

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun login(nombre: String, contrasenia: String): Boolean{
        var db = readableDatabase
        var cursor = db.rawQuery(
            "SELECT * FROM usuarios WHERE nombre=? AND contrasenia=?",
            arrayOf(nombre, contrasenia)
        )
        var existe = cursor.count > 0
        return existe
    }
}