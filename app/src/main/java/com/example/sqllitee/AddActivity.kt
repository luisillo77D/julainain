package com.example.sqllitee

import android.content.ContentValues
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AddActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun btnAdd(view:View){
       val editTextTitulo : EditText = findViewById(R.id.editTextTextTitulo)
        val editTextDescripcion: EditText = findViewById(R.id.editTextTextDescripcion)

        val baseDatos = DBManager(this)

        val values = ContentValues()
        values.put("Titulo",editTextTitulo.text.toString())
        values.put("Descripcion",editTextDescripcion.text.toString())

        val ID = baseDatos.insert(values)

        if (ID>0){
            Toast.makeText(this,"registro guardado",Toast.LENGTH_LONG).show()
            finish()
        }else{
            Toast.makeText(this,"registro no realizado",Toast.LENGTH_LONG).show()
        }
    }
}