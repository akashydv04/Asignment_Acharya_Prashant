package com.example.acharyaprashantassignment

import android.media.Image
import okio.IOException

class ImageRepository {
    suspend fun getImages(limit: Int): ArrayList<ImageItem> {
        // Fetch images from the API
        try {
            val response = RetrofitClient.imageService.getImages(limit)
            if (response.isSuccessful) {
                return ArrayList(response.body() ?: emptyList())
            } else {
                // Handle error response
                throw Exception("Failed to fetch images: ${response.code()}")
            }
        } catch (e: IOException) {
            // Handle network error
            throw e
        }
    }
}
