package com.example.asignmentacharyaprashant

import ImageAdapter
import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.acharyaprashantassignment.ImageItem
import com.example.acharyaprashantassignment.ImageRepository
import com.example.acharyaprashantassignment.ImageViewModel
import com.example.acharyaprashantassignment.ImageViewModelFactory
import com.example.asignmentacharyaprashant.R
import com.example.asignmentacharyaprashant.Util.Companion.isInternetConnected

// MainActivity.kt
class MainActivity : AppCompatActivity(), NetworkChangeListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var imageAdapter: ImageAdapter
    private lateinit var images : java.util.ArrayList<ImageItem>
    private var currentPage = 0 // Track the current page number
    private var isDataReceived = true // Flag to track if data is received
    private lateinit var imageViewModel: ImageViewModel
    private val networkChangeReceiver = NetworkChangeReceiver()
    private lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progressBar)


        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkChangeReceiver, filter)
        networkChangeReceiver.setNetworkChangeListener(this)

        recyclerView.layoutManager = GridLayoutManager(this, 3)

        images = ArrayList() // Replace this with actual data from API
        imageAdapter = ImageAdapter(images, this)
        recyclerView.adapter = imageAdapter

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (firstVisibleItemPosition + visibleItemCount >= totalItemCount - 5) {
                    // Load more images or implement pagination if needed
                    loadNextPageOfImages()
                }
            }
        })

        val imageRepository = ImageRepository()
        val imageViewModelFactory = ImageViewModelFactory(imageRepository)
        imageViewModel = ViewModelProvider(this, imageViewModelFactory)[ImageViewModel::class.java]

        // Fetch images from the API
        loadNextPageOfImages()

    }


    private fun loadNextPageOfImages() {
        // Increment the page number and fetch the next page of images from the API
        if (isInternetConnected(this)){
            if (isDataReceived) {
                showProgressBar()
                currentPage++
                imageViewModel.setLimit(currentPage*100)
                imageViewModel.loadImages()
            }
            imageViewModel.images.observe(this) {
                hideProgressBar()

                if (it.isNotEmpty()) {
                    isDataReceived = true
                    removeCommonImagesFromList(it)
                    val handler = Handler()
                    handler.post {
                        imageAdapter.addImages(it)
                    }
                }
                else{
                    isDataReceived = false
                }
            }
        }
        else{
            Toast.makeText(this, "No Active Internet Connection", Toast.LENGTH_SHORT).show()
        }
    }

    private fun removeCommonImagesFromList(nextPageImages: java.util.ArrayList<ImageItem>) {
        for (image in images) {
            if (nextPageImages.contains(image)) {
                nextPageImages.remove(image)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(networkChangeReceiver)
    }

    override fun onNetworkConnected() {
        if (images.isEmpty()) {
            loadNextPageOfImages()
        }
    }

    override fun onNetworkDisconnected() {
        Toast.makeText(this, "No Active Internet Connection", Toast.LENGTH_SHORT).show()
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }


}
