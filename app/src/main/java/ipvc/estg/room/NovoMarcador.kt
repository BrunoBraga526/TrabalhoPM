package ipvc.estg.room

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import ipvc.estg.room.*
import ipvc.estg.room.API_Login.PostLogin
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response




class NovoMarcador : AppCompatActivity() {

    private lateinit var createmarkerView: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.atividade_novomarcador)

        val sharedPref: SharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        var utilizador:String? = sharedPref.getString("automatic_login_username", null)
        var tipo = String();
        val latitude= intent.getStringExtra(EXTRA_LAT).toString()
        val longitude= intent.getStringExtra(EXTRA_LON).toString()
        createmarkerView = findViewById(R.id.marker_texto)
        val texto = createmarkerView.text
        val tipos = resources.getStringArray(R.array.TiposProblema)

        val spinner = findViewById<Spinner>(R.id.dropdown_novo_marcador)
        if (spinner != null) {
            val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, tipos)
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                    tipo=tipos[position]
                }
                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }
        }
        val button = findViewById<Button>(R.id.botao_guardar)
        button.setOnClickListener {
            val request = Servicos.buildServico(PostLogin::class.java)
            val call = request.criarProblema(
                utilizador.toString(),
                tipo,
                texto,
                latitude,
                longitude

            )
            call.enqueue(object : Callback<Output_Problema> {
                override fun onResponse(call: Call<Output_Problema>, response: Response<Output_Problema>)
                {
                    if (response.isSuccessful) {
                        val c: Output_Problema = response.body()!!
                        if (c.sucesso) {
                            Toast.makeText(this@NovoMarcador,R.string.marcador_inserido,Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@NovoMarcador, AtividadeMapa::class.java)
                            startActivity(intent)
                            finish()
                        } else Toast.makeText(this@NovoMarcador,R.string.marcador_nao_inserido,Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<Output_Problema>, t: Throwable) {
                    Toast.makeText(this@NovoMarcador, "${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
            finish()

        }

    }
    companion object {
        const val EXTRA_LAT = "com.example.android.wordlistsql.LAT"
        const val EXTRA_LON = "com.example.android.wordlistsql.LON"
    }
}