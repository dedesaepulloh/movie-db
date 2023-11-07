package com.dedesaepulloh.moviedb.di

import android.content.Context
import com.dedesaepulloh.moviedb.data.remote.ApiService
import com.dedesaepulloh.moviedb.utils.Constants.BASE_URL
import com.dedesaepulloh.moviedb.utils.NetworkConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNetworkConfig(@ApplicationContext context: Context): NetworkConfig =
        NetworkConfig(context)

    @Provides
    @Singleton
    fun provideRetrofit(
        networkConfig: NetworkConfig,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(networkConfig.client)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

}