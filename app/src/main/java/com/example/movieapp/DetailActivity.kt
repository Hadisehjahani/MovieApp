package com.example.movieapp

import android.content.ContentValues.TAG
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.movieapp.data.DataRepository
import com.example.movieapp.data.model.*
import com.example.movieapp.databinding.ActivityDetailBinding
import com.example.movieapp.recyclerView.CastHorizontalRecyclerAdapter
import com.example.movieapp.recyclerView.RecommendedRecyclerAdaptor
import com.example.movieapp.recyclerView.ViewPagerDetailAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var castManager: RecyclerView.LayoutManager
    private lateinit var recommendedManager: RecyclerView.LayoutManager
    private lateinit var mMyAdapter: CastHorizontalRecyclerAdapter
    private lateinit var mRecommendedAdaptor: RecommendedRecyclerAdaptor
    private var viewPager: ViewPager? = null
    private var viewPagerAdapter: ViewPagerDetailAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //send movie id by intent to other Activity
        val movieId = intent.extras?.getInt("movie-id")//MOVIE_ID
        val movieTitle = intent.extras?.getString("title")
        supportActionBar?.title = movieTitle
        if (movieId != null) {
            DataRepository.retrofitClient.getInstance()?.getMovieDetails(movieId)
                ?.enqueue(moviesResponseListener)

            DataRepository.retrofitClient.getInstance()?.getMovieCast(movieId)
                ?.enqueue(castResponseListener)

            DataRepository.retrofitClient.getInstance()?.getMovieVideos(movieId)
                ?.enqueue(movieVideosResponseListener)

            DataRepository.retrofitClient.getInstance()?.getMovieRecommendations(movieId)
                ?.enqueue(movieRecommendationsResponseListener)

        }

        // RecyclerView for show cast in detail page
//        linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//        recyclerView_Cast.layoutManager = linearLayoutManager

        castManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView_Cast.layoutManager = castManager

        recommendedManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView_Recommended.layoutManager = recommendedManager
    }

    private val moviesResponseListener: Callback<MovieItemDetail> =
        object : Callback<MovieItemDetail> {

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(
                call: Call<MovieItemDetail>,
                response: Response<MovieItemDetail>
            ) {
                val movieItemDetail: MovieItemDetail? = response.body()
                if (response.isSuccessful) {
                    if (movieItemDetail != null) {
                        Log.v(TAG, "onResponse: isSuccessful")
                        val urlPosterPath =
                            "https://image.tmdb.org/t/p/w500" + movieItemDetail.poster_path
                        Picasso.get().load(urlPosterPath).into(itemDetailImage)

                        val urlBackdropPath =
                            "https://image.tmdb.org/t/p/w500" + movieItemDetail.backdrop_path
                        Picasso.get().load(urlBackdropPath).into(itemDetailBackdropPath)
                        textDetailTitle.text = movieItemDetail.original_title

                        val result: List<Genres> = movieItemDetail.genres
                        val genreTopic: ArrayList<String> = ArrayList()
                        result.forEach {
                            genreTopic.add(it.name)
                        }
                        textDetailGenre.text = genreTopic.joinToString(", ")
                        textDetailReleaseDate.text = movieItemDetail.release_date
                        textDetailVoteAverage.text = movieItemDetail.vote_average.toString()
                        textDetailDescription.text = movieItemDetail.overview

                        // convert string to date "03 November 2021"
                        val releaseDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                        val date = LocalDate.parse(movieItemDetail.release_date, releaseDateFormat)
                        (date.dayOfMonth.toString() + " " + date.month.toString().toLowerCase()
                            .capitalize() + " " + date.year.toString()).also { textInfoReleaseDate.text = it }

                        // show language from Enum
                        val originalLanguage: String = movieItemDetail.original_language
                        enumValues<LanguageEnum>().forEach {
                            if (originalLanguage == it.id)
                                textInfoLanguage.text = it.english_name
                        }

                        // show budget in "$2million"
                        textInfoBudget.text =
                            String.format("$%.0f Million", movieItemDetail.budget / 1000000.0)
                        textInfoRevenue.text =
                            String.format("$%.2f Million", movieItemDetail.revenue / 1000000.0)

                        // show production_companies
                        val productionCompanies: List<ProductionCompany> =
                            movieItemDetail.production_companies
                        val prodCompanies: ArrayList<String> = ArrayList()
                        productionCompanies.forEach {
                            prodCompanies.add(it.name)
                        }
                        textInfoProductionCompanies.text = prodCompanies.joinToString("\n")
                    }
                }
            }

            override fun onFailure(call: Call<MovieItemDetail>, t: Throwable) {
                Log.v(TAG, t.message ?: "MovieItem error")
            }
        }

    private val castResponseListener: Callback<CastWrapperResponse> =
        object : Callback<CastWrapperResponse> {
            override fun onResponse(
                call: Call<CastWrapperResponse>,
                response: Response<CastWrapperResponse>
            ) {
                if (response.isSuccessful) {
                    recyclerView_Cast.apply {
                        mMyAdapter = CastHorizontalRecyclerAdapter(response.body()!!.cast)
                        layoutManager = castManager
                        adapter = mMyAdapter
                    }
                }
            }


            override fun onFailure(call: Call<CastWrapperResponse>, t: Throwable) {
                Log.v(TAG, t.message ?: "MovieCast error")
            }
        }

    private val movieVideosResponseListener: Callback<WrapperResponse<List<MovieVideos>>> =
        object : Callback<WrapperResponse<List<MovieVideos>>> {
            override fun onResponse(
                call: Call<WrapperResponse<List<MovieVideos>>>,
                response: Response<WrapperResponse<List<MovieVideos>>>
            ) {
                if (response.isSuccessful) {
                    viewPager = findViewById(R.id.ViewPagerDetail) as ViewPager
                    viewPagerAdapter =
                        ViewPagerDetailAdapter(this@DetailActivity, response.body()!!.results)
                    viewPager!!.adapter = viewPagerAdapter
                }
            }

            override fun onFailure(call: Call<WrapperResponse<List<MovieVideos>>>, t: Throwable) {
                Log.v(TAG, t.message ?: "MovieVideos error")
            }

        }

    private val movieRecommendationsResponseListener: Callback<WrapperResponse<List<MovieItem>>> =
        object : Callback<WrapperResponse<List<MovieItem>>> {
            override fun onResponse(
                call: Call<WrapperResponse<List<MovieItem>>>,
                response: Response<WrapperResponse<List<MovieItem>>>
            ) {
                if (response.isSuccessful) {
                    recyclerView_Recommended.apply {
                        mRecommendedAdaptor = RecommendedRecyclerAdaptor(response.body()!!.results)
                        layoutManager = recommendedManager
                        adapter = mRecommendedAdaptor
                    }
                }
            }

            override fun onFailure(call: Call<WrapperResponse<List<MovieItem>>>, t: Throwable) {
                Log.v(TAG, t.message ?: "MovieRecommendations error")
            }

        }

}