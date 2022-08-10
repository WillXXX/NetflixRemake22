package com.example.netflixremake22.util

import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import com.example.netflixremake22.model.Category
import java.io.IOException
import java.io.InputStream
import java.util.concurrent.Executors
import javax.net.ssl.HttpsURLConnection

class DownloadImageTask {

    private val handler = Handler(Looper.getMainLooper())
    private val executor = Executors.newSingleThreadExecutor()

    interface Callback {
        fun onResult(bitmap: Bitmap)
    }

    fun execulte(url: String) {
        executor.execute {
            var urlConnection:HttpsURLConnection? = null
            var stream:InputStream? = null
            try {

            }catch (e:IOException){

            }
        }
    }
}