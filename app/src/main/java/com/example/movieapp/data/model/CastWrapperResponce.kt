package com.example.movieapp.data.model

data class CastWrapperResponse(
    val id: Int,
    val cast: List<MovieCast>,
    val crew: List<MovieCast>
)


