package com.mackosoft.lebonalbum.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.mackosoft.lebonalbum.di.ContextModule
import com.mackosoft.lebonalbum.di.DaggerAppComponent
import com.mackosoft.lebonalbum.services.communication.ApiClient
import com.mackosoft.lebonalbum.services.communication.di.ApiModule
import com.mackosoft.lebonalbum.services.communication.di.RetrofitModule
import com.mackosoft.lebonalbum.services.database.AlbumDao
import com.mackosoft.lebonalbum.services.database.AlbumDatabase
import com.mackosoft.lebonalbum.services.database.di.DatabaseModule
import com.mackosoft.lebonalbum.services.model.Album
import com.mackosoft.lebonalbum.services.model.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


class MainViewModel(application: Application) : AndroidViewModel(application) {

    @Inject
    lateinit var apiClient: ApiClient
    @Inject
    lateinit var database: AlbumDao

    private val _albums = MutableLiveData<List<Album>>()
    val album: LiveData<List<Album>>
        get() =_albums


    init {
        DaggerAppComponent
            .builder()
            .providesContext(ContextModule(application))
            .providesRetrofitModule(RetrofitModule)
            .providesApiModule(ApiModule)
            .providesDatabaseModule(DatabaseModule)
            .build()
            .inject(this)
    }



    fun fetchAlbums() {
        viewModelScope.launch {
            val result = apiClient.getAlbums()

            if (result is Result.Success) {
                _albums.postValue(result.data)
                database.saveAllAlbums(result.data)
            } else if (result is Result.Error) {
                _albums.postValue(withContext(Dispatchers.IO) { database.getAllAlbums() })
            }
        }
    }
}