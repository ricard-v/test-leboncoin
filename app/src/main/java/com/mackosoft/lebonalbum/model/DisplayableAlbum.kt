package com.mackosoft.lebonalbum.model

import com.mackosoft.lebonalbum.services.model.Album

data class DisplayableAlbum(
    val album: Album? = null,
    val itemType: ItemType = ItemType.DEFAULT
)

enum class ItemType {
    DEFAULT, HEADER_FAVORITE, HEADER_OTHERS
}

val favoriteHeaderItem = DisplayableAlbum(itemType = ItemType.HEADER_FAVORITE)
val unFavoriteHeaderItem = DisplayableAlbum(itemType = ItemType.HEADER_OTHERS)