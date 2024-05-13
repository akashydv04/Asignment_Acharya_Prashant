import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.collection.LruCache
import androidx.recyclerview.widget.RecyclerView
import com.example.acharyaprashantassignment.ImageItem
import com.example.asignmentacharyaprashant.R
import kotlinx.coroutines.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class ImageAdapter(private val imageItems: ArrayList<ImageItem>, private val context: Context) :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    private val diskCacheDirectory = context.cacheDir
    private val maxMemory = Runtime.getRuntime().maxMemory() // Get the maximum available memory
    private val cacheSize = (maxMemory / 8).toInt() // Use 1/8th of the available memory for the cache

    private val memoryCache = LruCache<String, Bitmap>(cacheSize)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.grid_item_layout, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageItem = imageItems[position]
        val imageUrl = "${imageItem.thumbnail.domain}/${imageItem.thumbnail.basePath}/0/${imageItem.thumbnail.key}"

        // Cancel previous coroutine to avoid loading wrong image
        holder.currentJob?.cancel()
        // Load image using Kotlin coroutines and HttpURLConnection
        val job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val bitmap = loadBitmap(imageUrl)
                withContext(Dispatchers.Main) {
                    holder.imageView.setImageBitmap(bitmap)
                }
            } catch (e: IOException) {
                e.printStackTrace()
                // Handle error loading image
                withContext(Dispatchers.Main) {
                    holder.imageView.setImageResource(R.drawable.placeholder)
                }
            }
        }
        holder.currentJob = job // Save reference to the current coroutine job
    }

    override fun getItemCount(): Int {
        return imageItems.size
    }

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        var currentJob: Job? = null
    }

    private suspend fun loadBitmap(imageUrl: String): Bitmap {
        val bitmapFromMemory = memoryCache.get(imageUrl)
        if (bitmapFromMemory != null) {
            return bitmapFromMemory
        }

        val bitmapFromDisk = loadBitmapFromDiskCache(imageUrl)
        if (bitmapFromDisk != null) {
            memoryCache.put(imageUrl, bitmapFromDisk)
            return bitmapFromDisk
        }

        // If image not found in memory or disk cache, fetch from network
        val bitmapFromNetwork = fetchImageFromNetwork(imageUrl)
        memoryCache.put(imageUrl, bitmapFromNetwork)
        saveBitmapToDiskCache(imageUrl, bitmapFromNetwork)
        return bitmapFromNetwork
    }

    private fun fetchImageFromNetwork(imageUrl: String): Bitmap {
        val url = URL(imageUrl)
        val connection = url.openConnection() as HttpURLConnection
        connection.doInput = true
        connection.connect()
        val inputStream = connection.inputStream
        val bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream.close()
        return bitmap
    }

    private fun loadBitmapFromDiskCache(imageUrl: String): Bitmap? {
        val file = File(diskCacheDirectory, imageUrl.hashCode().toString())
        if (file.exists()) {
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            if (bitmap != null) {
                memoryCache.put(imageUrl, bitmap) // Cache the image in memory for faster access next time
            }
            return bitmap
        }
        return null
    }

    private fun saveBitmapToDiskCache(imageUrl: String, bitmap: Bitmap) {
        val file = File(diskCacheDirectory, imageUrl.hashCode().toString())
        val fos = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
        fos.flush()
        fos.close()
    }

    fun addImages(newImages: List<ImageItem>) {
        val startPosition = imageItems.size
        imageItems.addAll(newImages)
        notifyItemRangeInserted(startPosition, newImages.size)
    }
}
