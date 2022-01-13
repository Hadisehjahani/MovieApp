package com.example.movieapp.recyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.R
import com.example.movieapp.data.model.MovieCast
import com.squareup.picasso.Picasso

class CastHorizontal_RecyclerAdapter(private val mList: List<MovieCast>):RecyclerView.Adapter<CastHorizontal_RecyclerAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.castrecyclerview_item_row, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {
        val movieCast = mList[position]
        holder.nameTextView.text = movieCast.name
        val url = "https://image.tmdb.org/t/p/w500" + movieCast.profile_path
        Picasso.get().load(url).into(holder.castProfileImageView)
    }

    override fun getItemCount(): Int {
        return 25
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val nameTextView: TextView = itemView.findViewById(R.id.textCastName)
        val castProfileImageView: ImageView = itemView.findViewById(R.id.castProfilePath)
    }

}
