package com.mackosoft.lebonalbum.view.albumslist

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionSet
import com.mackosoft.lebonalbum.R
import com.mackosoft.lebonalbum.databinding.AlbumslistItemDefaultBinding
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        exitTransition = TransitionSet() // no transition
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAlbumslistBinding.bind(view)
        setupUI()
    }


    private fun setupUI() {
        binding.albumsList.adapter = adapter
        binding.albumsList.addItemDecoration(AlbumsListDecoration())
        binding.albumsList.setHasFixedSize(true)
        binding.refresher.setOnRefreshListener { viewModel.fetchAlbums() }

        postponeEnterTransition()
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // safe to interact with viewModel here
        viewModel.isFetchingAlbums.observe(viewLifecycleOwner) { binding.refresher.isRefreshing = it }
        viewModel.albums.observe(viewLifecycleOwner) { adapter.submitList(it) }

        // ready to fetch albums if needed
        if (viewModel.albums.value!!.isEmpty()) {
            viewModel.fetchAlbums()
            startPostponedEnterTransition()
        } else {
            adapter.submitList(viewModel.albums.value!!) {
                (binding.root.parent as? ViewGroup)?.doOnPreDraw {
                    startPostponedEnterTransition()
                }
            }
        }
    }


    override fun onAlbumSelected(displayableAlbum: DisplayableAlbum, binding: AlbumslistItemDefaultBinding) {
        val directions = AlbumsListFragmentDirections.actionAlbumsListFragmentToAlbumDetailsFragment(
            displayableAlbum.album.id,
            ViewCompat.getTransitionName(binding.imageThumbnail)!!
        )

        val extras = FragmentNavigatorExtras(
            binding.imageThumbnail to ViewCompat.getTransitionName(binding.imageThumbnail)!!
        )

        findNavController().navigate(directions, extras)
    }
}