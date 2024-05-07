package com.example.sqllitee

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity(var adapter: NotasAdapter?=null) : AppCompatActivity() {

    var listaDeNotas = ArrayList<Notas>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar) // Define el Toolbar como la barra de acción

        //agregamos las notas de la base de datos
        val baseDatos = DBManager(this)
        listaDeNotas =
            baseDatos.selectNotas(arrayOf("ID", "Titulo", "Descripcion"), null, null, null)

        adapter = NotasAdapter(this, listaDeNotas)
        var listView: ListView = findViewById(R.id.listView)
        listView.adapter = adapter
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        val buscar = menu!!.findItem(R.id.app_bar_search).actionView as SearchView
        val manejador = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        buscar.setSearchableInfo(manejador.getSearchableInfo(componentName))
        buscar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(applicationContext, query, Toast.LENGTH_LONG).show()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item!!.itemId) {
            R.id.menuAgregar -> {
                var intent = Intent(this, AddActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //onResume se ejecuta cada vez que la actividad se vuelve a mostrar
    override fun onResume() {
        super.onResume()
        val baseDatos = DBManager(this)
        listaDeNotas =
            baseDatos.selectNotas(arrayOf("ID", "Titulo", "Descripcion"), null, null, null)
        adapter = NotasAdapter(this, listaDeNotas)
        var listView: ListView = findViewById(R.id.listView)
        listView.adapter = adapter
    }

    class NotasAdapter(contexto: Context, var listaDeNotas: ArrayList<Notas>) : BaseAdapter() {
        var contexto: Context? = contexto
        var currentId: Int? = null
        override fun getCount(): Int {
            return listaDeNotas.size
        }

        override fun getItem(position: Int): Any {
            return listaDeNotas[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val nota = listaDeNotas[position]
            val inflater = contexto!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val miVista = inflater.inflate(R.layout.molde_notas, null)

            miVista.findViewById<TextView>(R.id.textViewTitulo).setText(nota.titulo)
            miVista.findViewById<TextView>(R.id.textViewContenido).setText(nota.descripcion)

            // Encuentra el botón de eliminar en la vista y establece un OnClickListener
            val btnEliminar = miVista.findViewById<ImageView>(R.id.btnEliminar)
            btnEliminar.setOnClickListener {
                eliminarNota(nota.notasID)
            }

            // Encuentra el botón de editar en la vista y establece un OnClickListener
            val btnEditar = miVista.findViewById<ImageView>(R.id.btnEditar)
            btnEditar.setOnClickListener {
                editarNota(nota.notasID)
            }


            return miVista
        }

        // Función para eliminar una nota
        fun eliminarNota(notaId: Int?) {
            val baseDatos = DBManager(contexto!!)
            if (notaId != null) {
                val count = baseDatos.delete("ID=?", arrayOf(notaId.toString()))
                if (count > 0) {
                    Toast.makeText(contexto, "Nota eliminada", Toast.LENGTH_LONG).show()
                    // Actualiza la lista de notas
                    listaDeNotas = baseDatos.selectNotas(arrayOf("ID", "Titulo", "Descripcion"), null, null, null)
                    notifyDataSetChanged()

                } else {
                    Toast.makeText(contexto, "Nota no eliminada", Toast.LENGTH_LONG).show()
                }
            }
        }

        //funcion para al dar click en editar una nota nos lleve a la actividad de editar
        fun editarNota(notaId: Int?) {
            val intent = Intent(contexto, AddActivity::class.java)
            intent.putExtra("ID", notaId)
            intent.putExtra("Titulo", listaDeNotas.find { it.notasID == notaId }?.titulo)
            intent.putExtra("Descripcion", listaDeNotas.find { it.notasID == notaId }?.descripcion)
            contexto!!.startActivity(intent)
        }

    }


}
