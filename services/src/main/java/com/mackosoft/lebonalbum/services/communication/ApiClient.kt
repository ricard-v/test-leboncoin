package com.mackosoft.lebonalbum.services.communication

import android.util.Log
import com.mackosoft.lebonalbum.services.model.Album
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.mackosoft.lebonalbum.services.model.Result
import javax.inject.Inject

class ApiClient @Inject constructor(private val apiService: ApiService) {

    companion object {
        private const val TAG = "ApiClient"
    }


    /**
     * Request from the API Service to fetch the album from the API end point
     */
    suspend fun getAlbums() : Result<List<Album>> {
        return try {
            withContext(Dispatchers.IO) {
                Result.Success( apiService.getAlbums() ).also {
                    Log.i(TAG, "[getAlbums] successfully got albums list.")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "[getAlbums] failed to get list of albums -> ${e.message}")
            Log.getStackTraceString(e)
            Result.Error("[getAlbums] network call failed")
        }
    }
}