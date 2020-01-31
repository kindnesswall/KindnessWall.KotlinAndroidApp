package com.farshidabz.kindnesswall.utils.helper

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity


/**
 * Created by farshid.abazari since 1/24/19
 *
 * Usage:
 *
 * How to call:
 *
 * Useful parameter:
 *
 */

fun getDeviceWidth(context: Context): Int {
    val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = wm.defaultDisplay
    val size = Point()
    display.getSize(size)
    return size.x
}

fun getDeviceWidth(context: AppCompatActivity): Int {
    val display = context.windowManager.defaultDisplay
    val size = Point()
    display.getSize(size)
    return size.x
}

fun getDeviceHeight(context: AppCompatActivity): Int {
    val display = context.windowManager.defaultDisplay
    val size = Point()
    display.getSize(size)
    return size.y
}

fun getDeviceHeight(context: Context): Int {
    val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = wm.defaultDisplay
    val size = Point()
    display.getSize(size)
    return size.y
}

fun getDeviceAspectRatio(context: Context): Int {
    val metrics = context.resources.displayMetrics
    return metrics.heightPixels / metrics.widthPixels
}

fun isInternetAvailable(activity: Activity?): Boolean {
    var result = false
    val manager = activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        if (manager != null) {
            val capabilities = manager.getNetworkCapabilities(manager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    result = true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    result = true
                }
            }
        }
    } else {
        if (manager != null) {
            if (activity == null) return false
            val connectivityManager =
                activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            val networkInfo = connectivityManager?.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }
    }
    return result
}