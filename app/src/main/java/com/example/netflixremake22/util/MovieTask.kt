package com.example.netflixremake22.util

import android.os.Handler
import android.os.Looper
import android.telecom.Call
import android.util.Log
import com.example.netflixremake22.CategoryAdapter
import com.example.netflixremake22.model.Category
import com.example.netflixremake22.model.Movie
import com.example.netflixremake22.model.MovieDetail
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors

class MovieTask(private val callback: Callback) {
    //(entre os parenteses fica o construtor)

    private val handler = Handler(Looper.getMainLooper())
    private val executor = Executors.newSingleThreadExecutor()


    interface Callback {
        fun onPreExecute()
        fun onResult(movieDetail: MovieDetail)
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

                if (statusCode == 400) {
                    stream = urlConnection.errorStream
                    buffer = BufferedInputStream(stream)
                    val jsonAsString = toString(buffer!!)

                    val json = JSONObject(jsonAsString)
                    val message = json.getString("message")
                    throw IOException(message)

                } else if (statusCode > 400) {
                    throw IOException("Erro na comunicação com o servidor!")
                }

                //Primeira forma de url conection
                stream = urlConnection.inputStream
//                val jsonAsString = stream.bufferedReader().use { it.readText() }
//                Log.i("Teste", jsonAsString)

                //Segunda forma de url conection
                val buffer = BufferedInputStream(stream)
                val jsonAsString = toString(buffer)
                val movieDetail = toMovieDetail(jsonAsString)

                handler.post {
                    //roda dentro da UI_thread
                    callback.onResult(movieDetail)

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

    private fun toMovieDetail(jsonAsString: String): MovieDetail {
        val json = JSONObject(jsonAsString)

        val id = json.getInt("id")
        val title = json.getString("title")
        val desc = json.getString("desc")
        val cast = json.getString("cast")
        val coverUrl = json.getString("cover_url")
        val jsonMovies = json.getJSONArray("movie")

        val similars = mutableListOf<Movie>()
        for (i in 0 until jsonMovies.length()) {
            val jsonMovie = jsonMovies.getJSONObject(i)

            val similarId = jsonMovie.getInt("id")
            val similarCoverUrl = jsonMovie.getString("cover_url")

            val m = Movie(similarId, similarCoverUrl)
            similars.add(m)
        } //loop lista de similares

        val movie = Movie(id, coverUrl, desc, cast)
        return MovieDetail(movie, similars)
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