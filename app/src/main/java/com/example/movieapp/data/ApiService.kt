package com.example.movieapp.data

import com.example.movieapp.data.model.MovieItem
import com.example.movieapp.data.model.MovieItemDetail
import com.example.movieapp.data.model.WrapperResponse
import com.example.movieapp.utils.apikey
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("movie/popular?api_key=$apikey")
    fun getPopularMovies(): Call<WrapperResponse<List<MovieItem>>>

    @GET("movie/{movie_id}?api_key=$apikey&language=en-US")
    fun getMovieDetails(@Path("movie_id") movieId: Int): Call<MovieItemDetail>

}