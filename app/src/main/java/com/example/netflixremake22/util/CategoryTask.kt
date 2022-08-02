package com.example.netflixremake22.util

import android.util.Log
import java.io.IOException
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors

class CategoryTask {

    fun execulte(url: String) {
        try {

            val executor = Executors.newSingleThreadExecutor()

            executor.execute {
                val requestURL = URL(url) //abrir Url
                val urlConnection = requestURL.openConnection() as HttpURLConnection //abrir conexão
                urlConnection.readTimeout =
                    2000 //tempo de leitura dos dados que vem do servidor(2s)
                urlConnection.connectTimeout = 2000 //tempo de conexão do app com  servidor(2s)

                val statusCode: Int = urlConnection.responseCode
                if (statusCode > 400) {
                    throw IOException("Erro na comunicação com o servidor!")
                }
                val stream = urlConnection.inputStream
                val jsonAsString = stream.bufferedReader().use { it.readText() }
                Log.i("Teste", jsonAsString)
            }

        } catch (e: IOException) {
            Log.e("Teste", e.message ?: "Erro desconhecido", e)
        }
    }

}