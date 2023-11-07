package com.dedesaepulloh.moviedb.repository

import com.dedesaepulloh.moviedb.data.remote.ApiService
import com.dedesaepulloh.moviedb.data.remote.model.response.GenresItem
import com.dedesaepulloh.moviedb.data.remote.model.response.ResultsItem
import com.dedesaepulloh.moviedb.data.remote.model.response.ReviewsItem
import com.dedesaepulloh.moviedb.data.remote.model.response.TrailersItem
import com.dedesaepulloh.moviedb.utils.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    private val api: ApiService,
) {

    suspend fun getGenre(): Resource<List<GenresItem>> {
        return try {
            val response = api.getGenre()
            if (response.isSuccessful) {
                Resource.Success(response.body()?.genres ?: emptyList())
            } else {
                Resource.Error(Exception("Failed to get genres"))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun getMovie(genreIds: Int, page: Int): Resource<List<ResultsItem>> {
        return try {
            val response = api.getMovie(genreIds = genreIds, page = page)
            if (response.isSuccessful) {
                Resource.Success(response.body()?.results ?: emptyList())
            } else {
                Resource.Error(Exception("Failed to get movies"))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun getTrailer(movieId: Int): Resource<List<TrailersItem>> {
        return try {
            val response = api.getTrailer(movieId = movieId)
            if (response.isSuccessful) {
                Resource.Success(response.body()?.results ?: emptyList())
            } else {
                Resource.Error(Exception("Failed to get trailers"))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun getReview(movieId: Int, page: Int): Resource<List<ReviewsItem>> {
        return try {
            val response = api.getReview(movieId = movieId, page = page)
            if (response.isSuccessful) {
                Resource.Success(response.body()?.results ?: emptyList())
            } else {
                Resource.Error(Exception("Failed to get reviews"))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

}