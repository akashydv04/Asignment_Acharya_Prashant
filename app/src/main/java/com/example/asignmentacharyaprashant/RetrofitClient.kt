package com.example.acharyaprashantassignment

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://acharyaprashant.org/api/v2/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val imageService: ImageService by lazy {
        retrofit.create(ImageService::class.java)
    }
}
