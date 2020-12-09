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


class Problemas : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            val sharedPref: SharedPreferences = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE )


            //inicia variaveis e realiza as calls para popular o mapa
            val utilizador: String = sharedPref.getString(getString(R.string.automatic_login_username), null)!!
            var tipo=String()
            val intent = intent
            //popula com os valores retirados dos companions
            val id = intent.getStringExtra(EXTRA_MENSAGEM)
            val latitude = intent.getStringExtra(EXTRA_LATITUDE)
            val longitude = intent.getStringExtra(EXTRA_LONGITUDE)
            val call_id= id?.toInt()
            val request = Servicos.buildServico(PostLogin::class.java)
            //define a call para receber os problemas da API
            val call = request.getProblema(call_id!!)
            call.enqueue(object : Callback<List<Problema>> {
                override fun onResponse(call: Call<List<Problema>>, response: Response<List<Problema>>) {
                    if (response.isSuccessful) {
                        val c = response.body()!!
                        for (problema in c) { //percorre os problemas e os que utilizador = utilizador criou problema, aponta xml do dono
                            if (problema.utilizador.equals(utilizador)) {
                                setContentView(R.layout.atividade_problema_dono)
                                editar_descricao_problema = findViewById(R.id.editar_problema)
                                editar_latitude_problema = findViewById(R.id.editar_latitude)
                                editar_longitude_problema = findViewById(R.id.editar_longitude)
                                editar_tipo_problema = findViewById(R.id.editar_tipo)
                                editar_descricao_problema.setText(problema.descricao)
                                editar_latitude_problema.text = problema.latitude
                                editar_longitude_problema.text = problema.longitude
                                editar_tipo_problema.text = problema.tipo

                                //botao para alterar a localização
                                val local = findViewById<Button>(R.id.botao_localizacao)
                                local.setOnClickListener {
                                    editar_latitude_problema.text = latitude
                                    editar_longitude_problema.text = longitude
                                }

                                //botao para alterar o tipo pelo que está no dropdown
                                val type = findViewById<Button>(R.id.botao_tipo)
                                type.setOnClickListener {
                                    editar_tipo_problema.text = tipo
                                }

                                //dropdown dos tipos de problemas definidos nas strings
                                val types = resources.getStringArray(R.array.TiposProblema)
                                val spinner = findViewById<Spinner>(R.id.dropdown1)
                                if (spinner != null) {
                                    val adapter = ArrayAdapter(
                                        this@Problemas,
                                        android.R.layout.simple_spinner_item,
                                        types
                                    )
                                    spinner.adapter = adapter

                                    spinner.onItemSelectedListener = object :
                                        AdapterView.OnItemSelectedListener {
                                        override fun onItemSelected(parent: AdapterView<*>,view: View, position: Int, id: Long) {
                                            tipo = types[position]
                                        }
                                        override fun onNothingSelected(parent: AdapterView<*>) {
                                        }
                                    }
                                }
                                //chama a api para guardar os valores dos campos de texto/ seleçoes na BD
                                val save = findViewById<Button>(R.id.botao_guardar)
                                save.setOnClickListener {
                                    val callid = id?.toInt()
                                    val calltipo = editar_tipo_problema.text.toString()
                                    val calldescricao = editar_descricao_problema.text.toString()
                                    val calllatitude = editar_latitude_problema.text.toString()
                                    val calllongitude = editar_longitude_problema.text.toString()
                                    val request = Servicos.buildServico(PostLogin::class.java)
                                    val call = request.editarProblema(
                                        callid,
                                        calltipo,
                                        calldescricao,
                                        calllatitude,
                                        calllongitude
                                    )
                                    call.enqueue(object : Callback<Output_Problema> {
                                        override fun onResponse(call: Call<Output_Problema>, response: Response<Output_Problema>) {
                                            if (response.isSuccessful) {
                                                val c = response.body()!!

                                                if (c.sucesso) {
                                                    Toast.makeText(this@Problemas, "Sucesso", Toast.LENGTH_SHORT).show()
                                                    val intent = Intent(this@Problemas, AtividadeMapa::class.java)
                                                    startActivity(intent)
                                                } else Toast.makeText(
                                                    this@Problemas, "Falha", Toast.LENGTH_LONG).show()
                                                val intent = Intent(this@Problemas, AtividadeMapa::class.java)
                                                startActivity(intent)
                                            }
                                        }

                                        override fun onFailure(call: Call<Output_Problema>, t: Throwable) {
                                            Toast.makeText(this@Problemas, "Falhou", Toast.LENGTH_SHORT).show()
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
                                                    Toast.makeText(this@Problemas, "Sucesso", Toast.LENGTH_SHORT).show()
                                                    val intent = Intent(this@Problemas, AtividadeMapa::class.java)
                                                    startActivity(intent)
                                                } else Toast.makeText(
                                                    this@Problemas, "Falhou", Toast.LENGTH_LONG
                                                ).show()
                                            }
                                        }

                                        override fun onFailure(
                                            call: Call<Output_Login>,
                                            t: Throwable
                                        ) {
                                            Toast.makeText(this@Problemas, "Falhou", Toast.LENGTH_SHORT).show()

                                        }
                                    })
                                }

                            } else {
                                setContentView(R.layout.atividade_problema_naodono)
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
                    Toast.makeText(this@Problemas, "Falhou", Toast.LENGTH_SHORT).show()
                }
            })
        }
        companion object {
            const val EXTRA_MENSAGEM = "com.example.android.wordlistsql.MSG"
            const val EXTRA_LATITUDE = "com.example.android.wordlistsql.LAT"
            const val EXTRA_LONGITUDE = "com.example.android.wordlistsql.LON"
        }
    }
