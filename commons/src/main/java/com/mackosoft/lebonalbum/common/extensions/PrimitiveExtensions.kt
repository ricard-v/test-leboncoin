package com.mackosoft.lebonalbum.common.extensions

import android.content.res.Resources

// Inspired from https://medium.com/@johanneslagos/dp-to-px-and-viceversa-for-kotlin-d797815d852b

// Int

val Int.asDp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

val Int.asPx: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()


// Float

val Float.asDp: Float
    get() = (this / Resources.getSystem().displayMetrics.density)

val Float.asPx: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()