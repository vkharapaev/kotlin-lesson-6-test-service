package com.headmostlab.testservice

import android.app.IntentService
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

const val MY_SERVICE_EXTRA_URL = "URL"
const val BROADCAST_FILTER_CONTENT_RECEIVED = "CONTENT_RECEIVED"
const val BROADCAST_EXTRA_CONTENT = "CONTENT"

class MyService(name: String = "TestService") : IntentService(name) {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onHandleIntent(intent: Intent?) {
        val spec = intent?.getStringExtra(MY_SERVICE_EXTRA_URL) ?: return
        val content = try {
            val url = URL(spec)
            val connection = url.openConnection() as HttpsURLConnection
            connection.apply {
                requestMethod = "GET"
                readTimeout = 10000
            }
            val reader = BufferedReader(InputStreamReader(connection.inputStream))
            reader.lines().collect(Collectors.joining("\n"))
        } catch (e: Throwable) {
            "Error: " + e.message
        }
        send(content)
    }

    private fun send(content: String) {
        val intent = Intent(BROADCAST_FILTER_CONTENT_RECEIVED)
        intent.putExtra(BROADCAST_EXTRA_CONTENT, content)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

}