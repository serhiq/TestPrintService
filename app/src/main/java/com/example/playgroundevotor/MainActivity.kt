package com.example.playgroundevotor

import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.evotor.devices.commons.Constants
import ru.evotor.devices.commons.DeviceServiceConnector
import ru.evotor.devices.commons.printer.PrinterDocument
import ru.evotor.devices.commons.printer.printable.PrintableImage
import java.io.IOException
import java.io.InputStream


class MainActivity : AppCompatActivity() {

    private val scope = CoroutineScope(Dispatchers.IO)
    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        DeviceServiceConnector.startInitConnections(getApplicationContext());

        textView = findViewById<TextView>(R.id.textView)
        val btnPrint = findViewById<Button>(R.id.btnPrint)
        btnPrint.setOnClickListener { startPrint() }
    }


    private fun startPrint() {
        scope.launch {
            try {
                val printerService = DeviceServiceConnector.getPrinterService()
                printerService.printDocument(
                    Constants.DEFAULT_DEVICE_INDEX, PrinterDocument(
                        PrintableImage(
                            getBitmapFromAsset("ic_launcher.png")
                        ),
                    )
                )
            } catch (e: Exception) {
                withContext(Dispatchers.Main) { textView.text = e.message }
            } finally {

            }
        }
    }

    private fun getBitmapFromAsset(fileName: String): Bitmap? {
        val assetManager: AssetManager = getAssets()
        var stream: InputStream? = null
        try {
            stream = assetManager.open(fileName)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return BitmapFactory.decodeStream(stream)
    }
}

