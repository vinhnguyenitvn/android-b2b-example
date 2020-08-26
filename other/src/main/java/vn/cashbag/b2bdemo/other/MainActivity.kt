package vn.cashbag.b2bdemo.other

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import vn.cashbag.b2bdemo.WebActivity
import vn.cashbag.b2bdemo.start

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<EditText>(R.id.etUrl).append(WebActivity.DEFAULT_HOST)

        findViewById<Button>(R.id.btAction).setOnClickListener {

            val userId = findViewById<EditText>(R.id.etPartnerId).text.toString().trim()
            val userName = findViewById<EditText>(R.id.etName).text.toString().trim()
            val host = findViewById<EditText>(R.id.etUrl).text.toString().trim()
            val token = findViewById<EditText>(R.id.etToken).text.toString().trim()


            if (host.isNotEmpty() &&
                (token.isNotEmpty() || (userId.isNotEmpty() && userName.isNotEmpty()))
            ) {
                start(userId, userName, host, token)
            }
        }
    }
}