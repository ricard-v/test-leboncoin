package com.mackosoft.lebonalbum.common.imageview

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide

class GlideImageView(context: Context, attrs: AttributeSet?) : AppCompatImageView(context, attrs) {

    fun loadImage(url: String) = Glide.with(this).load(url).into(this)

}