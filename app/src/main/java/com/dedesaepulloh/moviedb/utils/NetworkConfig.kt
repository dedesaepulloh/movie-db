package com.dedesaepulloh.moviedb.utils

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.dedesaepulloh.moviedb.BuildConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkConfig @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level =
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
    }
    val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request()
            val queryParams = request.url.newBuilder()
                .addQueryParameter("api_key", BuildConfig.API_KEY)
                .build()
            val newRequest = request.newBuilder()
                .url(queryParams)
                .addHeader("accept", "application/json")
                .addHeader(
                    "Authorization",
                    "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJiNzBkMzU4ZDQ0NDhiNWI0MGIzYmE1MjFiYTU0NjliZCIsInN1YiI6IjYwZGVkYWZiY2I5ZjRiMDA1ZjYwNTkxYyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.jwXZDIKxPDozageOLsGYLGyk5PX3GJH-n-p8JilTf-E"
                )
                .build()
            chain.proceed(newRequest)
        }
        .addInterceptor(loggingInterceptor)
        .addInterceptor(ChuckerInterceptor(context))
        .build()
}