package com.mackosoft.lebonalbum.view.albumdetails

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.mackosoft.lebonalbum.R

class AlbumDetailsFragment : Fragment(R.layout.fragment_albumdetails) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.textView)?.let {
            it.text = "album id = ${AlbumDetailsFragmentArgs.fromBundle(requireArguments()).albumId}"
        }
    }
}