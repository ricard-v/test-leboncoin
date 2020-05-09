package com.mackosoft.lebonalbum.viewmodel.main

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mackosoft.lebonalbum.di.ContextModule
import com.mackosoft.lebonalbum.di.DaggerAppComponent
import com.mackosoft.lebonalbum.model.DisplayableAlbum
import com.mackosoft.lebonalbum.model.favoriteHeaderItem
import com.mackosoft.lebonalbum.model.unFavoriteHeaderItem
import com.mackosoft.lebonalbum.services.communication.ApiClient
import com.mackosoft.lebonalbum.services.communication.di.ApiModule
import com.mackosoft.lebonalbum.services.communication.di.RetrofitModule
import com.mackosoft.lebonalbum.services.database.AlbumDao
import com.mackosoft.lebonalbum.services.database.di.DatabaseModule
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

    private val _albums = MutableLiveData<List<DisplayableAlbum>>()
    val albums: LiveData<List<DisplayableAlbum>>
        get() =_albums

    private val _selectedAlbum = MutableLiveData<DisplayableAlbum>()
    val selectedAlbum: LiveData<DisplayableAlbum>
        get() = _selectedAlbum

    private val favoritesAlbums = mutableListOf<DisplayableAlbum>()
    private val unFavoritesAlbums = mutableListOf<DisplayableAlbum>()


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
                fetchFromDatabase()
                _albums.postValue(buildAlbumsList())
            } else if (result is Result.Error) {
                // TODO handle / show error
                _albums.postValue(withContext(Dispatchers.IO) { database.getAllAlbums().map { DisplayableAlbum(it) } })
            }

            _isFetchingAlbums.postValue(false)
        }
    }


    private suspend fun fetchFromDatabase() {
        favoritesAlbums.clear()
        unFavoritesAlbums.clear()
        favoritesAlbums.addAll(database.getAllFavoriteAlbums().map { DisplayableAlbum(it) })
        unFavoritesAlbums.addAll(database.getAllUnFavoriteAlbums().map { DisplayableAlbum(it) })
    }


    private suspend fun buildAlbumsList() : List<DisplayableAlbum> {
        return mutableListOf<DisplayableAlbum>().apply {
            withContext(Dispatchers.Default) {
                if (favoritesAlbums.isNotEmpty()) {
                    favoritesAlbums.sortBy { it.album!!.id }
                    add(favoriteHeaderItem)
                    addAll(favoritesAlbums)
                }

                if (unFavoritesAlbums.isNotEmpty()) {
                    unFavoritesAlbums.sortBy { it.album!!.id }
                    add(unFavoriteHeaderItem)
                    addAll(unFavoritesAlbums)
                }
            }
        }.toList()
    }


    fun setSelectedAlbum(displayableAlbum: DisplayableAlbum) {
        _selectedAlbum.value = displayableAlbum
    }

    suspend fun setSelectedAlbumFavoriteOrNot(isFavorite: Boolean) {
        with(_selectedAlbum.value!!) {
            this.album!!.isFavorite = isFavorite
            withContext(Dispatchers.IO) { database.updateAlbum(this@with.album) }

            // place album into correct list
            if (isFavorite) {
                unFavoritesAlbums.remove(this)
                favoritesAlbums.add(this)
            } else {
                favoritesAlbums.remove(this)
                unFavoritesAlbums.add(this)
            }
            // rebuild displayable list
            _albums.postValue(buildAlbumsList())
        }
    }
}