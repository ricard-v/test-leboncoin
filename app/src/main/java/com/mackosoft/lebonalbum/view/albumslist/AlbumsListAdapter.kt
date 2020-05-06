package com.mackosoft.lebonalbum.view.albumslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mackosoft.lebonalbum.R
import com.mackosoft.lebonalbum.common.recyclerview.BindableViewHolder
import com.mackosoft.lebonalbum.databinding.AlbumslistItemDefaultBinding
import com.mackosoft.lebonalbum.model.DisplayableAlbum

class AlbumsListAdapter(private val handler: AlbumHandler) : ListAdapter<DisplayableAlbum, BindableViewHolder<DisplayableAlbum>>(itemDifferCallback) {

    private companion object {
        private val itemDifferCallback = object : DiffUtil.ItemCallback<DisplayableAlbum?>() {
            override fun areItemsTheSame(
                oldItem: DisplayableAlbum,
                newItem: DisplayableAlbum
            ) = oldItem.album.albumId == newItem.album.albumId

            override fun areContentsTheSame(
                oldItem: DisplayableAlbum,
                newItem: DisplayableAlbum
            ) = oldItem.album == newItem.album
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BindableViewHolder<DisplayableAlbum> {
        return with(LayoutInflater.from(parent.context).inflate(R.layout.albumslist_item_default, parent, false)) {
            AlbumDefaultItemViewHolder(this)
        }
    }


    override fun onBindViewHolder(holder: BindableViewHolder<DisplayableAlbum>, position: Int) {
        holder.bind(currentList[position])
    }



    private inner class AlbumDefaultItemViewHolder(itemView: View) : BindableViewHolder<DisplayableAlbum>(itemView) {

        private val binding = AlbumslistItemDefaultBinding.bind(itemView)


        init {
            itemView.setOnClickListener {
                currentList.getOrNull(adapterPosition)?.let { handler.onAlbumSelected(it) }
            }
        }


        override fun bind(value: DisplayableAlbum) {
            binding.imageThumbnail.loadImage(value.album.thumbnailUrl)
            binding.labelTitle.text = value.album.title
        }
    }

}