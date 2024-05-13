package com.example.acharyaprashantassignment

import android.media.Image
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ImageViewModel(private val imageRepository: ImageRepository) : ViewModel() {
    private val _images = MutableLiveData<ArrayList<ImageItem>>()
    val images: LiveData<ArrayList<ImageItem>> get() = _images
    private var limit = 100 // Default limit

    init {
        loadImages()
    }

    fun setLimit(newLimit: Int) {
        limit = newLimit
    }

    fun loadImages() {
        viewModelScope.launch {
            try {
                Log.d("MainActivity", "loadNextPageOfImages: $limit")
                val imagesData = imageRepository.getImages(limit)
                _images.postValue(imagesData)
            } catch (e: Exception) {
                // Handle error fetching images
                e.printStackTrace()
            }
        }
    }
}

