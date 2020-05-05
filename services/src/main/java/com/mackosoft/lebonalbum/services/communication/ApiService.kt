package com.mackosoft.lebonalbum.services.communication

import com.mackosoft.lebonalbum.services.model.Album
import retrofit2.http.GET

interface ApiService {

    @GET("technical-test.json")
    suspend fun getAlbums(): List<Album>

}