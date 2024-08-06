package com.example.gp


import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import okhttp3.OkHttpClient
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import okhttp3.Callback
import okhttp3.Headers
import okhttp3.Response
import java.io.IOException


class MainMainActivity : AppCompatActivity() {
    public lateinit var imgview17: ImageView
    public lateinit var imgview13: ImageView
    public lateinit var imgview15: ImageView
    public lateinit var imgview14: ImageView
    public lateinit var txt72: TextView

    private lateinit var webView: WebView
    private lateinit var webView2: WebView
    private lateinit var webView3: WebView
    private lateinit var webView4: WebView
    private lateinit var webView5: WebView


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_main)
        imgview17 = findViewById(R.id.imageView17)
        imgview13 = findViewById(R.id.imageView13)
        imgview15 = findViewById(R.id.imageView15)
        imgview14 = findViewById(R.id.imageView14)

        webView = findViewById(R.id.web1)
        webView2 = findViewById(R.id.web2)
        webView3 = findViewById(R.id.web3)
        webView4 = findViewById(R.id.web4)
        webView5 = findViewById(R.id.web5)

        webView.settings.javaScriptEnabled = true
        webView.webViewClient = MyWebViewClient()
        webView.loadUrl("https://thingspeak.com/channels/2588522/charts/1?bgcolor=%23ffffff&color=%23d62020&dynamic=true&results=60&type=line&update=15")

        webView2.settings.javaScriptEnabled = true
        webView2.webViewClient = MyWebViewClient()
        webView2.loadUrl("https://thingspeak.com/channels/2588522/charts/2?bgcolor=%23ffffff&color=%23d62020&dynamic=true&results=60&type=line&update=15")

        webView3.settings.javaScriptEnabled = true
        webView3.webViewClient = MyWebViewClient()
        webView3.loadUrl("https://thingspeak.com/channels/2588522/charts/3?bgcolor=%23ffffff&color=%23d62020&dynamic=true&results=60&type=line&update=15")

        webView4.settings.javaScriptEnabled = true
        webView4.webViewClient = MyWebViewClient()
        webView4.loadUrl("https://thingspeak.com/channels/2588522/charts/4?bgcolor=%23ffffff&color=%23d62020&dynamic=true&results=60&type=line&update=15")

        webView5.settings.javaScriptEnabled = true
        webView5.webViewClient = MyWebViewClient()
        webView5.loadUrl("https://thingspeak.com/channels/2588522/charts/5?bgcolor=%23ffffff&color=%23d62020&dynamic=true&results=60&type=line&update=15")





        imgview14.setOnClickListener {


            var intent = Intent(this, activity_conversations::class.java)
            startActivity(intent)


        }

        imgview15.setOnClickListener {
            var auth = FirebaseAuth.getInstance()
            val builder = AlertDialog.Builder(this)
            builder.setTitle("تسجيل الخروج")
                .setMessage("هل انت متاكد انك تريد تسجيل الخروج ؟")

            builder.setPositiveButton("نعم") { dialogInterface: DialogInterface, i: Int ->
                var intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                auth.signOut()
                finish()
            }
            builder.setNegativeButton("الغاء") { dialogInterface: DialogInterface, i: Int ->

                dialogInterface.dismiss()
            }
            val dialog = builder.create()
            dialog.show()

        }

        imgview13.setOnClickListener {
            var intent = Intent(this, activity_notifications::class.java)
            startActivity(intent)

        }


        imgview17.setOnClickListener {
            var intent = Intent(this, information_activity::class.java)
            startActivity(intent)

        }


    }

    class MyWebViewClient : WebViewClient() {

        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            view?.loadUrl(request?.url.toString())
            return true
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        var i = Intent(this, MainMainActivity::class.java)
        startActivity(i)
        finish()
    }


}
