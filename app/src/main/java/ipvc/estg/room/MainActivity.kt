package ipvc.estg.room

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ipvc.estg.room.adapters.NotaAdapter
import ipvc.estg.room.entities.Nota
import ipvc.estg.room.viewModel.NotaViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var notaViewModel: NotaViewModel
    private val newWordActivityRequestCode = 1
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // recycler view
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = NotaAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // view model
        notaViewModel = ViewModelProvider(this).get(NotaViewModel::class.java)
        notaViewModel.allNotas.observe(this, Observer { notas ->
            // Update the cached copy of the words in the adapter.
            notas?.let { adapter.setNotas(it) }
        })

        //Fab
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, AddNota::class.java)
            startActivityForResult(intent, newWordActivityRequestCode)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == newWordActivityRequestCode && resultCode == Activity.RESULT_OK) {
            val pnota = data?.getStringExtra(AddNota.EXTRA_REPLY_NOTA)
            val ptexto = data?.getStringExtra(AddNota.EXTRA_REPLY_TEXTO)

            if (pnota!= null && ptexto != null) {
                val nota = Nota(nota = pnota, texto = ptexto)
                notaViewModel.insert(nota)
            }

        } else {
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG).show()
        }
    }

        //teste de git networking
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.apagartudo -> {
                notaViewModel.deleteAll()
                true
            }

            R.id.textosPortugal -> {

                // recycler view
                val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
                val adapter = NotaAdapter(this)
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(this)

                // view model
                notaViewModel = ViewModelProvider(this).get(NotaViewModel::class.java)
                notaViewModel.getNotaByTexto("Portugal").observe(this, Observer { notas ->
                    // Update the cached copy of the words in the adapter.
                    notas?.let { adapter.setNotas(it) }
                })

                true
            }

            R.id.todasNotas -> {

                // recycler view
                val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
                val adapter = NotaAdapter(this)
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(this)

                // view model
                notaViewModel = ViewModelProvider(this).get(NotaViewModel::class.java)
                notaViewModel.allNotas.observe(this, Observer { notas ->
                    // Update the cached copy of the words in the adapter.
                    notas?.let { adapter.setNotas(it) }
                })


                true
            }

            R.id.getTextoFromNota -> {
                notaViewModel = ViewModelProvider(this).get(NotaViewModel::class.java)
                notaViewModel.getTextoFromNota("Aveiro").observe(this, Observer { nota ->
                    Toast.makeText(this, nota.texto, Toast.LENGTH_SHORT).show()
                })
                true
            }

            R.id.apagarAveiro -> {
                notaViewModel.deleteByNota("Aveiro")
                true
            }

            R.id.alterar -> {
                val nota = Nota(id = 1, nota = "alterado", texto = "alterado")
                notaViewModel.updateNota(nota)
                true
            }

            R.id.alteraraveiro -> {
                notaViewModel.updateTextoFromNota("Aveiro", "Portugal alterado")
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

}