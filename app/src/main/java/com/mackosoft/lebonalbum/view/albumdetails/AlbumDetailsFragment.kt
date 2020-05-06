package com.mackosoft.lebonalbum.view.albumdetails

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.doOnLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.transition.*
import com.mackosoft.lebonalbum.R
import com.mackosoft.lebonalbum.databinding.FragmentAlbumdetailsBinding
import com.mackosoft.lebonalbum.viewmodel.MainViewModel

class AlbumDetailsFragment : Fragment(R.layout.fragment_albumdetails) {

    private val viewModel by activityViewModels<MainViewModel> { object : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(requireContext()) as T
        }
    } }

    private lateinit var binding: FragmentAlbumdetailsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = TransitionSet().apply {
            addTransition(ChangeBounds())
            addTransition(ChangeTransform())
            addTransition(ChangeClipBounds())
            addTransition(ChangeImageTransform())
            addListener(object : TransitionListenerAdapter() {
                override fun onTransitionEnd(transition: Transition) {
                    super.onTransitionEnd(transition)
                    binding.labelTitle
                        .animate()
                        .translationY(0f)
                        .alpha(1f)
                }
            })
        }
        postponeEnterTransition() // wait for image to be loaded
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAlbumdetailsBinding.bind(view)
        setupUI()
    }


    private fun setupUI() {
        with (AlbumDetailsFragmentArgs.fromBundle(requireArguments())) {
            viewModel.fetchAlbum(albumId)
            ViewCompat.setTransitionName(binding.imageAlbum, imageTransitionName)
        }

        binding.labelTitle.doOnLayout {
            binding.labelTitle.translationY = -binding.labelTitle.measuredHeight.toFloat()
        }

        binding.imageAlbum.doOnImageLoaded { startPostponedEnterTransition() }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.selectedAlbum.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { album ->
                binding.imageAlbum.loadImageWithPicasso(album.url)
                binding.labelTitle.text = album.title
            }
        }
    }
}