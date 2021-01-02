package com.example.moviesapp.fragments.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviesapp.R
import com.example.moviesapp.adapters.ActorsAdapter
import com.example.moviesapp.common.ViewModelsFactory
import com.example.moviesapp.pojo.movies.details.ResponseMovieDetail
import com.example.moviesapp.utils.imageOption


class FragmentMoviesDetails : Fragment() {
    private val adapter = ActorsAdapter()
    private val viewModel: MoviesDetailsViewModel by viewModels { ViewModelsFactory() }
    private val movie: ResponseMovieDetail by lazy { arguments?.get(SAVE_MOVIE_DATA_KEY) as ResponseMovieDetail }
    private var ageRating: AppCompatTextView? = null
    private var titleMovie: AppCompatTextView? = null
    private var tagLine: AppCompatTextView? = null
    private var reviewsCount: AppCompatTextView? = null
    private var storyLine: AppCompatTextView? = null
    private var detailPoster: AppCompatImageView? = null
    private var progressBar: ProgressBar? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_movies_details, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner, this::setStateLoading)

        viewModel.startLoadingData()
        setupViews(view = view)
        bindViews(view = view)
        setupRecyclerView(view = view)
        view.findViewById<AppCompatTextView>(R.id.back_activity).setOnClickListener {
            activity?.onBackPressed()
        }
        view.findViewById<AppCompatTextView>(R.id.back_activity_path).setOnClickListener {
            activity?.onBackPressed()
        }
        viewModel.stopLoadingData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        detailPoster = null
        ageRating = null
        titleMovie = null
        tagLine = null
        reviewsCount = null
        storyLine = null
    }

    private fun setStateLoading(state: Boolean) {
        progressBar?.isVisible = state
    }

    private fun setupViews(view: View) {
        detailPoster = view.findViewById(R.id.detail_poster)
        ageRating = view.findViewById(R.id.pg)
        titleMovie = view.findViewById(R.id.title_movie)
        tagLine = view.findViewById(R.id.tag_line)
        reviewsCount = view.findViewById(R.id.reviews_count)
        storyLine = view.findViewById(R.id.storyline_description)
        progressBar = view.findViewById(R.id.progress_bar_details)
    }

    private fun setupRecyclerView(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.actors_recycler_view)
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter
        //adapter.bindActors(newActors = movie.actors)
        adapter.notifyDataSetChanged()
    }

    private fun bindViews(view: View) {
        /*
        ageRating?.text = "+${movie.minimumAge}"
        titleMovie?.text = movie.title
        tagLine?.text = movie.genres.joinToString(separator = " , ") { it.name }
        reviewsCount?.text = "${movie.numberOfRatings} reviews"
        storyLine?.text = movie.overview
        downloadPoster(detailPoster = detailPoster)
        bindStars(view = view, countRating = (movie.ratings / 2).toInt())


         */
    }

    private fun bindStars(view: View, countRating: Int) {
        val listStarsRating = listOf<AppCompatImageView>(
            view.findViewById(R.id.star_one),
            view.findViewById(R.id.star_two),
            view.findViewById(R.id.star_three),
            view.findViewById(R.id.star_four),
            view.findViewById(R.id.star_five)
        )
        for (i in 0 until countRating) {
            listStarsRating[i].setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_star_positive
                )
            )
        }
    }

    private fun downloadPoster(detailPoster: AppCompatImageView?) {
        /*
        if (detailPoster != null) {
            Glide.with(requireContext())
                .clear(detailPoster)

            Glide.with(requireContext())
                .load(movie.poster)
                .apply(imageOption)
                .into(detailPoster)
        }

         */
    }

    companion object {
        fun newInstance(movie: ResponseMovieDetail): FragmentMoviesDetails {
            val fragment = FragmentMoviesDetails()
            val bundle = Bundle()
            //bundle.putParcelable(SAVE_MOVIE_DATA_KEY, movie)
            fragment.arguments = bundle
            return fragment
        }

        private const val SAVE_MOVIE_DATA_KEY = "SAVE_MOVIE_DATA_KEY"
    }


}