package com.dedesaepulloh.moviedb.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dedesaepulloh.moviedb.data.remote.model.response.GenresItem
import com.dedesaepulloh.moviedb.data.remote.model.response.ResultsItem
import com.dedesaepulloh.moviedb.data.remote.model.response.ReviewsItem
import com.dedesaepulloh.moviedb.data.remote.model.response.TrailersItem
import com.dedesaepulloh.moviedb.domain.usecase.MovieUseCase
import com.dedesaepulloh.moviedb.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(private val useCase: MovieUseCase) : ViewModel() {

    private val _genres = MutableStateFlow<Resource<List<GenresItem>>>(Resource.Loading)
    val genres: StateFlow<Resource<List<GenresItem>>> = _genres

    private val _movies = MutableStateFlow<Resource<List<ResultsItem>>>(Resource.Loading)
    val movies: StateFlow<Resource<List<ResultsItem>>> = _movies

    private val _trailers = MutableStateFlow<Resource<List<TrailersItem>>>(Resource.Loading)
    val trailers: StateFlow<Resource<List<TrailersItem>>> = _trailers

    private val _reviews = MutableStateFlow<Resource<List<ReviewsItem>>>(Resource.Loading)
    val reviews: StateFlow<Resource<List<ReviewsItem>>> = _reviews

    private val allMovies = mutableListOf<ResultsItem>()

    fun loadGenre() {
        viewModelScope.launch {
            _genres.value = useCase.getGenre()
        }
    }

    fun loadMovies(genreIds: Int, page: Int = 1) {
        viewModelScope.launch {
            val result = useCase.getMovie(
                genreIds = genreIds,
                page = page
            )

            if (result is Resource.Success) {
                val newMovies = result.data
                val uniqueMovies = newMovies.filter { newMovie ->
                    !allMovies.any { it.id == newMovie.id }
                }
                allMovies.addAll(uniqueMovies)
            }

            _movies.value = result
        }
    }

    fun loadTrailers(movieId: Int) {
        viewModelScope.launch {
            _trailers.value = useCase.getTrailer(movieId)
        }
    }

    fun loadReviews(movieId: Int, page: Int = 1) {
        viewModelScope.launch {
            _reviews.value = useCase.getReview(movieId, page)
        }
    }

}