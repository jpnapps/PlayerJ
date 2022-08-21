package com.jpndev.player.utils.extensions

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

/**
 * Custom Kotlin extensions for Network operations.
 * @author JPNApps
 */

/**
 * Check whether network is available
 *
 * @param context
 * @return Whether device is connected to Network.
 */
fun Context.isNetworkAvailable(): Boolean {
    with(getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager) {
        //Device is running on Marshmallow or later Android OS.
        getNetworkCapabilities(activeNetwork)?.let {
            return it.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) && it.hasCapability(
                NetworkCapabilities.NET_CAPABILITY_VALIDATED
            )
        }
    }
    return false
}

