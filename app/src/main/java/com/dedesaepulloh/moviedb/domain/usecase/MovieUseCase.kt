package com.dedesaepulloh.moviedb.domain.usecase

import com.dedesaepulloh.moviedb.data.remote.model.response.GenresItem
import com.dedesaepulloh.moviedb.data.remote.model.response.ResultsItem
import com.dedesaepulloh.moviedb.data.remote.model.response.ReviewsItem
import com.dedesaepulloh.moviedb.data.remote.model.response.TrailersItem
import com.dedesaepulloh.moviedb.repository.MovieRepository
import com.dedesaepulloh.moviedb.utils.Resource
import javax.inject.Inject

class MovieUseCase @Inject constructor(private val repository: MovieRepository) {

    suspend fun getGenre(): Resource<List<GenresItem>> {
        return repository.getGenre()
    }

    suspend fun getMovie(genreIds: Int, page: Int): Resource<List<ResultsItem>> =
        repository.getMovie(genreIds, page)

    suspend fun getTrailer(movieId: Int): Resource<List<TrailersItem>> =
        repository.getTrailer(movieId)

    suspend fun getReview(movieId: Int, page: Int): Resource<List<ReviewsItem>> =
        repository.getReview(movieId, page)

}