package com.dedesaepulloh.moviedb.presentation.view.genre

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dedesaepulloh.moviedb.data.remote.model.response.GenresItem
import com.dedesaepulloh.moviedb.databinding.ItemsGenreBinding

class GenreAdapter (private val onGenreClick: (GenresItem) -> Unit) : RecyclerView.Adapter<GenreAdapter.GenreViewHolder>() {

    private var genreList: List<GenresItem> = emptyList()

    fun setGenres(genres: List<GenresItem>) {
        genreList = genres
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        val binding = ItemsGenreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GenreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        holder.bind(genreList[position])
    }

    override fun getItemCount(): Int = genreList.size

    inner class GenreViewHolder(private val binding: ItemsGenreBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(genre: GenresItem) {
            binding.apply {
                tvName.text = genre.name
                root.setOnClickListener{
                    onGenreClick.invoke(genre)
                }
            }
        }
    }

}