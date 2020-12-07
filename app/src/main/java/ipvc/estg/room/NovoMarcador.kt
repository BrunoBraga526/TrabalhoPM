package ipvc.estg.room

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import ipvc.estg.room.API_Login.PostLogin
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class NovoMarcador : AppCompatActivity() {
    private lateinit var createmarkerView: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.atividade_novomarcador)
        var tipo = String();
        var posicao_corrente: String;
        val sharedPref: SharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE)
        var utilizador:String? = sharedPref.getString("automatic_login_username", null)
        var foto="/URL/HELLO"
        val date= intent.getStringExtra(EXTRA_LOCAL)
        posicao_corrente= date.toString();
        createmarkerView = findViewById(R.id.marker_texto)
        val texto = createmarkerView.text
        val tipos = resources.getStringArray(R.array.TiposProblema)
        val spinner = findViewById<Spinner>(R.id.dropdown1)
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
        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener {
            val request = Servicos.buildServico(PostLogin::class.java)
            val call = request.create(
                utilizador.toString(),
                tipo,
                texto,
                posicao_corrente,
                foto

            )
            call.enqueue(object : Callback<Output_Problema> {
                override fun onResponse(call: Call<Output_Problema>, response: Response<Output_Problema>)
                {
                    if (response.isSuccessful) {
                        val c: Output_Problema = response.body()!!
                        if (c.sucesso) {
                            Toast.makeText(this@NovoMarcador,R.string.markercorrectlabel,Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@NovoMarcador, AtividadeMapa::class.java)
                            startActivity(intent)
                            finish()
                        } else Toast.makeText(this@NovoMarcador,R.string.markerincorrectlabel,Toast.LENGTH_SHORT).show()
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
        const val EXTRA_LOCAL = "com.example.android.wordlistsql.LOCAL"
    }
}
