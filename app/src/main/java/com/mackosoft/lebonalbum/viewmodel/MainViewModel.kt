package com.mackosoft.lebonalbum.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mackosoft.lebonalbum.services.communication.ApiClient
import com.mackosoft.lebonalbum.services.communication.RetrofitBuilder
import com.mackosoft.lebonalbum.services.model.Album
import com.mackosoft.lebonalbum.services.model.Result
import kotlinx.coroutines.launch


class MainViewModel : ViewModel() {

    // TODO inject api client here
    private val apiClient: ApiClient = ApiClient(RetrofitBuilder.apiService)

    private val _albums = MutableLiveData<List<Album>>()
    val album : LiveData<List<Album>>
        get() =_albums


    fun fetchAlbums() {
        viewModelScope.launch {
            val result = apiClient.getAlbums()

            if (result is Result.Success) {
                _albums.postValue(result.data)
            } else if (result is Result.Error) {
                _albums.postValue(emptyList())
            }
        }
    }
}