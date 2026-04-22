package com.henry.fakeingresso.home

import android.content.Context
import android.net.ConnectivityManager
import androidx.activity.ComponentActivity

abstract class BaseHomeActivity : ComponentActivity()  {
    fun isConnected(): Boolean {
        val manager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = manager.activeNetwork ?: return false
        val capabilities = manager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}