package com.example.movieapp

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.movieapp.data.DataRepository
import com.example.movieapp.data.model.Genres
import com.example.movieapp.data.model.MovieItemDetail
import com.example.movieapp.databinding.ActivityDetailBinding
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity(), Callback<MovieItemDetail> {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //send movie id by intent to other Activity
        val value = intent.extras?.getInt("key")
        if (value != null) {
            DataRepository.retrofitClient.getInstance()?.getMovieDetails(value)?.enqueue(this)
        }
    }

    override fun onResponse(
        call: Call<MovieItemDetail>,
        response: Response<MovieItemDetail>
    ) {
        val movieItemDetail: MovieItemDetail? = response.body()
        if (response.isSuccessful) {
            if (movieItemDetail != null) {
                Log.v(TAG, "onResponse: isSuccessful")
                val urlPosterPath = "https://image.tmdb.org/t/p/w500" + movieItemDetail.poster_path
                Picasso.get().load(urlPosterPath).into(itemDetailImage)

                val urlBackdropPath =
                    "https://image.tmdb.org/t/p/w500" + movieItemDetail.backdrop_path
                Picasso.get().load(urlBackdropPath).into(itemDetailBackdropPath)
                textDetailTitle.text = movieItemDetail.title

                val result: List<Genres> = movieItemDetail.genres
                val genreTopic: ArrayList<String> = ArrayList<String>()
                result.forEach {
                    genreTopic.add(it.name)
                }
                textDetailGenre.text = genreTopic.joinToString(", ")

                textDetailReleaseDate.text = movieItemDetail.release_date
                textDetailVoteAverage.text = movieItemDetail.vote_average.toString()
                textDetailDescription.text = movieItemDetail.overview
            }
        }
    }

    override fun onFailure(call: Call<MovieItemDetail>, t: Throwable) {
        Log.v(TAG, t.message ?: "error")
    }
}