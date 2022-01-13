package com.example.movieapp

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.data.DataRepository
import com.example.movieapp.data.model.CastWrapperResponse
import com.example.movieapp.data.model.Genres
import com.example.movieapp.data.model.MovieCast
import com.example.movieapp.data.model.MovieItemDetail
import com.example.movieapp.databinding.ActivityDetailBinding
import com.example.movieapp.recyclerView.CastHorizontal_RecyclerAdapter
import com.example.movieapp.recyclerView.RecyclerAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_main.*

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var linearLayoutManager: RecyclerView.LayoutManager
    private lateinit var myAdapter: CastHorizontal_RecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //send movie id by intent to other Activity
        val movieId = intent.extras?.getInt("key")//MOVIE_ID
        if (movieId != null) {
            DataRepository.retrofitClient.getInstance()?.getMovieDetails(movieId)?.enqueue(moviesResponseListener)

            DataRepository.retrofitClient.getInstance()?.getMovieCast(movieId)?.enqueue(castResponseListener)
        }

        // RecyclerView for show cast in detail page

        linearLayoutManager = LinearLayoutManager(this , LinearLayoutManager.HORIZONTAL , false)
        recyclerView_Cast.layoutManager = linearLayoutManager
//        recyclerView.layoutManager = LinearLayoutManager(this , LinearLayoutManager.HORIZONTAL , false)
    }

    private val moviesResponseListener:Callback<MovieItemDetail> = object :Callback<MovieItemDetail>{

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
            Log.v(TAG, t.message ?: "MovieItem error")
        }
    }

    private val castResponseListener:Callback<CastWrapperResponse> = object :Callback<CastWrapperResponse>{
        override fun onResponse(call: Call<CastWrapperResponse>, response: Response<CastWrapperResponse>) {
            if (response.isSuccessful) {
                recyclerView_Cast.apply {
                    myAdapter = CastHorizontal_RecyclerAdapter(response.body()!!.cast)
                    layoutManager = linearLayoutManager
                    adapter = myAdapter
                }
            }
        }


        override fun onFailure(call: Call<CastWrapperResponse>, t: Throwable) {
            Log.v(TAG, t.message ?: "MovieCast error")
        }
    }

}