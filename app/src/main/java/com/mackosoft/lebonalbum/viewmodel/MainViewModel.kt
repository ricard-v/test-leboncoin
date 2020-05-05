package com.mackosoft.lebonalbum.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mackosoft.lebonalbum.di.DaggerApiComponent
import com.mackosoft.lebonalbum.services.communication.ApiClient
import com.mackosoft.lebonalbum.services.communication.di.ApiModule
import com.mackosoft.lebonalbum.services.communication.di.RetrofitModule
import com.mackosoft.lebonalbum.services.database.AlbumDatabase
import com.mackosoft.lebonalbum.services.model.Album
import com.mackosoft.lebonalbum.services.model.Result
import kotlinx.coroutines.launch
import javax.inject.Inject


class MainViewModel : ViewModel() {

    @Inject
    lateinit var apiClient: ApiClient

    private val _albums = MutableLiveData<List<Album>>()
    val album : LiveData<List<Album>>
        get() =_albums


    init {
        DaggerApiComponent
            .builder()
            .providesRetrofitModule(RetrofitModule)
            .providesApiModule(ApiModule)
            .build()
            .inject(this)
    }



    fun fetchAlbums(context: Context) {
        viewModelScope.launch {
            val result = apiClient.getAlbums()

            if (result is Result.Success) {
                _albums.postValue(result.data)

                AlbumDatabase.getDatabase(context).albumDao().saveAllAlbums(result.data)

            } else if (result is Result.Error) {
                _albums.postValue(emptyList())
            }
        }
    }
}