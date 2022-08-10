package com.example.netflixremake22.util

import android.os.Handler
import android.os.Looper
import android.telecom.Call
import android.util.Log
import com.example.netflixremake22.CategoryAdapter
import com.example.netflixremake22.model.Category
import com.example.netflixremake22.model.Movie
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors

class CategoryTask(private val callback: Callback) {
    //(entre os parenteses fica o construtor)

    private val handler = Handler(Looper.getMainLooper())
    private val executor = Executors.newSingleThreadExecutor()


    interface Callback {
        fun onPreExecute()
        fun onResult(categories: List<Category>)
        fun onFailure(message: String)
    }

    fun execulte(url: String) {
        callback.onPreExecute()

        var urlConnection: HttpURLConnection? = null
        var buffer: BufferedInputStream? = null
        var stream: InputStream? = null

        try {


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

                //Primeira forma de url conection
                stream = urlConnection.inputStream
//                val jsonAsString = stream.bufferedReader().use { it.readText() }
//                Log.i("Teste", jsonAsString)

                //Segunda forma de url conection
                val buffer = BufferedInputStream(stream)
                val jsonAsString = toString(buffer)
                val categories = toCategories(jsonAsString)

                handler.post {
                    //roda dentro da UI_thread
                    callback.onResult(categories)

                }
            }

        } catch (e: IOException) {
            val message = e.message ?: "Erro desconhecido"
            Log.e("Teste", message, e)
            handler.post {
                callback.onFailure(message)
            }
        } finally {
            urlConnection?.disconnect()
            stream?.close()
            buffer?.close()
            //checagem dupla de fechamento
        }
    }

    private fun toCategories(jsonAsString: String): MutableList<Category> {
        val categories = mutableListOf<Category>()

        //trasendo da internet o resultado
        val jsonRoot = JSONObject(jsonAsString)
        val jsonCategories = jsonRoot.getJSONArray("category")
        for (i in 0 until jsonCategories.length()) { //quantidade de elementos da lista
            val jsonCategory = jsonCategories.getJSONObject(i)

            val title = jsonCategory.getString("title")
            val jsonMovies = jsonCategory.getJSONArray("movie")

            //loop dos filmes
            val movies = mutableListOf<Movie>()
            for (j in 0 until jsonMovies.length()) {
                val jsonMovie = jsonMovies.getJSONObject(j)
                val id = jsonMovie.getInt("id")
                val coverUrl = jsonMovie.getString("cover_url")

                movies.add(Movie(id, coverUrl))
            }
            categories.add(Category(title, movies))
        }
        return categories
    }

    private fun toString(stream: InputStream): String {
        val bytes = ByteArray(1024)
        val baos = ByteArrayOutputStream()
        var read: Int
        while (true) {
            read = stream.read(bytes)
            if (read <= 0) {
                break
            }
            baos.write(bytes, 0, read)
        }
        return String(baos.toByteArray())
    }
}