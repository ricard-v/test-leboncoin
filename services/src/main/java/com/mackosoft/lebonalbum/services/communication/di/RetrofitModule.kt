package com.mackosoft.lebonalbum.services.communication.di

import com.mackosoft.lebonalbum.services.communication.ApiService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
object RetrofitModule {

    private const val URL = "https://static.leboncoin.fr/img/shared/"


    @Singleton
    @Provides
    internal fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    @Singleton
    @Provides
    internal fun provideApiInterface(retrofit: Retrofit): ApiService = retrofit.create(
        ApiService::class.java)
}