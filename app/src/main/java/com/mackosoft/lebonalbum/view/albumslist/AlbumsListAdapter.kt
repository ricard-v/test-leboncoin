package com.mackosoft.lebonalbum.view.albumslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.mackosoft.lebonalbum.R
import com.mackosoft.lebonalbum.common.imageview.NetworkImageView
import com.mackosoft.lebonalbum.common.recyclerview.BindableViewHolder
import com.mackosoft.lebonalbum.common.recyclerview.UnsupportedTypeItemViewHolder
import com.mackosoft.lebonalbum.databinding.AlbumslistItemDefaultBinding
import com.mackosoft.lebonalbum.databinding.AlbumslistItemHeaderFavoriteBinding
import com.mackosoft.lebonalbum.databinding.AlbumslistItemHeaderOthersBinding
import com.mackosoft.lebonalbum.model.DisplayableAlbum
import com.mackosoft.lebonalbum.model.ItemType

class AlbumsListAdapter(private val handler: AlbumHandler) : ListAdapter<DisplayableAlbum, BindableViewHolder<DisplayableAlbum>>(itemDifferCallback) {

    private companion object {
        private val itemDifferCallback = object : DiffUtil.ItemCallback<DisplayableAlbum?>() {
            override fun areItemsTheSame(
                oldItem: DisplayableAlbum,
                newItem: DisplayableAlbum
            ) = oldItem.itemType == newItem.itemType || oldItem.album?.albumId == newItem.album?.albumId

            override fun areContentsTheSame(
                oldItem: DisplayableAlbum,
                newItem: DisplayableAlbum
            ) = oldItem.album == newItem.album
        }
    }


    val isHeader: (itemPosition: Int) -> Boolean = {
        when (getItemViewType(it)) {
            R.layout.albumslist_item_header_favorite,
            R.layout.albumslist_item_header_others -> true
            else -> false
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BindableViewHolder<DisplayableAlbum> {

        val itemView = LayoutInflater.from(parent.context).inflate(viewType, parent, false)

        return when (viewType) {
            R.layout.albumslist_item_default -> AlbumDefaultItemViewHolder(itemView)
            R.layout.albumslist_item_header_favorite -> AlbumFavoriteHeaderItemViewHolder(itemView)
            R.layout.albumslist_item_header_others -> AlbumOthersHeaderItemViewHolder(itemView)
            else -> UnsupportedTypeItemViewHolder(itemView)
        }

    }

    override fun getItemViewType(position: Int): Int {
        return when (currentList[position].itemType) {
            ItemType.DEFAULT            -> R.layout.albumslist_item_default
            ItemType.HEADER_FAVORITE    -> R.layout.albumslist_item_header_favorite
            ItemType.HEADER_OTHERS      -> R.layout.albumslist_item_header_others
            else                        -> R.layout.unsupported_item
        }
    }


    override fun onBindViewHolder(holder: BindableViewHolder<DisplayableAlbum>, position: Int) {
        holder.bind(currentList[position])
    }



    private inner class AlbumDefaultItemViewHolder(itemView: View) : BindableViewHolder<DisplayableAlbum>(itemView) {

        private val binding = AlbumslistItemDefaultBinding.bind(itemView)


        init {
            itemView.setOnClickListener {
                currentList.getOrNull(adapterPosition)?.let { album ->
                    handler.onAlbumSelected(album, binding)
                }
            }
        }


        override fun bind(value: DisplayableAlbum) {
            binding.imageThumbnail.loadImageWithPicasso(value.album!!.thumbnailUrl)
            binding.labelTitle.text = value.album.title

            // Transition name to allow smooth transition to Details Fragment
            ViewCompat.setTransitionName(
                binding.imageThumbnail,
                itemView.resources.getString(
                    R.string.image_transition_name,
                    value.album.id
                )
            )

            NetworkImageView.preloadImageWithPicasso(value.album.url)
        }
    }


    private inner class AlbumFavoriteHeaderItemViewHolder(itemView: View) : BindableViewHolder<DisplayableAlbum>(itemView) {

        private val binding = AlbumslistItemHeaderFavoriteBinding.bind(itemView)


        override fun bind(value: DisplayableAlbum) {} // nothing to do here

    }


    private inner class AlbumOthersHeaderItemViewHolder(itemView: View) : BindableViewHolder<DisplayableAlbum>(itemView) {

        private val binding = AlbumslistItemHeaderOthersBinding.bind(itemView)


        override fun bind(value: DisplayableAlbum) {} // nothing to do here

    }

}