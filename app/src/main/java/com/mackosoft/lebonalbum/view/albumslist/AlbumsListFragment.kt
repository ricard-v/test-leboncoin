package com.mackosoft.lebonalbum.view.albumslist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.observe
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.mackosoft.lebonalbum.R
import com.mackosoft.lebonalbum.databinding.FragmentAlbumslistBinding
import com.mackosoft.lebonalbum.model.DisplayableAlbum
import com.mackosoft.lebonalbum.viewmodel.MainViewModel

class AlbumsListFragment : Fragment(R.layout.fragment_albumslist), AlbumHandler {

    private val viewModel by activityViewModels<MainViewModel> { object : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(requireContext()) as T
        }
    } }

    private lateinit var binding: FragmentAlbumslistBinding
    private val adapter = AlbumsListAdapter(this)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAlbumslistBinding.bind(view)
        setupUI()
    }


    private fun setupUI() {
        binding.albumsList.adapter = adapter
        binding.albumsList.addItemDecoration(AlbumsListDecoration())
        binding.refresher.setOnRefreshListener { viewModel.fetchAlbums() }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // safe to call interact with viewModel here
        viewModel.isFetchingAlbums.observe(viewLifecycleOwner) { binding.refresher.isRefreshing = it }
        viewModel.album.observe(viewLifecycleOwner) { adapter.submitList(it) }

        // ready to fetch albums
        viewModel.fetchAlbums()
    }


    override fun onAlbumSelected(displayableAlbum: DisplayableAlbum) {
        val directions = AlbumsListFragmentDirections.actionAlbumsListFragmentToAlbumDetailsFragment(
            displayableAlbum.album.id
        )

        findNavController().navigate(directions)
    }
}