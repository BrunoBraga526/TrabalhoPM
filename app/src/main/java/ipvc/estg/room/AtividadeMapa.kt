package ipvc.estg.room

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.Marker
import ipvc.estg.room.API_Login.PostLogin
import ipvc.estg.room.entities.Problema
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AtividadeMapa : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mMap: GoogleMap
    private var LOCATION_PERMISSION_REQUEST_CODE=1
    lateinit var longitude:String
    lateinit var latitude:String

    override fun onCreate(savedInstanceState: Bundle?) {
        longitude = ""
        latitude = ""

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.atividade_mapa)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val request = Servicos.buildServico(PostLogin::class.java)
        val call = request.getProblemas()
        var position: LatLng

        call.enqueue(object : Callback<List<Problema>> {
            override fun onResponse(call: Call<List<Problema>>, response: Response<List<Problema>>) {
                if (response.isSuccessful) {
                    val c = response.body()!!

                    for (problema in c) {
                        position = LatLng(problema.latitude.toDouble(), problema.longitude.toDouble())
                        var marcador = mMap.addMarker(MarkerOptions().position(position).title("${problema.descricao}"))
                        marcador.tag = problema.id
                    }
                }
            }

            override fun onFailure(call: Call<List<Problema>>, t: Throwable) {
            }
        })
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
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        setUpMap()
        mMap.setOnMarkerClickListener( object: GoogleMap.OnMarkerClickListener {
            override fun onMarkerClick(p0: Marker): Boolean {

                val intent = Intent(this@AtividadeMapa, problema::class.java)
                setUpMap()
                intent.putExtra(problema.EXTRA_LAT, latitude)
                intent.putExtra(problema.EXTRA_LON, longitude)
                intent.putExtra(problema.EXTRA_MSG, p0.tag.toString())
                startActivity(intent)
                return false
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_mapa, menu)
        return true
    }
    fun setUpMap(){
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }else {
            mMap.isMyLocationEnabled = true
            fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
                if (location != null) {
                    latitude = location.latitude.toString()
                    longitude = location.longitude.toString()


                }
            }}}
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.botao_novo_marcador -> {
                setUpMap()
                val intent = Intent(this, NovoMarcador::class.java)
                intent.putExtra(NovoMarcador.EXTRA_LAT, latitude)
                intent.putExtra(NovoMarcador.EXTRA_LON, longitude)
                startActivity(intent)
                true
            }
            R.id.botao_logout -> {
                val sharedPref: SharedPreferences = getSharedPreferences(
                    getString(R.string.preference_file_key), Context.MODE_PRIVATE)
                with(sharedPref.edit()) {
                    putBoolean(getString(R.string.keeper_loginauto_valida), false)
                    putString(getString(R.string.keeper_loginauto_utilizador), null)
                    commit()
                }

                val intent = Intent(this@AtividadeMapa, MainActivity::class.java)
                startActivity(intent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}