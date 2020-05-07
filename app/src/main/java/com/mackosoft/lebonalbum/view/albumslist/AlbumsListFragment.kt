package com.mackosoft.lebonalbum.view.albumslist

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.transition.TransitionSet
import com.mackosoft.lebonalbum.R
import com.mackosoft.lebonalbum.databinding.AlbumslistItemDefaultBinding
import com.mackosoft.lebonalbum.databinding.FragmentAlbumslistBinding
import com.mackosoft.lebonalbum.model.DisplayableAlbum
import com.mackosoft.lebonalbum.viewmodel.main.MainViewModel

class AlbumsListFragment : Fragment(R.layout.fragment_albumslist), AlbumHandler {

    private companion object {

        private const val KEY_LIST_STATE = "list_state"

    }


    private val viewModel by activityViewModels<MainViewModel> { object : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(
                requireContext()
            ) as T
        }
    } }

    private lateinit var binding: FragmentAlbumslistBinding
    private val adapter = AlbumsListAdapter(this)

    private var listState : Parcelable? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        listState = savedInstanceState?.getParcelable(KEY_LIST_STATE)

        exitTransition = TransitionSet() // no transition
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAlbumslistBinding.bind(view)
        setupUI()
    }


    private fun setupUI() {
        binding.albumsList.adapter = adapter
        binding.albumsList.addItemDecoration(AlbumsListDecoration(binding.albumsList, adapter.isHeader))
        binding.albumsList.setHasFixedSize(true)
        binding.refresher.setOnRefreshListener { viewModel.fetchAlbums() }
        (binding.albumsList.layoutManager as? GridLayoutManager)?.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            // in landscape orientation, this allows the header to take the full width
            override fun getSpanSize(position: Int): Int {
                return if (adapter.isHeader(position))
                    resources.getInteger(R.integer.fragment_albums_list_landscape_span_count)
                else
                    1
            }
        }

        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)

        postponeEnterTransition()
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // safe to interact with viewModel here
        viewModel.isFetchingAlbums.observe(viewLifecycleOwner) { binding.refresher.isRefreshing = it }
        viewModel.albums.observe(viewLifecycleOwner) {
            adapter.submitList(it) { restoreListState() }
        }

        if (viewModel.albums.value.isNullOrEmpty()) {
            startPostponedEnterTransition()
        } else {
            adapter.submitList(viewModel.albums.value!!) {
                (binding.root.parent as? ViewGroup)?.doOnPreDraw {
                    startPostponedEnterTransition()
                }
            }
        }
    }


    private fun restoreListState() {
        binding.albumsList.post {
            binding.albumsList.layoutManager?.onRestoreInstanceState(listState)
                .also { listState = null } // consumed
        }
    }


    override fun onResume() {
        super.onResume()
        viewModel.fetchAlbums()
    }


    override fun onAlbumSelected(displayableAlbum: DisplayableAlbum, binding: AlbumslistItemDefaultBinding) {
        val directions = AlbumsListFragmentDirections.actionAlbumsListFragmentToAlbumDetailsFragment(
            ViewCompat.getTransitionName(binding.imageThumbnail)!!
        )

        viewModel.setSelectedAlbum(displayableAlbum)

        // ready to show details
        val extras = FragmentNavigatorExtras(
            binding.imageThumbnail to ViewCompat.getTransitionName(binding.imageThumbnail)!!
        )

        findNavController().navigate(directions, extras)
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelable(
            KEY_LIST_STATE,
            binding.albumsList.layoutManager?.onSaveInstanceState()
        )
    }

}