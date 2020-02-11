package vn.cashbag.b2bdemo.ocb

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import vn.cashbag.b2bdemo.start

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.image).setOnClickListener {
            start("minhtruong", "Minh Truong")
        }
    }
}
