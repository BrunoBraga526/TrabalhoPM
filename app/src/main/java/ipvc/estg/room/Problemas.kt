package ipvc.estg.room

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import ipvc.estg.room.*
import ipvc.estg.room.API_Login.PostLogin
import ipvc.estg.room.entities.Problema

private lateinit var editar_descricao_problema: EditText
private lateinit var editar_latitude_problema: TextView
private lateinit var editar_longitude_problema: TextView
private lateinit var editar_tipo_problema: TextView


private lateinit var descricaoView: TextView
private lateinit var latitudeView: TextView
private lateinit var longitudeView: TextView
private lateinit var tipoView: TextView


class problema : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            val sharedPref: SharedPreferences = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE )

            val utilizador: String = sharedPref.getString(getString(R.string.keeper_loginauto_utilizador), null)!!
            var tipo=String()
            val intent = intent
            val id = intent.getStringExtra(problema.EXTRA_MSG)
            val latitude = intent.getStringExtra(problema.EXTRA_LAT)
            val longitude = intent.getStringExtra(problema.EXTRA_LON)
            val call_id= id?.toInt()
            val request = Servicos.buildServico(PostLogin::class.java)
            val call = request.getProblema(call_id!!)
            call.enqueue(object : Callback<List<Problema>> {
                override fun onResponse(call: Call<List<Problema>>, response: Response<List<Problema>>) {
                    if (response.isSuccessful) {
                        val c = response.body()!!
                        for (problema in c) {
                            if (problema.utilizador.equals(utilizador)) {
                                setContentView(R.layout.atividade_ticket_dono)
                                editar_descricao_problema = findViewById(R.id.editar_problema)
                                editar_latitude_problema = findViewById(R.id.editar_latitude)
                                editar_longitude_problema = findViewById(R.id.editar_longitude)
                                editar_tipo_problema = findViewById(R.id.editar_tipo)
                                editar_descricao_problema.setText(problema.descricao)
                                editar_latitude_problema.text = problema.latitude
                                editar_longitude_problema.text = problema.longitude
                                editar_tipo_problema.text = problema.tipo

                                val local = findViewById<Button>(R.id.botao_localizacao)
                                local.setOnClickListener {
                                    editar_latitude_problema.text = latitude
                                    editar_longitude_problema.text = longitude
                                }
                                val type = findViewById<Button>(R.id.botao_tipo)
                                type.setOnClickListener {
                                    editar_tipo_problema.text = tipo
                                }
                                val types = resources.getStringArray(R.array.TiposProblema)

                                val dropdown = findViewById<Spinner>(R.id.dropdown_novo_marcador)
                                if (dropdown != null) {
                                    val adapter = ArrayAdapter(this@problema, android.R.layout.simple_spinner_item, types)
                                    dropdown.adapter = adapter

                                    dropdown.onItemSelectedListener = object :
                                        AdapterView.OnItemSelectedListener {
                                        override fun onItemSelected(parent: AdapterView<*>,
                                                                    view: View, position: Int, id: Long) {
                                            tipo = types[position]
                                        }

                                        override fun onNothingSelected(parent: AdapterView<*>) {
                                        }
                                    }
                                }
                                val save = findViewById<Button>(R.id.botao_guardar)
                                save.setOnClickListener {
                                    val callid = id?.toInt()
                                    val calltipo = editar_tipo_problema.text.toString()
                                    val calltexto = editar_descricao_problema.text.toString()
                                    val calllatitude = editar_latitude_problema.text.toString()
                                    val calllongitude = editar_longitude_problema.text.toString()
                                    val request = Servicos.buildServico(PostLogin::class.java)
                                    val call = request.editarProblema(
                                        callid,
                                        calltipo,
                                        calltexto,
                                        calllatitude,
                                        calllongitude
                                    )
                                    call.enqueue(object : Callback<Output_Problema> {
                                        override fun onResponse(call: Call<Output_Problema>, response: Response<Output_Problema>) {
                                            if (response.isSuccessful) {
                                                val c = response.body()!!

                                                if (c.sucesso) {
                                                    Toast.makeText(this@problema, "Sucesso", Toast.LENGTH_SHORT).show()
                                                    val intent = Intent(this@problema, AtividadeMapa::class.java)
                                                    startActivity(intent)
                                                } else Toast.makeText(
                                                    this@problema, "Falha", Toast.LENGTH_LONG
                                                ).show()
                                            }
                                        }

                                        override fun onFailure(call: Call<Output_Problema>, t: Throwable) {
                                            Toast.makeText(this@problema, "Falhou", Toast.LENGTH_SHORT).show()
                                        }
                                    })
                                }
                                val removerProblema = findViewById<Button>(R.id.botao_remover)
                                removerProblema.setOnClickListener {
                                    val callid = id?.toInt()
                                    val request = Servicos.buildServico(PostLogin::class.java)
                                    val call = request.removerProblema(callid)
                                    call.enqueue(object : Callback<Output_Login> {
                                        override fun onResponse(
                                            call: Call<Output_Login>,
                                            response: Response<Output_Login>
                                        ) {
                                            if (response.isSuccessful) {
                                                val c = response.body()!!

                                                if (c.sucesso) {
                                                    Toast.makeText(this@problema, "Sucesso", Toast.LENGTH_SHORT).show()
                                                    val intent = Intent(this@problema, AtividadeMapa::class.java)
                                                    startActivity(intent)
                                                } else Toast.makeText(
                                                    this@problema, "Falhou", Toast.LENGTH_LONG
                                                ).show()
                                            }
                                        }

                                        override fun onFailure(
                                            call: Call<Output_Login>,
                                            t: Throwable
                                        ) {
                                            Toast.makeText(this@problema, "Falhou", Toast.LENGTH_SHORT).show()

                                        }
                                    })
                                }

                            } else {
                                setContentView(R.layout.atividade_ticket_naodono)
                                descricaoView = findViewById(R.id.problema)
                                latitudeView = findViewById(R.id.latitude_naodono)
                                longitudeView = findViewById(R.id.longitude_naodono)
                                tipoView = findViewById(R.id.tipo)
                                descricaoView.text = problema.descricao
                                latitudeView.text = problema.latitude
                                longitudeView.text = problema.longitude
                                tipoView.text = problema.tipo
                            }

                        }
                    }
                }


                override fun onFailure(call: Call<List<Problema>>, t: Throwable) {
                    Toast.makeText(this@problema, "Falhou", Toast.LENGTH_SHORT).show()
                }
            })
        }
        companion object {
            const val EXTRA_MSG = "com.example.android.wordlistsql.MSG"
            const val EXTRA_LAT = "com.example.android.wordlistsql.LAT"
            const val EXTRA_LON = "com.example.android.wordlistsql.LON"
        }
    }
