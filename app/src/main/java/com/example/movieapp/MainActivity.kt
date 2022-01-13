package com.example.movieapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.recyclerView.RecyclerAdapter
import com.example.movieapp.data.DataRepository
import com.example.movieapp.data.model.MovieItem
import com.example.movieapp.data.model.WrapperResponse
import com.example.movieapp.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.HttpException

class MainActivity : AppCompatActivity(), Callback<WrapperResponse<List<MovieItem>>> {
    private lateinit var binding: ActivityMainBinding
    private lateinit var linearLayoutManager: RecyclerView.LayoutManager
    lateinit var myAdapter: RecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        DataRepository.retrofitClient.getInstance()?.getPopularMovies()?.enqueue(this)

        linearLayoutManager = LinearLayoutManager(this)
        MovieRecyclerView.layoutManager = linearLayoutManager
    }

    override fun onResponse(
        call: Call<WrapperResponse<List<MovieItem>>>,
        response: Response<WrapperResponse<List<MovieItem>>>
    ) {
        if (response.isSuccessful) {
            MovieRecyclerView.apply {
                myAdapter = RecyclerAdapter(response.body()!!.results)
                layoutManager = linearLayoutManager
                adapter = myAdapter
            }
        }
    }

    override fun onFailure(call: Call<WrapperResponse<List<MovieItem>>>, t: Throwable) {
        if (t is HttpException)
            Toast.makeText(this, "Server Error", Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(this, "Network Error", Toast.LENGTH_SHORT).show()
    }
}

