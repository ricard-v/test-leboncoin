package com.mackosoft.lebonalbum.services.communication

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {

    private const val URL = "https://static.leboncoin.fr/img/shared/"

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    val apiService: ApiService = getRetrofit().create(ApiService::class.java)
}