package com.example.movieapp.recyclerView

import android.content.ContentValues
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.DetailActivity
import com.example.movieapp.R
import com.example.movieapp.data.model.GenresEnum
import com.example.movieapp.data.model.MovieItem
import com.squareup.picasso.Picasso

class RecyclerAdapter(private val mList: List<MovieItem>) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_item_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movieItem = mList[position]
        val url = "https://image.tmdb.org/t/p/w500" + movieItem.poster_path
        Picasso.get().load(url).into(holder.imageView)

        holder.titleTextView.text = movieItem.title
        holder.releaseDateTextView.text = movieItem.release_date
        holder.voteAverageTextView.text = movieItem.vote_average.toString()

        var genres: List<Int> = movieItem.genre_ids
        var genreTopic: ArrayList<String> = ArrayList<String>()

        genres.forEach { g ->
            GenresEnum.values().find { it.id == g }?.let {
                genreTopic.add(it.name)
            }
        }
        holder.genreTextView.text = genreTopic.joinToString(", ")
        holder.itemView.setOnClickListener {
            val intent: Intent = Intent(it.context, DetailActivity::class.java)
            intent.putExtra("key", movieItem.id)
            it.context.startActivity(intent)
            Log.d(ContentValues.TAG, "onclick: $movieItem")
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.itemImage)
        val titleTextView: TextView = itemView.findViewById(R.id.textTitle)
        val releaseDateTextView: TextView = itemView.findViewById(R.id.textReleaseDate)
        val voteAverageTextView: TextView = itemView.findViewById(R.id.textVoteAverage)
        val genreTextView: TextView = itemView.findViewById(R.id.textGenre)
    }
}