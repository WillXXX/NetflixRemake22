package com.example.netflixremake22

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.netflixremake22.model.Category
import com.example.netflixremake22.model.Movie
import com.example.netflixremake22.util.CategoryTask

class MainActivity : AppCompatActivity(), CategoryTask.Callback {
    //implementado o callback para trazer ele (CategoryTask.Callback)

    //padrão MVC (model - view/controller)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val categories = mutableListOf<Category>()

        val adapter = CategoryAdapter(categories)
        val rv: RecyclerView = findViewById(R.id.rv_main)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        CategoryTask(this).execulte("https://api.tiagoaguiar.co/netflixapp/homeim?apiKey=6b73fe1d-4643-4fd9-8d52-00aa7d44cce4")
    }

    //implementando o onResult
    override fun onResult(categories: List<Category>) {

        Log.i("Teste", categories.toString())
    }

    override fun onFailure(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}