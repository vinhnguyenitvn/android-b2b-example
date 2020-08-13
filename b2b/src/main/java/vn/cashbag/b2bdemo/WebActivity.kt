package vn.cashbag.b2bdemo

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import java.math.BigInteger
import java.security.MessageDigest
import java.util.*

class WebActivity : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_web)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val userId = intent.getStringExtra(EXTRA_USER_ID) ?: return
        val userName = intent.getStringExtra(EXTRA_USER_NAME) ?: return

        val url = generateURL(userId, userName).toString()

        findViewById<WebView>(R.id.webView).run {
            settings.javaScriptEnabled = true
            settings.javaScriptCanOpenWindowsAutomatically = true
            settings.domStorageEnabled = true
            webViewClient = object : WebViewClient() {

                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    return Uri.parse(url).getQueryParameter("cashback_aff_url")?.let { s ->
                        try {
                            startActivity(Intent(Intent.ACTION_VIEW).apply {
                                data = Uri.parse(s)
                            })
                            finish() //close current activity if needed
                        } catch (e: Exception) {
                            //Cannot find any app handle the cashback_aff_url
                        }
                        true
                    } ?: false
                }
            }
            loadUrl(url)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun generateURL(userId: String, userName: String): Uri {

        fun String.sha256(): String {
            val md = MessageDigest.getInstance("SHA-256")
            return BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
        }

        val timestamp = Date().time / 1000 //time in seconds
        val privateKey = "AG9JKIZsxE0BCMkhgXwrdNuK"
        val tokenString = "$userId$userName$privateKey$timestamp"
        val token = tokenString.sha256()
        return Uri.Builder()
            .scheme("https")
            .authority("dev-webview.cashbagb2b.com")
            .appendQueryParameter("userId", userId)
            .appendQueryParameter("userName", userName)
            .appendQueryParameter("timestamp", timestamp.toString())
            .appendQueryParameter("token", token)
            .build()
    }

    companion object {
        const val EXTRA_USER_ID = "UserId"
        const val EXTRA_USER_NAME = "UserName"
    }
}

fun Context.start(userId: String, userName: String) {
    startActivity(Intent(this, WebActivity::class.java).apply {
        putExtra(WebActivity.EXTRA_USER_ID, userId)
        putExtra(WebActivity.EXTRA_USER_NAME, userName)
    })
}
