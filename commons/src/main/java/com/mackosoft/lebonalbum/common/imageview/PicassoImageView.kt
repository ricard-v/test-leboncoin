package com.mackosoft.lebonalbum.common.imageview

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.squareup.picasso.Picasso

class PicassoImageView(context: Context, attrs: AttributeSet?) : AppCompatImageView(context, attrs) {

    fun loadImage(url: String) = Picasso.get().load(url).into(this)

}