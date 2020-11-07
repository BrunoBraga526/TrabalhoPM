package ipvc.estg.room

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText

class AddNota : AppCompatActivity() {
    //declaração de varáveis
    private lateinit var notaText: EditText
    private lateinit var textoText: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_nota)
        //associa a nota e o texto ao ID
        notaText = findViewById(R.id.nota)
        textoText = findViewById(R.id.texto)

        //Botao de edição, deteção de alterações e caso existam empurra os valores para a intent
        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(notaText.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val nota  = notaText.text.toString()
                val texto = textoText.text.toString()
                replyIntent.putExtra(EXTRA_REPLY_NOTA, notaText.text.toString())
                replyIntent.putExtra(EXTRA_REPLY_TEXTO, textoText.text.toString())
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }

    companion object {
        const val EXTRA_REPLY_NOTA = "com.example.android.nota"
        const val EXTRA_REPLY_TEXTO = "com.example.android.texto"
    }
}