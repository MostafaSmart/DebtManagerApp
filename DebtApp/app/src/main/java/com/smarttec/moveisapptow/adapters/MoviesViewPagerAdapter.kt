package com.smarttec.moveisapptow.adapters

import android.content.ContentValues
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smarttec.moveisapptow.R
import com.smarttec.moveisapptow.data.models.MovesModel
import com.squareup.picasso.Picasso


class MoviesViewPagerAdapter() :




    RecyclerView.Adapter<MoviesViewPagerAdapter.ImageViewHolder>() {

    class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val movie_name = view.findViewById<TextView>(R.id.movie_name);
        val movie_image = view.findViewById<ImageView>(R.id.movie_image);
        val movie_rating = view.findViewById<RatingBar>(R.id.movie_rating);
        val item_move_viewpigr2 = view.findViewById<FrameLayout>(R.id.item_move_viewpigr2);
    }


    var movesList: List<MovesModel> = ArrayList()
    var count = 0

    fun setMovesList(categoryList: List<MovesModel>, count2:Int){
        this.movesList = categoryList
        this.count = count2
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie_viewpiger, parent, false)

        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        var data = movesList[position]
        var url = "https://image.tmdb.org/t/p/original"+data.backdrop_path
        Log.d(ContentValues.TAG, "resposn  : ${url}")
        Picasso.get()
            .load(url).into(holder.movie_image)

        holder.movie_name.text = data.title

        var rite = data.vote_average /2

        holder.movie_rating.rating = rite.toFloat()
    }




    override fun getItemCount(): Int = count
}
