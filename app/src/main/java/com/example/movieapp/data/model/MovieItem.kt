package com.example.movieapp.data.model

import java.util.*
import kotlin.collections.ArrayList

data class MovieItem(
    val id: Int,
    val title: String,
    val poster_path: String,
    val release_date: String,
    val vote_average: Double,
    val genre_ids: List<Int>
)

data class MovieItemDetail(
    val id: Int,
    val title: String,
    val poster_path: String,
    val backdrop_path: String,
    val release_date: String,
    val vote_average: Double,
    val genres: List<Genres>,
    val overview: String
)

data class Genres(
    val id: Int,
    val name: String
)

