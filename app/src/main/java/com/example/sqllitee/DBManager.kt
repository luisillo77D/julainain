package com.example.sqllitee

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class DBManager {

    val dbNombre = "MisNotas"
    val dbTabla = "Notas"
    val ColumnaID = "ID"
    val ColumnaTitulo = "Titulo"
    val ColumnaDescripcion = "Descripcion"
    val dbVersion = 1

    val sqlCrearTabla = "CREATE TABLE IF NOT EXISTS " + dbTabla + " (" + ColumnaID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+ColumnaTitulo + " TEXT NOT NULL," + ColumnaDescripcion +" TEXT NOT NULL)"



    constructor(contexto: Context){
        val db=DBHelperNotas(contexto)
        sqlDB = db.writableDatabase
    }
    var sqlDB:SQLiteDatabase?=null
    inner  class DBHelperNotas(contexto:Context):SQLiteOpenHelper(contexto,dbNombre,null,dbVersion){
        var contexto:Context?=contexto
        override fun onCreate(db: SQLiteDatabase?) {
            db!!.execSQL(sqlCrearTabla)
            Toast.makeText(this.contexto,"Base de datos creada",Toast.LENGTH_LONG).show()
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db!!.execSQL("DROP TABLE IF EXISTS"+dbTabla)
        }

    }

    fun insert(values:ContentValues):Long{
        val ID =sqlDB!!.insert(dbTabla,"",values)

        return ID
    }


}