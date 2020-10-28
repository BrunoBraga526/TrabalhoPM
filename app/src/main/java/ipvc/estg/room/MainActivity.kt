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
import ipvc.estg.room.adapters.CityAdapter
import ipvc.estg.room.entities.City
import ipvc.estg.room.viewModel.CityViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var cityViewModel: CityViewModel
    private val newWordActivityRequestCode = 1
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // recycler view
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = CityAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // view model
        cityViewModel = ViewModelProvider(this).get(CityViewModel::class.java)
        cityViewModel.allCities.observe(this, Observer { cities ->
            // Update the cached copy of the words in the adapter.
            cities?.let { adapter.setCities(it) }
        })

        //Fab
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, AddCity::class.java)
            startActivityForResult(intent, newWordActivityRequestCode)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == newWordActivityRequestCode && resultCode == Activity.RESULT_OK) {
            val pcity = data?.getStringExtra(AddCity.EXTRA_REPLY_CITY)
            val pcountry = data?.getStringExtra(AddCity.EXTRA_REPLY_COUNTRY)

            if (pcity!= null && pcountry != null) {
                val city = City(city = pcity, country = pcountry)
                cityViewModel.insert(city)
            }

        } else {
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG).show()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.apagartudo -> {
                cityViewModel.deleteAll()
                true
            }

            R.id.cidadesPortugal -> {

                // recycler view
                val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
                val adapter = CityAdapter(this)
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(this)

                // view model
                cityViewModel = ViewModelProvider(this).get(CityViewModel::class.java)
                cityViewModel.getCitiesByCountry("Portugal").observe(this, Observer { cities ->
                    // Update the cached copy of the words in the adapter.
                    cities?.let { adapter.setCities(it) }
                })

                true
            }

            R.id.todasCidades -> {

                // recycler view
                val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
                val adapter = CityAdapter(this)
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(this)

                // view model
                cityViewModel = ViewModelProvider(this).get(CityViewModel::class.java)
                cityViewModel.allCities.observe(this, Observer { cities ->
                    // Update the cached copy of the words in the adapter.
                    cities?.let { adapter.setCities(it) }
                })


                true
            }

            R.id.getCountryFromAveiro -> {
                cityViewModel = ViewModelProvider(this).get(CityViewModel::class.java)
                cityViewModel.getCountryFromCity("Aveiro").observe(this, Observer { city ->
                    Toast.makeText(this, city.country, Toast.LENGTH_SHORT).show()
                })
                true
            }

            R.id.apagarAveiro -> {
                cityViewModel.deleteByCity("Aveiro")
                true
            }

            R.id.alterar -> {
                val city = City(id = 1, city = "alterado", country = "alterado")
                cityViewModel.updateCity(city)
                true
            }

            R.id.alteraraveiro -> {
                cityViewModel.updateCountryFromCity("Aveiro", "Portugal alterado")
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

}