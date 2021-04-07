package com.headmostlab.testservice

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    private lateinit var urlEditText: EditText

    private val receiver: BroadcastReceiver by lazy {
        object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val content = intent?.getStringExtra(BROADCAST_EXTRA_CONTENT) ?: return
                webView.loadData(content, "text/html", "UTF-8")
                Toast.makeText(context, "ON RECEIVED", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webView)

        urlEditText = findViewById(R.id.urlEditText)

        findViewById<Button>(R.id.goButton).setOnClickListener {
            val intent = Intent(this, MyService::class.java).apply {
                putExtra(MY_SERVICE_EXTRA_URL, urlEditText.text.toString())
            }
            startService(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(receiver, IntentFilter(BROADCAST_FILTER_CONTENT_RECEIVED))
    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)
    }
}