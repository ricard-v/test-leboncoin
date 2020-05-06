package com.mackosoft.lebonalbum.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.mackosoft.lebonalbum.common.livedata.ViewModelEvent
import com.mackosoft.lebonalbum.di.ContextModule
import com.mackosoft.lebonalbum.di.DaggerAppComponent
import com.mackosoft.lebonalbum.model.DisplayableAlbum
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


class MainViewModel(context: Context) : ViewModel() {

    @Inject
    lateinit var apiClient: ApiClient
    @Inject
    lateinit var database: AlbumDao

    private val _isFetchingAlbums = MutableLiveData(false)
    val isFetchingAlbums: LiveData<Boolean>
        get() = _isFetchingAlbums

    private val _albums = MutableLiveData<List<DisplayableAlbum>>(emptyList())
    val albums: LiveData<List<DisplayableAlbum>>
        get() =_albums

    private val _selectedAlbum = MutableLiveData<ViewModelEvent<Album>>()
    val selectedAlbum: LiveData<ViewModelEvent<Album>>
        get() = _selectedAlbum


    init {
        DaggerAppComponent
            .builder()
            .providesContext(ContextModule(context))
            .providesRetrofitModule(RetrofitModule)
            .providesApiModule(ApiModule)
            .providesDatabaseModule(DatabaseModule)
            .build()
            .inject(this)
    }



    fun fetchAlbums() {
        viewModelScope.launch {
            _isFetchingAlbums.postValue(true)

            val result = apiClient.getAlbums()

            if (result is Result.Success) {
                database.saveAllAlbums(result.data)
                Log.d("HELLO", "Notifying observer -> ${_albums.hasActiveObservers()}")
                _albums.postValue(result.data.map { DisplayableAlbum(it) })
            } else if (result is Result.Error) {
                // TODO handle / show error
                _albums.postValue(withContext(Dispatchers.IO) { database.getAllAlbums().map { DisplayableAlbum(it) } })
            }

            _isFetchingAlbums.postValue(false)
        }
    }


    fun fetchAlbum(id: Long) {
        viewModelScope.launch {
            database.getAlbum(id)?.let {
                _selectedAlbum.postValue(ViewModelEvent(it))
            } ?: run {
                // TODO handle / show error
            }
        }
    }
}