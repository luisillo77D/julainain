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
    //al abrir la actividad se posicionan los extras en los campos de texto, en caso de que se haya seleccionado una nota
    override fun onStart() {
        super.onStart()
        val extras = intent.extras
        if (extras != null) {
            val titulo = extras.getString("Titulo")
            val descripcion = extras.getString("Descripcion")
            val editTextTitulo: EditText = findViewById(R.id.editTextTextTitulo)
            val editTextDescripcion: EditText = findViewById(R.id.editTextTextDescripcion)
            editTextTitulo.setText(titulo)
            editTextDescripcion.setText(descripcion)
        }
    }

    fun btnAdd(view:View){
       val editTextTitulo : EditText = findViewById(R.id.editTextTextTitulo)
        val editTextDescripcion: EditText = findViewById(R.id.editTextTextDescripcion)
        val baseDatos = DBManager(this)

        val values = ContentValues()
        values.put("Titulo",editTextTitulo.text.toString())
        values.put("Descripcion",editTextDescripcion.text.toString())

        //en caso de que se haya seleccionado una nota, se actualiza
        if (intent.extras != null){
            val selectionArgs = arrayOf(intent.extras!!.getInt("ID").toString())
            val selection = "ID=?"
            val ID = baseDatos.update(values,selection,selectionArgs)
            if (ID>0){
                Toast.makeText(this,"registro actualizado",Toast.LENGTH_LONG).show()
                finish()
            }else{
                Toast.makeText(this,"registro no actualizado",Toast.LENGTH_LONG).show()
            }
            return
        }else{
            //en caso de que no se haya seleccionado una nota, se inserta
            val ID = baseDatos.insert(values)

            if (ID>0){
                Toast.makeText(this,"registro guardado",Toast.LENGTH_LONG).show()
                finish()
            }else{
                Toast.makeText(this,"registro no realizado",Toast.LENGTH_LONG).show()
            }
        }

    }

}