package com.mackosoft.lebonalbum.services.database

import androidx.room.*
import com.mackosoft.lebonalbum.services.model.Album

@Dao
interface AlbumDao {

    /**
     * Retrieves all existing albums from the album table.
     */
    @Query("SELECT * FROM album_table ORDER BY id ASC")
    suspend fun getAllAlbums(): List<Album>


    /**
     * Retrieves a single album, from the album table, based on its id.
     */
    @Query("SELECT * FROM album_table WHERE albumId = :id")
    suspend fun getAlbum(id: Long) : Album?


    /**
     * Insert a list of Album into the album table.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAllAlbums(albums: List<Album>)


    /**
     * Deletes any given album from the album table.
     */
    @Delete
    suspend fun deleteAlbums(vararg album: Album)
}