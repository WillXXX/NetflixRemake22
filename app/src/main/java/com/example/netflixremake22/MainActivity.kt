package com.example.netflixremake22

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.netflixremake22.model.Movie

class MainActivity : AppCompatActivity() {

    //padrão MVC (model - view/controller)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val movies = mutableListOf<Movie>()
        for(i in 0 until 15){
            val movie = Movie(R.drawable.movie)
            movies.add(movie)
        }

        val adapter = MainAdapter(movies)
        val rv: RecyclerView = findViewById(R.id.rv_main)
        rv.layoutManager = LinearLayoutManager(this,RecyclerView.HORIZONTAL,false)
        rv.adapter = adapter
    }


}