package com.mackosoft.lebonalbum.view.albumdetails

import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MenuItem.SHOW_AS_ACTION_IF_ROOM
import android.view.View
import android.view.ViewPropertyAnimator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.doOnLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.transition.*
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.mackosoft.lebonalbum.R
import com.mackosoft.lebonalbum.common.extensions.viewLifecycleScope
import com.mackosoft.lebonalbum.databinding.FragmentAlbumdetailsBinding
import com.mackosoft.lebonalbum.viewmodel.main.MainViewModel
import kotlinx.coroutines.launch

class AlbumDetailsFragment : Fragment(R.layout.fragment_albumdetails) {

    private val viewModel by activityViewModels<MainViewModel> { object : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(
                requireContext()
            ) as T
        }
    } }

    private lateinit var binding: FragmentAlbumdetailsBinding

    private var menuActionFavorite: MenuItem? = null
    private var menuActionUnFavorite: MenuItem? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) { // fragment was no recreated due to a configuration change
            sharedElementEnterTransition = TransitionSet().apply {
                addTransition(ChangeBounds())
                addTransition(ChangeTransform())
                addTransition(ChangeClipBounds())
                addTransition(ChangeImageTransform())
                addListener(object : TransitionListenerAdapter() {
                    override fun onTransitionEnd(transition: Transition) {
                        super.onTransitionEnd(transition)
                        animateTitleIn()
                    }
                })
            }

            sharedElementReturnTransition = TransitionSet().apply {
                addTransition(ChangeBounds())
                addTransition(ChangeTransform())
                addTransition(ChangeClipBounds())
//            addTransition(ChangeImageTransform()) --> weird position while animating
            }
        }

        exitTransition = Fade(Fade.OUT)
        enterTransition = Fade(Fade.IN)

        setHasOptionsMenu(true)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAlbumdetailsBinding.bind(view)
        setupUI(savedInstanceState)
    }


    private fun setupUI(savedInstanceState: Bundle?) {
        with (AlbumDetailsFragmentArgs.fromBundle(requireArguments())) {
            ViewCompat.setTransitionName(binding.imageAlbum, imageTransitionName)
        }

        postponeEnterTransition() // wait for image to be loaded
        binding.labelTitle.doOnLayout {
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                binding.labelTitle.translationX = binding.labelTitle.measuredWidth.toFloat()
            } else {
                binding.labelTitle.translationY = -binding.labelTitle.measuredHeight.toFloat()
            }

            if (savedInstanceState != null) { // fragment was recreated. There won't be transition animations
                animateTitleIn()
            }
        }

        binding.imageAlbum.doOnImageLoaded { startPostponedEnterTransition() }

        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    private fun animateTitleIn() : ViewPropertyAnimator {
        val animator =
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                binding.labelTitle.animate().translationX(0f)
            } else {
                binding.labelTitle.animate().translationY(0f)
            }
        return animator.alpha(1f)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.selectedAlbum.observe(viewLifecycleOwner) {
            binding.imageAlbum.loadImageWithPicasso(it.album!!.url)
            binding.labelTitle.text = it.album.title
            requireActivity().invalidateOptionsMenu()
        }

    }


    override fun onPrepareOptionsMenu(menu: Menu) {
        viewModel.selectedAlbum.value?.let {
            if (it.album!!.isFavorite) {
                menuActionUnFavorite?.let { item -> menu.removeItem(item.itemId) }
                // add un-favorite action
                menuActionUnFavorite = menu.add(R.string.fragment_album_details_menu_action_unfavorite).apply {
                    this.icon = VectorDrawableCompat.create(resources, R.drawable.ic_favorite, null)
                    this.setShowAsAction(SHOW_AS_ACTION_IF_ROOM)
                    menuActionFavorite = null
                }
            } else {
                menuActionFavorite?.let { item -> menu.removeItem(item.itemId) }
                // add favorite action
                menuActionFavorite = menu.add(R.string.fragment_album_details_menu_action_favorite).apply {
                    this.icon = VectorDrawableCompat.create(resources, R.drawable.ic_un_favorite, null)
                    this.setShowAsAction(SHOW_AS_ACTION_IF_ROOM)
                    menuActionUnFavorite = null
                }
            }
        }

        super.onPrepareOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            menuActionFavorite?.itemId    -> setFavoriteOrNot(true)
            menuActionUnFavorite?.itemId  -> setFavoriteOrNot(false)
            android.R.id.home       -> findNavController().popBackStack()
        }
        return super.onOptionsItemSelected(item)
    }


    private fun setFavoriteOrNot(isFavorite: Boolean) {
        viewLifecycleScope.launch {
            viewModel.setSelectedAlbumFavoriteOrNot(isFavorite)
            requireActivity().invalidateOptionsMenu()
        }
    }
}