package com.example.netflixremake22.model

import androidx.annotation.DrawableRes
import java.net.URL

data class Movie(
    val id: Int,
    val coverURL: String,
    val title: String = "",
    val desc: String = "",
    val cast: String = "",


    )
