package ipvc.estg.room

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText

class AddCity : AppCompatActivity() {

    private lateinit var cityText: EditText
    private lateinit var countryText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_city)

        cityText = findViewById(R.id.city)
        countryText = findViewById(R.id.country)

        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(cityText.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                replyIntent.putExtra(EXTRA_REPLY_CITY, cityText.text.toString())
                replyIntent.putExtra(EXTRA_REPLY_COUNTRY, countryText.text.toString())
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }

    companion object {
        const val EXTRA_REPLY_CITY = "com.example.android.city"
        const val EXTRA_REPLY_COUNTRY = "com.example.android.country"
    }
}