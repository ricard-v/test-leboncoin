package com.mackosoft.lebonalbum.view.albumslist

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mackosoft.lebonalbum.R
import kotlin.random.Random

class AlbumsListFragment : Fragment(R.layout.fragment_albumslist) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.textView)?.let { it.setOnClickListener {
            findNavController().navigate(
                AlbumsListFragmentDirections.actionAlbumsListFragmentToAlbumDetailsFragment(
                    Random(System.currentTimeMillis()).nextLong()
                )
            )
        } }
    }
}