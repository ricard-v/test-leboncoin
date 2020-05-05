package com.mackosoft.lebonalbum.services.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mackosoft.lebonalbum.services.model.Album

@Database(entities = [Album::class], version = 1, exportSchema = false)
abstract class AlbumDatabase : RoomDatabase() {

    abstract fun albumDao() : AlbumDao

    companion object {

        private var INSTANCE: AlbumDatabase? = null

        fun getDatabase(context: Context) : AlbumDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    AlbumDatabase::class.java,
                    "album_database"
                ).build()
            }

            return INSTANCE!!
        }
    }
}