package com.example.movieapp.data.model

data class WrapperResponse<T>(
    val page: Int,
    val results: T,
    val total_pages: Int,
    val total_results: Int
)


