package com.IoT.controlrelay

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private val client = OkHttpClient()
    private val esp32Url = "192.168.120.182" // Ganti dengan alamat IP ESP32 Anda

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonActivate1 = findViewById<Button>(R.id.buttonActivate1)
        val buttonDeactivate1 = findViewById<Button>(R.id.buttonDeactivate1)
        val buttonActivate2 = findViewById<Button>(R.id.buttonActivate2)
        val buttonDeactivate2 = findViewById<Button>(R.id.buttonDeactivate2)
        val buttonActivateAll = findViewById<Button>(R.id.buttonActivatesemua)
        val buttonDeactivateAll = findViewById<Button>(R.id.buttonNonactivatesemua)

        buttonActivate1.setOnClickListener {
            controlRelay("1", "on")
        }

        buttonDeactivate1.setOnClickListener {
            controlRelay("1", "off")
        }

        buttonActivate2.setOnClickListener {
            controlRelay("2", "on")
        }

        buttonDeactivate2.setOnClickListener {
            controlRelay("2", "off")
        }

        buttonActivateAll.setOnClickListener {
            controlAllRelays("on")
        }

        buttonDeactivateAll.setOnClickListener {
            controlAllRelays("off")
        }
    }

    private fun controlRelay(relayNumber: String, action: String) {
        val url = "http://$esp32Url/relay/$relayNumber/$action"
        Log.d("ControlRelay", "Mengirim permintaan ke : $url")
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle kesalahan permintaan HTTP
                Log.e("ControlRelay", "Gagal : ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()
                Log.d("ControlRelay", "Tanggapan : $responseData")
                runOnUiThread {
                    findViewById<TextView>(R.id.textViewStatus).text = responseData
                }
            }
        })
    }

    private fun controlAllRelays(action: String) {
        val url = "http://$esp32Url/relay/all/$action"
        Log.d("ControlRelay", "Mengirim permintaan ke : $url")
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle kesalahan permintaan HTTP
                Log.e("ControlRelay", "Gagal : ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()
                Log.d("ControlRelay", "Tanggapan : $responseData")
                runOnUiThread {
                    findViewById<TextView>(R.id.textViewStatus).text = responseData
                }
            }
        })
    }
}
