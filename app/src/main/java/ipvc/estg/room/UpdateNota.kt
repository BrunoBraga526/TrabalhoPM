package ipvc.estg.room

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class UpdateNota : AppCompatActivity(){
    private lateinit var notaText: EditText
    private lateinit var textoText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_nota)
        notaText = findViewById(R.id.editar_nota)
        textoText = findViewById(R.id.editar_texto)

        //recolhe os valores associados ao
        val intent = intent
        notaText.setText( intent.getStringExtra(EXTRA_REPLY_NOTA) )
        textoText.setText( intent.getStringExtra(EXTRA_REPLY_TEXTO) )
        val id = intent.getIntExtra( EXTRA_ID , -1)
        val button = findViewById<Button>(R.id.botao_guardar)
        button.setOnClickListener {
            val replyIntent = Intent()

            val nota = notaText.text.toString()
            val texto = textoText.text.toString()
            if( id != -1 ) {
                replyIntent.putExtra(EXTRA_ID, id)
            }
            replyIntent.putExtra(EXTRA_REPLY_NOTA, nota)
            replyIntent.putExtra(EXTRA_REPLY_TEXTO, texto)
            setResult(Activity.RESULT_OK, replyIntent)

            finish()
        }
    }

    companion object {
        const val EXTRA_REPLY_NOTA = "com.example.android.nota"
        const val EXTRA_REPLY_TEXTO = "com.example.android.texto"
        const val EXTRA_ID = "com.example.android.wordlistsql.ID"
    }
}
