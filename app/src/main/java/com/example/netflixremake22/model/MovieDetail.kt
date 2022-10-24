package com.example.netflixremake22.model

data class MovieDetail(
    val movie: Movie,
    val similars: List<Movie>
)