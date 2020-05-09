package com.mackosoft.lebonalbum.services.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.mackosoft.lebonalbum.services.model.Album
import okhttp3.OkHttpClient

@Database(entities = [Album::class], version = 2, exportSchema = false)
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
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
            }

            return INSTANCE!!
        }


        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE `album_table` ADD isFavorite BOOLEAN NULL")
            }
        }
    }
}