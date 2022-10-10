package com.example.netflixremake22.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.netflixremake22.model.Category
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors
import javax.net.ssl.HttpsURLConnection

class DownloadImageTask(private val callback: Callback) {

    private val handler = Handler(Looper.getMainLooper())
    private val executor = Executors.newSingleThreadExecutor()

    interface Callback {
        fun onResult(bitmap: Bitmap)
    }

    fun execulte(url: String) {
        executor.execute {
            var urlConnection: HttpsURLConnection? = null
            var stream: InputStream? = null
            try {
                val requestURL = URL(url) //abrir Url
                val urlConnection = requestURL.openConnection() as HttpURLConnection //abrir conexão
                urlConnection.readTimeout = 2000 //tempo de leitura dos dados que vem do servidor(2s)
                urlConnection.connectTimeout = 2000 //tempo de conexão do app com  servidor(2s)

                val statusCode: Int = urlConnection.responseCode
                if (statusCode > 400) {
                    throw IOException("Erro na comunicação com o servidor!")
                }

                stream = urlConnection.inputStream
                val bitmap = BitmapFactory.decodeStream(stream)

                handler.post {
                    //roda dentro da UI_thread
                    callback.onResult(bitmap)

                }

            } catch (e: IOException) {
                val message = e.message ?: "Erro desconhecido"
                Log.e("Teste", message, e)
            }finally {
                urlConnection?.disconnect()
                stream?.close()
            }
        }
    }
}