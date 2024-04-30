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
    //obtenemos todas las notas y las regresamos como un arraylist de notas, ejecutando un execsql
    fun selectNotas(columnas:Array<String>, selection:String?, selectionArgs:Array<String>?, groupBy:String?):ArrayList<Notas>{
        val cursor = sqlDB!!.query(dbTabla,columnas,selection,selectionArgs,groupBy,null,null)
        val listaNotas = ArrayList<Notas>()
        while (cursor.moveToNext()){
            listaNotas.add(Notas(cursor.getInt(0),cursor.getString(1),cursor.getString(2)))
        }
        return listaNotas
    }

    //funcion para actualizar una nota
    fun update(values:ContentValues, selection:String, selectionArgs:Array<String>):Int{
        val count = sqlDB!!.update(dbTabla,values,selection,selectionArgs)
        return count
    }

    //funcion para eliminar una nota con el id
    fun delete(selection: String, selectionArgs: Array<String>):Int{
        val count = sqlDB!!.delete(dbTabla,selection,selectionArgs)
        return count
    }

    //funcion para regresar una nota buscando por id
    fun selectNota(columnas:Array<String>, selection:String, selectionArgs:Array<String>):Notas{
        val cursor = sqlDB!!.query(dbTabla,columnas,selection,selectionArgs,null,null,null)
        cursor.moveToFirst()
        return Notas(cursor.getInt(0),cursor.getString(1),cursor.getString(2))
    }





}