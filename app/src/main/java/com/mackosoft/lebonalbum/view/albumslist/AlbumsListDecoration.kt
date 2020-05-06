package com.mackosoft.lebonalbum.view.albumslist

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.mackosoft.lebonalbum.common.extensions.asPx

class AlbumsListDecoration : RecyclerView.ItemDecoration() {

    private val marginTop       = 16.asPx
    private val marginStart     = 16.asPx
    private val marginEnd       = 16.asPx
    private val marginBottom    = 16.asPx


    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {

        outRect.top = marginTop
        outRect.left = marginStart
        outRect.right = marginEnd
        outRect.bottom = marginBottom

    }

}