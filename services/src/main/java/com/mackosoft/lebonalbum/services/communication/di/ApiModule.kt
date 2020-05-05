package com.mackosoft.lebonalbum.services.communication.di

import com.mackosoft.lebonalbum.services.communication.ApiClient
import com.mackosoft.lebonalbum.services.communication.ApiService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object ApiModule {

    @Provides
    @Singleton
    fun providesApiClient(apiService: ApiService) =
        ApiClient(apiService = apiService)
}