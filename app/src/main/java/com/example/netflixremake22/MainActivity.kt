package com.example.netflixremake22

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.netflixremake22.model.Category
import com.example.netflixremake22.model.Movie
import com.example.netflixremake22.util.CategoryTask

class MainActivity : AppCompatActivity(), CategoryTask.Callback {
    private lateinit var progress: ProgressBar
    private lateinit var adapter: CategoryAdapter
    private val categories = mutableListOf<Category>()

    //padrão MVC (model - view/controller)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progress = findViewById(R.id.progress_main)

        adapter = CategoryAdapter(categories) { id ->
            val intent = Intent(this@MainActivity, MovieActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
        }
        val rv: RecyclerView = findViewById(R.id.rv_main)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        CategoryTask(this).execulte("https://api.tiagoaguiar.co/netflixapp/home?apiKey=6b73fe1d-4643-4fd9-8d52-00aa7d44cce4")
    }

    override fun onPreExecute() {
        progress.visibility = View.VISIBLE

    }

    //implementando o onResult
    override fun onResult(categories: List<Category>) {
        this.categories.clear()
        this.categories.addAll(categories)
        adapter.notifyDataSetChanged()//esse metodo força o adapter chamar novamente o onBindViewHolder

        progress.visibility = View.GONE
    }

    override fun onFailure(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        progress.visibility = View.GONE
    }
}