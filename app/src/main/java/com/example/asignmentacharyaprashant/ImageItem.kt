package com.example.acharyaprashantassignment

// ImageItem.kt
data class ImageItem(
    val id: String,
    val thumbnail: Thumbnail
)

data class Thumbnail(
    val domain: String,
    val basePath: String,
    val key: String
)

