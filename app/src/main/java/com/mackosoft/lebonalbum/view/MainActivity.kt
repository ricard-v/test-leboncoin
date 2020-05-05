package com.mackosoft.lebonalbum.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mackosoft.lebonalbum.R
import com.mackosoft.lebonalbum.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel> { object : ViewModelProvider.AndroidViewModelFactory(application) {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(application = application) as T
        }
    } }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    override fun onStart() {
        super.onStart()

        viewModel.fetchAlbums()
    }
}
