package com.dedesaepulloh.moviedb.presentation.view.movie

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.dedesaepulloh.moviedb.R
import com.dedesaepulloh.moviedb.data.remote.model.response.ResultsItem
import com.dedesaepulloh.moviedb.databinding.ItemsMovieBinding
import com.dedesaepulloh.moviedb.utils.Constants.BASE_IMAGE_URL

class MovieAdapter(private val onMovieClick: (ResultsItem) -> Unit) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    private var movies: MutableList<ResultsItem> = mutableListOf()

    fun setMovies(movie: List<ResultsItem>) {
        movies.addAll(movie)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MovieAdapter.MovieViewHolder {
        val binding = ItemsMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieAdapter.MovieViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount(): Int = movies.size

    inner class MovieViewHolder(private val binding: ItemsMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: ResultsItem) {
            movie.apply {
                binding.apply {
                    tvVote.text = voteAverage.toString()
                    Glide.with(itemView)
                        .load("$BASE_IMAGE_URL$posterPath")
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .placeholder(R.drawable.ic_image)
                        .into(imgPoster)
                    itemView.setOnClickListener {
                        onMovieClick.invoke(movie)
                    }
                }
            }
        }
    }

}