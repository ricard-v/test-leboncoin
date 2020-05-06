package com.mackosoft.lebonalbum.view.albumslist

import com.mackosoft.lebonalbum.databinding.AlbumslistItemDefaultBinding
import com.mackosoft.lebonalbum.model.DisplayableAlbum

interface AlbumHandler {

    fun onAlbumSelected(displayableAlbum: DisplayableAlbum, binding: AlbumslistItemDefaultBinding)

}