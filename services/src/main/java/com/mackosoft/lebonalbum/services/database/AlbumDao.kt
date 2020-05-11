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
     * Retrieves all existing albums from the album table, but only those which were marked as
     * favorite by the end-user.
     */
    @Query("SELECT * FROM album_table WHERE isFavorite = 0 ORDER BY ID ASC")
    suspend fun getAllUnFavoriteAlbums() : List<Album>


    /**
     * /**
     * Retrieves all existing albums from the album table, but only those which were NOT marked as
     * favorite by the end-user.
    */
     */
    @Query("SELECT * FROM album_table WHERE isFavorite = 1 ORDER BY ID ASC")
    suspend fun getAllFavoriteAlbums() : List<Album>


    /**
     * Retrieves a single album, from the album table, based on its id.
     */
    @Query("SELECT * FROM album_table WHERE albumId = :id")
    suspend fun getAlbum(id: Long) : Album?


    /**
     * Updates all fields (columns) of the given album
     */
    @Update
    suspend fun updateAlbum(album: Album)


    /**
     * Insert a given list of Album into the album table.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveAllAlbums(albums: List<Album>)


    /**
     * Deletes any given album(s) from the album table.
     */
    @Delete
    suspend fun deleteAlbums(vararg album: Album)
}