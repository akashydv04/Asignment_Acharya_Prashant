package com.example.acharyaprashantassignment

import android.media.Image
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ImageService {
    @GET("/api/v2/content/misc/media-coverages")
    suspend fun getImages(@Query("limit") limit: Int): Response<ArrayList<ImageItem>>
}

