package com.example.movieapp.data.model

data class MovieItem(
    val id: Int,
    val title: String,
    val poster_path: String,
    val release_date: String,
    val vote_average: Double,
    val genre_ids: List<Int>
)

data class MovieItemDetail(
    val backdrop_path: String,
    val budget: Long,
    val genres: List<Genres>,
    val id: Int,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val poster_path: String,
    val production_companies: List<ProductionCompany>,
    val release_date: String,
    val revenue: Long,
    val vote_average: Double
)

data class ProductionCompany(
    val id: Int,
    val name: String
)

data class Genres(
    val id: Int,
    val name: String
)



