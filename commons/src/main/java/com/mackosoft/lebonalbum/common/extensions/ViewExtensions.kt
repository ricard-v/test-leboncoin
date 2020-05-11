package com.mackosoft.lebonalbum.common.extensions

import android.view.View

inline fun View.doOnEachNextLayout(crossinline action: (view: View) -> Unit) {
    addOnLayoutChangeListener { view, _, _, _, _, _, _, _, _ -> action(view) }
}