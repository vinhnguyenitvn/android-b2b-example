package vn.cashbag.b2bdemo

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.math.BigInteger
import java.security.MessageDigest
import java.util.*

class WebActivity : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled", "AddJavascriptInterface")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_web)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val userId = intent.getStringExtra(EXTRA_USER_ID) ?: return
        val userName = intent.getStringExtra(EXTRA_USER_NAME) ?: return
        val host = intent.getStringExtra(EXTRA_HOST) ?: return
        val token = intent.getStringExtra(EXTRA_TOKEN)

        val url = if (token?.isNotEmpty() == true)
            generateURL(host, token).toString()
        else
            generateURL(userId, userName, host).toString()

        findViewById<WebView>(R.id.webView).run {
            settings.javaScriptEnabled = true
            settings.javaScriptCanOpenWindowsAutomatically = true
            settings.domStorageEnabled = true

            addJavascriptInterface(JSBridge(this@WebActivity), "JSBridge")
            loadUrl(url)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    class JSBridge(val context: Context) {

        @JavascriptInterface
        fun sendMessage(message: String) {
            try {
                JSONObject(message).apply {
                    when (opt("type")) {
                        "open_tel_link" -> {
                            context.startActivity(Intent(Intent.ACTION_VIEW).apply {
                                data = Uri.parse("tel:${opt("value")}")
                            })
                        }
                        "open_affiliate_link" -> {
                            context.startActivity(Intent(Intent.ACTION_VIEW).apply {
                                data = Uri.parse("${opt("value")}")
                            })
                        }
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun generateURL(userId: String, userName: String, host: String): Uri {

        fun String.sha256(): String {
            val md = MessageDigest.getInstance("SHA-256")
            return BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
        }

        val timestamp = Date().time / 1000 //time in seconds
        val privateKey = "AG9JKIZsxE0BCMkhgXwrdNuK"
        val tokenString = "$userId$userName$privateKey$timestamp"
        val token = tokenString.sha256()
        return Uri.parse(host).buildUpon()
            .appendQueryParameter("userId", userId)
            .appendQueryParameter("userName", userName)
            .appendQueryParameter("timestamp", timestamp.toString())
            .appendQueryParameter("token", token)
            .build()
    }

    private fun generateURL(host: String, token: String): Uri {
        return Uri.parse(host).buildUpon()
            .appendQueryParameter("token", token)
            .build()
    }

    companion object {
        const val EXTRA_USER_ID = "UserId"
        const val EXTRA_USER_NAME = "UserName"
        const val EXTRA_HOST = "Host"
        const val EXTRA_TOKEN = "Token"

        const val DEFAULT_HOST = "https://dev-webview.devatcashback.com"
    }
}

fun Context.start(userId: String, userName: String, host: String, token: String) {
    startActivity(Intent(this, WebActivity::class.java).apply {
        putExtra(WebActivity.EXTRA_USER_ID, userId)
        putExtra(WebActivity.EXTRA_USER_NAME, userName)
        putExtra(WebActivity.EXTRA_HOST, host)
        putExtra(WebActivity.EXTRA_TOKEN, token)
    })
}
