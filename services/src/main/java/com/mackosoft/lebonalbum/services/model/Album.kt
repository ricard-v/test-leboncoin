package com.mackosoft.lebonalbum.services.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "album_table")
data class Album (
    @PrimaryKey @ColumnInfo val id: Long,
    @ColumnInfo             val albumId: Long,
    @ColumnInfo             val title: String,
    @ColumnInfo             val url: String,
    @ColumnInfo             val thumbnailUrl: String,
    @ColumnInfo             var isFavorite: Boolean = false
)