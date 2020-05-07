package com.mackosoft.lebonalbum.common.extensions

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope


val Fragment.viewLifecycleScope : LifecycleCoroutineScope
    get() = viewLifecycleOwner.lifecycleScope