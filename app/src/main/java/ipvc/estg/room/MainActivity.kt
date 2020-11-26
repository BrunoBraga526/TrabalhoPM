package ipvc.estg.room

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import ipvc.estg.room.API_Login.PostLogin
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var utilizadorEditTextView: EditText
    private lateinit var palavrapasseEditTextView: EditText
    private lateinit var botao_login: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.atividade_login)

        utilizadorEditTextView = findViewById(R.id.login_utilizador)
        palavrapasseEditTextView = findViewById(R.id.login_palavrapasse)
        botao_login = findViewById(R.id.botao_login)

        val sharedPref: SharedPreferences = getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE )


        val loginauto_valida = sharedPref.getBoolean(getString(R.string.keeper_loginauto_valida), false)
        Log.d("SP_AutoLoginCheck", "$loginauto_valida")

        if( loginauto_valida ) {
            val intent = Intent(this@MainActivity, AtividadeMapa::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun login( view: View ) {

        val utilizador = utilizadorEditTextView.text.toString()
        val palavrapasse = palavrapasseEditTextView.text.toString()//.sha256()


        //validação dos campos de login (isempty???)
        if ( TextUtils.isEmpty(utilizador) ) {
            Toast.makeText(this, R.string.fieldusernameemptylabel, Toast.LENGTH_LONG).show()
            return
        }
        else if ( TextUtils.isEmpty(palavrapasse) ) {
            Toast.makeText(this, R.string.fieldpasswordemptylabel, Toast.LENGTH_LONG).show()
            return
        }
        else {

            val request = Servicos.buildServico(PostLogin::class.java) //pedido POST à BD
            val call = request.postTest(  //valida atraves do Output_login (sucesso, user e resposta da API)
                utilizador,
                palavrapasse
            )

            call.enqueue(object : Callback<Output_Login> {
                override fun onResponse(
                    call: Call<Output_Login>,
                    response: Response<Output_Login> //se a resposta é sucesso (utilizador = utilizadorBD) devolve true
                ) {
                    if (response.isSuccessful) {
                        val c: Output_Login = response.body()!!

                        if (c.sucesso) {
                            Toast.makeText(
                                this@MainActivity,
                                R.string.logincorrectlabel,
                                Toast.LENGTH_SHORT
                            ).show()

                            //keeper dos dados do utilizador caso este esteja logado para login automatico emshared preferences
                            val sharedPref: SharedPreferences = getSharedPreferences(
                                getString(R.string.preference_file_key), Context.MODE_PRIVATE )
                            with ( sharedPref.edit() ) {
                                putBoolean(getString(R.string.keeper_loginauto_valida), true)
                                putString(getString(R.string.keeper_loginauto_utilizador), utilizador )
                                putString(getString(R.string.keeper_loginauto_palavrapasse), palavrapasse )
                                commit()
                            }

                            //segue para a atividade mapa
                            val intent = Intent(this@MainActivity, AtividadeMapa::class.java)
                            startActivity(intent)
                            finish()
                        } else Toast.makeText( //resposta em caso de falha
                            this@MainActivity,
                            R.string.loginincorrectlabel,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                //em caso de falha na api recebe resposta a identificar  que correu mal e recebe resposta
                override fun onFailure(call: Call<Output_Login>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }


    fun mostraratividadenotas(view: View ) {
        val intent = Intent( this, Nota_MainActivity::class.java )
        startActivity(intent)
    }
}

//encriptação da palavra passe para decoding

/*fun String.sha256(): String {
    return hashString(this, "SHA-256")
}
private fun hashString(input:String, algorithm:String): String{
    return MessageDigest
        .getInstance(algorithm)
        .digest(input.toByteArray())
        .fold("",{str,it->str + "%02x".format(it)})
}
*/