package com.example.moviesapp.viewholders

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviesapp.R
import com.example.moviesapp.adapters.MoviesAdapter
import com.example.moviesapp.fragments.list.MoviesListViewModel
import com.example.moviesapp.pojo.movies.popular.Result
import com.example.moviesapp.utils.imageOption

class MovieViewHolder(
    itemView: View,
    listener: MoviesAdapter.OnClickPoster?,
    movies: List<Result>,
    val viewModel: MoviesListViewModel
) :
    RecyclerView.ViewHolder(itemView) {

    private val configuration = viewModel.configuration
    private val genres = viewModel.genreList

    private val pgMovie = itemView.findViewById<AppCompatTextView>(R.id.pg_movie)
    private val favouriteImage = itemView.findViewById<AppCompatImageView>(R.id.favourite_image)
    private val imagePoster = itemView.findViewById<AppCompatImageView>(R.id.movie_poster)
    private val tagLine = itemView.findViewById<AppCompatTextView>(R.id.tag_line_movie)
    private val countReviews = itemView.findViewById<AppCompatTextView>(R.id.reviews_count_movie)
    private val title = itemView.findViewById<AppCompatTextView>(R.id.title_movie)
    //private val timeLine = itemView.findViewById<AppCompatTextView>(R.id.time_film)
    private val listStarsRating = listOf<AppCompatImageView>(
        itemView.findViewById(R.id.star_one_movie),
        itemView.findViewById(R.id.star_two_movie),
        itemView.findViewById(R.id.star_three_movie),
        itemView.findViewById(R.id.star_four_movie),
        itemView.findViewById(R.id.star_five_movie)
    )

    init {
        itemView.apply {
            setOnClickListener {
                listener?.createMoviesDetailsFragment(movie = movies[adapterPosition])
            }
        }
    }

    fun onBind(movie: Result) {

        title.text = movie.title
        pgMovie.text = "${if (movie?.adult == true) "+18" else "+16"}"
        tagLine.text = genres?.genres?.filter { movie.genreIDS?.contains(it.id) ?: false }
            ?.joinToString(separator = " , ") { it.name }
        countReviews.text = "${movie.voteCount} reviews"
        //timeLine.text = "${movie.runtime} MIN"

        downloadImage(movie = movie)
        bindStars(countRating = (movie.voteAverage?.div(2))?.toInt() ?: 0)


        //bindFavouriteImage(isFavourite = movie.isFavourite)
    }

    private fun bindStars(countRating: Int) {
        for (i in 0 until countRating) {
            listStarsRating[i].setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_star_positive
                )
            )
        }
    }

    private fun bindFavouriteImage(isFavourite: Boolean) {
        if (isFavourite) favouriteImage.setImageDrawable(
            ContextCompat.getDrawable(
                context,
                R.drawable.ic_like_positive
            )
        ) else favouriteImage.setImageDrawable(
            ContextCompat.getDrawable(
                context,
                R.drawable.ic_like
            )
        )
    }

    private fun downloadImage(movie: Result) {
            Glide.with(context)
                .clear(imagePoster)

            Glide.with(context)
                .load(
                    configuration?.baseURL + (configuration?.posterSizes?.get(4)
                        ?: "") + movie.posterPath
                )
                .apply(imageOption)
                .into(imagePoster)
        }

}

private val RecyclerView.ViewHolder.context
    get() = this.itemView.context