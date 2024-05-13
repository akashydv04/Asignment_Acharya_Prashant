package com.example.asignmentacharyaprashant

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import com.example.asignmentacharyaprashant.Util.Companion.isInternetConnected

class NetworkChangeReceiver : BroadcastReceiver() {

    private var listener: NetworkChangeListener? = null

    fun setNetworkChangeListener(listener: NetworkChangeListener) {
        this.listener = listener
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == ConnectivityManager.CONNECTIVITY_ACTION) {
            val isConnected = isInternetConnected(context!!) ?: false
            if (isConnected) {
                listener?.onNetworkConnected()
            }
            else{
                listener?.onNetworkDisconnected()
            }
        }
    }
}
