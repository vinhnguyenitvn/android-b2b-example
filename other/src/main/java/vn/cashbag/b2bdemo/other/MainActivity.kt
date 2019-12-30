package vn.cashbag.b2bdemo.other

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import vn.cashbag.b2bdemo.start

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btAction).setOnClickListener {

            val partnerId = findViewById<EditText>(R.id.etPartnerId).text.toString().trim()
            val name = findViewById<EditText>(R.id.etName).text.toString().trim()

            if (partnerId.isNotEmpty() && name.isNotEmpty()) {
                start(partnerId, name)
            }
        }
    }
}