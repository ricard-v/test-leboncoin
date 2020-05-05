package com.mackosoft.lebonalbum.services.database.di

import android.content.Context
import com.mackosoft.lebonalbum.services.database.AlbumDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Inject
import javax.inject.Singleton

@Module
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(context: Context) = AlbumDatabase.getDatabase(context = context).albumDao()

}