package com.dedesaepulloh.moviedb.data.remote

import com.dedesaepulloh.moviedb.data.remote.model.response.GenreResponse
import com.dedesaepulloh.moviedb.data.remote.model.response.MovieResponse
import com.dedesaepulloh.moviedb.data.remote.model.response.ReviewResponse
import com.dedesaepulloh.moviedb.data.remote.model.response.TrailerResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("genre/movie/list")
    suspend fun getGenre(): Response<GenreResponse>

    @GET("discover/movie")
    suspend fun getMovie(
        @Query("with_genres") genreIds: Int,
        @Query("page") page: Int
    ): Response<MovieResponse>

    @GET("movie/{movie_id}/videos")
    suspend fun getTrailer(
        @Path("movie_id") movieId: Int
    ): Response<TrailerResponse>

    @GET("movie/{movie_id}/reviews")
    suspend fun getReview(
        @Path("movie_id") movieId: Int,
        @Query("page") page: Int
    ): Response<ReviewResponse>

}