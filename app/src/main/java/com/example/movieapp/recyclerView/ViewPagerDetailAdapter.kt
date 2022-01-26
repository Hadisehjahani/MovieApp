package com.example.movieapp.recyclerView

import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.viewpager.widget.PagerAdapter
import com.example.movieapp.R
import com.example.movieapp.data.model.MovieItem
import com.example.movieapp.data.model.MovieVideos
import com.example.movieapp.data.model.WrapperResponse
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import retrofit2.Callback

class ViewPagerDetailAdapter(var context: Context, private val mList: List<MovieVideos>) :
    PagerAdapter() {
//    private lateinit var layoutInflater: LayoutInflater

    override fun getCount(): Int {
        return mList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as RelativeLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val movieVideos = mList[position]
        val view = LayoutInflater.from(container.context)
            .inflate(R.layout.viewpager_layout, container, false)
        val videosImageView = view.findViewById<View>(R.id.ViewPagerDetailImage) as ImageView

        val urlThumbnailPath = "https://img.youtube.com/vi/" + movieVideos.key + "/mqdefault.jpg"
        Picasso.get().load(urlThumbnailPath).into(videosImageView)

        view.setOnClickListener {
            Log.v(ContentValues.TAG, "yes done")
            this.openYoutubeLink(movieVideos.key)
        }
        container.addView(view)
        return view
    }


//    init {
//        view = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//
//    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as RelativeLayout)
    }

    fun openYoutubeLink(youtubeID: String) {
        val intentApp = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$youtubeID"))
        val intentBrowser =
            Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=$youtubeID"))
        try {
            context.startActivity(intentApp)
        } catch (ex: ActivityNotFoundException) {
            context.startActivity(intentBrowser)
        }
    }
}