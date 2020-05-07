package com.mackosoft.lebonalbum.common.extensions

import android.view.View
import android.view.ViewTreeObserver
import androidx.core.view.ViewCompat

inline fun View.doOnEachNextLayout(crossinline action: (view: View) -> Unit) {
    addOnLayoutChangeListener { view, _, _, _, _, _, _, _, _ -> action(view) }
}