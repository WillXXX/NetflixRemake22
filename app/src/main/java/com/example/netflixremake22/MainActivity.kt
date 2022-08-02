package com.example.netflixremake22

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.netflixremake22.model.Category
import com.example.netflixremake22.model.Movie
import com.example.netflixremake22.util.CategoryTask

class MainActivity : AppCompatActivity() {

    //padrão MVC (model - view/controller)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val categories = mutableListOf<Category>()

        val adapter = CategoryAdapter(categories)
        val rv: RecyclerView = findViewById(R.id.rv_main)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        CategoryTask().execulte("https://api.tiagoaguiar.co/netflixapp/home?apiKey=6b73fe1d-4643-4fd9-8d52-00aa7d44cce4")
    }


}