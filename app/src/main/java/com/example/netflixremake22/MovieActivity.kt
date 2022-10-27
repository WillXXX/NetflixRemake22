package com.example.netflixremake22

import android.graphics.drawable.LayerDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.netflixremake22.model.Movie
import com.example.netflixremake22.model.MovieDetail
import com.example.netflixremake22.util.MovieTask

class MovieActivity : AppCompatActivity(), MovieTask.Callback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        val txtTitle: TextView = findViewById(R.id.movie_txt_title)
        val txtDesc: TextView = findViewById(R.id.movie_txt_desc)
        val txtcast: TextView = findViewById(R.id.movie_txt_cast)
        val rv: RecyclerView = findViewById(R.id.movie_rv_similar)

        val id =intent?.getIntExtra("id", 0) ?: throw IllegalStateException("ID não foi encontrado!")

        val url = "https://api.tiagoaguiar.co/netflixapp/movie/$id?apiKey=6b73fe1d-4643-4fd9-8d52-00aa7d44cce4"
                                                              //$d é o valor deinamico
        MovieTask(this).execulte(url)
        //chamando servidor

        txtTitle.text = "Batman Begins"
        txtDesc.text = "Essa é a descrição do filme"
        txtcast.text = getString(R.string.cast, "ator A, ator B, Atriz A")

        val movies = mutableListOf<Movie>()

        rv.layoutManager = GridLayoutManager(this, 3)
        rv.adapter = MovieAdapter(movies, R.layout.movie_item_similar)

        val toolbar: Toolbar = findViewById(R.id.movie_toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //buscar desenhavel(layerlist)
        val layerDrawable: LayerDrawable =
            ContextCompat.getDrawable(this, R.drawable.shadows) as LayerDrawable
        //buscar filme()
        val movieCover = ContextCompat.getDrawable(this, R.drawable.movie_4)
        //atribui a essse layerList novo filme
        layerDrawable.setDrawableByLayerId(R.id.cover_drawable, movieCover)
        //setar no imageView
        val coverImg: ImageView = findViewById(R.id.movie_img)
        coverImg.setImageDrawable(layerDrawable)
    }

    override fun onPreExecute() {

    }

    override fun onFailure(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onResult(movieDetail: MovieDetail) {
        Log.i("Teste", movieDetail.toString())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

}