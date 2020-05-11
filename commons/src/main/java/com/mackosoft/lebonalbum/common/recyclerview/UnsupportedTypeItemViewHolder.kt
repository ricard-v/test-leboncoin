package com.mackosoft.lebonalbum.common.recyclerview

import android.view.View

class UnsupportedTypeItemViewHolder<Any>(itemView: View) : BindableViewHolder<Any>(itemView) {

    override fun bind(value: Any) {
        // nothing to do here
    }

}