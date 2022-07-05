package com.example.netflixremake22

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class MainAdapter : RecyclerView.Adapter<MainAdapter.MovieViewHolder>() {

    //implementado metdos do Adapter
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
    }

    override fun getItemCount(): Int {
        return 60
    }

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }


}