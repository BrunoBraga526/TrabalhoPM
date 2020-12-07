package ipvc.estg.room

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem



class AtividadeMapa : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    //definição do layout do mapa
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.atividade_mapa)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    //quando a atividade do mapa está pronto cria ponto
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val braga = LatLng(41.545434777333256, -8.409669231151259) //define as coordenadas da variavel "braga"
        mMap.addMarker(MarkerOptions().position(braga).title("Marcador em BRAGA!!!")) //adciona um marcador nas coordenadas de braga e titulo
        mMap.moveCamera(CameraUpdateFactory.newLatLng(braga)) //move o mapa para centrar no marcador
    }

    //criação do menu do mapa
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_mapa, menu)
        return true
    }

    //opções do menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.botao_novo_marcador -> { //botao para novo marcador, criar posicionamento auto, ligar os problemas aqui
                true
            }

            //botao logout insere valores null nos campos, torna o check falso para nao iniciar o login automatico
            R.id.botao_logout -> {
                val sharedPref: SharedPreferences = getSharedPreferences(
                    getString(R.string.preference_file_key), Context.MODE_PRIVATE )
                with ( sharedPref.edit() ) {
                    putBoolean(getString(R.string.keeper_loginauto_valida), false )
                    putString(getString(R.string.keeper_loginauto_utilizador), null )
                    putString(getString(R.string.keeper_loginauto_palavrapasse), null )
                    commit()
                }

                //volta a atividade login (Main)
                val intent = Intent(this@AtividadeMapa, MainActivity::class.java)
                startActivity(intent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}